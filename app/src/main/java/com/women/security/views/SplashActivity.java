package com.women.security.views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hihasan.prisom.toaster.Toaster;
import com.women.security.R;
import com.women.security.utils.Utils;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Thread Handler for 5 sec.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logicFun();
                checkConnection();
            }
        }, Utils.APP_LOAD_TIME);
    }

    private void logicFun() {

        Utils.sharedPreferences=getSharedPreferences(Utils.MyPreferences, Context.MODE_PRIVATE);
        String fnumber = Utils.sharedPreferences.getString(Utils.Fnumber, "");

        if (fnumber.trim().length() == 0){
            //Toast.makeText(getApplicationContext(),"Information Not Added", Toast.LENGTH_SHORT).show();

            final Intent mainIntent = new Intent(SplashActivity.this, InformationActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }

        else {
            //Toast.makeText(getApplicationContext(),"Information Added", Toast.LENGTH_SHORT).show();

            final Intent mainIntent = new Intent(SplashActivity.this, InformationActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
    }

    //Check connection Here
    private void checkConnection()
    {
        if(isOnline())
        {
            Toaster.makeText(getApplicationContext(),"You Are Online", Toaster.SUCCESS,true);
        }

        else
        {
            Toaster.makeText(getApplicationContext(),"You Are Offline", Toaster.ERROR,true);
        }
    }

    //Internet Connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
