package com.women.security.views;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import com.hihasan.prisom.toaster.Toaster;
import com.women.security.R;
import com.women.security.event.ShakeDetector;
import com.women.security.service.ShakeService;
import com.women.security.utils.Utils;
import com.women.security.views.bot.BotMainActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{
    FloatingActionMenu fabmenu;
    FloatingActionButton call_btn,record_btn,cam_button,round_button,Round_button;

    private static final int REQUEST_CALL=1;
    public Context context = this;
    private AppCompatButton sos;
    String call,police,family;
    //public static String fnumber;

    //Google Location
    private static final String TAG = "Women Security";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private Button round;
    private Button round1;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    //Sensor
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

  // emerency..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, ShakeService.class);
        //Start Service
        startService(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Call record
        fabmenu=(FloatingActionMenu) findViewById (R.id.fabmenu);

        call_btn=(FloatingActionButton) findViewById (R.id.call);
        record_btn=(FloatingActionButton) findViewById (R.id.record);
        cam_button=(FloatingActionButton)findViewById(R.id.camera);


        FloatingAction();

        round=(Button) findViewById(R.id.round);
        round1=(Button) findViewById(R.id.round2);

        //get latitude, longitude
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone




        round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   callAction2();

            }
        });



        round1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    callAction3();
            }
        });















        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                //tvShake.setText("Shake Action is just detected!!");
                //Toast.makeText(MainActivity.this, "Shaked!!!", Toast.LENGTH_SHORT).show();

                String name=Utils.sharedPreferences.getString(Utils.FName,"");
                String lat= String.valueOf(mLocation.getLatitude());
                String lon=String.valueOf(mLocation.getLongitude());
                String msg= "Hi I Am "+ name + " I am In Trouble, My location "+ lat+" , "+lon+" Help Me.";
                String number= Utils.sharedPreferences.getString(Utils.Snumber,"");

                //Getting intent and PendingIntent instance
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                //Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(number, null, msg, pi,null);

                Toaster.makeText(getApplicationContext(), "Hi I Am "+ name + " I am In Trouble, My location "+ lat+" , "+lon+" Help Me.",Toaster.INFO,true);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private void FloatingAction() {
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAction();
            }
        });

        cam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, CamActivity.class);
                startActivity(i);
            }
        });

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toaster.makeText(getApplicationContext(),"Action Needed",Toaster.INFO,true);
                Intent i=new Intent(MainActivity.this, Record.class);
                startActivity(i);
                if (isOnline()){

                    Toaster.makeText(getApplicationContext(),"Audio Recording...will start",Toaster.INFO,true);

                }
            }
        });
    }

    private void recordAction() {
        try {
            String h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            File root = new File(Environment.getExternalStorageDirectory(), "WS");

            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, h + ".mp3");  // file path to save
            FileWriter writer = new FileWriter(filepath);
            Toaster.makeText(getApplicationContext(),"Mail Sent", Toaster.INFO);

        } catch (IOException e) {
            e.printStackTrace();
            //result.setText(e.getMessage().toString());

        }
    }

    private void callAction() {
        Utils.sharedPreferences=getSharedPreferences(Utils.MyPreferences, Context.MODE_PRIVATE);
        call=Utils.sharedPreferences.getString(Utils.Fnumber,"");

        if (call.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

            }

            else {
                String dial="tel:"+call;
                Intent call=new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse(dial));
                startActivity(call);

            }
        }

        else {
            Utils.ToastShort(getApplicationContext(),"Please Set Number in Settings");
        }
    }
    private void callAction2() {
        Utils.sharedPreferences=getSharedPreferences(Utils.MyPreferences, Context.MODE_PRIVATE);
        family=Utils.sharedPreferences.getString(Utils.Snumber,"");

        if (family.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

            }

            else {
                String dial="tel:"+family;
                Intent call2=new Intent(Intent.ACTION_DIAL);
                call2.setData(Uri.parse(dial));
                startActivity(call2);

            }
        }

        else {
            Utils.ToastShort(getApplicationContext(),"Please Set Number in Settings");
        }
    }
    private void callAction3() {
        Utils.sharedPreferences=getSharedPreferences(Utils.MyPreferences, Context.MODE_PRIVATE);
        police=Utils.sharedPreferences.getString(Utils.Police,"");

        if (police.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

            }

            else {
                String dial="tel:"+police;
                Intent call3=new Intent(Intent.ACTION_DIAL);
                call3.setData(Uri.parse(dial));
                startActivity(call3);

            }
        }

        else {
            Utils.ToastShort(getApplicationContext(),"Please Set Number in Settings");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                callAction();
           //     callAction2();
           //     callAction3();

            }

            else {
                Utils.ToastShort(getApplicationContext(),"Permission Denied");
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return true;
    }

    public void navigationAction(View view){
        if (view.getId() == R.id.about){
                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.about_us);

                AppCompatButton close=(AppCompatButton) dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
        }

        if (view.getId() == R.id.settings){
            Intent i=new Intent(MainActivity.this, InformationActivity.class);
            startActivity(i);
        }

        if (view.getId() ==R.id.help){

            if (isOnline()){
                Intent i=new Intent(MainActivity.this, BotMainActivity.class);
                startActivity(i);
            }

            else {
                Toaster.makeText(getApplicationContext(),"You are Offline",Toaster.ERROR,true);
            }

        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

//            mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
//            mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

//        String msg = "Updated Location: " +
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
//        mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
