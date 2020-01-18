package com.women.security.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.women.security.R;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Record extends AppCompatActivity {

    ImageButton send;
    TextView textshow1,textshow2,backtext,text3,last_text;
    Button  buttonPlayLastRecordAudio,
            buttonStopPlayingRecording ;
    ImageButton buttonStart, buttonStop;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        text3=(TextView) findViewById(R.id.textshow3);
        last_text=(TextView) findViewById(R.id.textshow_last);

        backtext=(TextView) findViewById(R.id.textshow3_back);
        backtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Record.this, MainActivity.class);
                startActivity(i);
            }
        });

        textshow1=(TextView) findViewById(R.id.textshow1);
        textshow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Record.this,
                        "You can Click record icon to record..", Toast.LENGTH_LONG).show();
            }
        });
        textshow2=(TextView) findViewById(R.id.textshow2);
        textshow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlayLastRecordAudio.setVisibility(View.VISIBLE);
                textshow2.setVisibility(View.INVISIBLE);
                text3.setVisibility(View.VISIBLE);
                Toast.makeText(Record.this,
                        "You must have to stop playing at first..", Toast.LENGTH_LONG).show();
            }
        });

        send=(ImageButton) findViewById(R.id.Send) ;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Record.this, EmailActivity.class);
                startActivity(i);
            }
        });

        buttonStart = (ImageButton) findViewById(R.id.button);
        buttonStop = (ImageButton) findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button)findViewById(R.id.button4);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setVisibility(View.INVISIBLE);
                buttonStop.setVisibility(View.VISIBLE);
                textshow1.setVisibility(View.INVISIBLE);
                textshow2.setVisibility(View.VISIBLE);

                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(Record.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonStart.setVisibility(View.INVISIBLE);
                buttonStop.setVisibility(View.INVISIBLE);
                send.setVisibility(View.VISIBLE);


                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(Record.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {
                buttonStopPlayingRecording.setVisibility(View.VISIBLE);
                buttonPlayLastRecordAudio.setVisibility(View.INVISIBLE);


                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(Record.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text3.setVisibility(View.INVISIBLE);
                last_text.setVisibility(View.VISIBLE);
                buttonStopPlayingRecording.setVisibility(View.INVISIBLE);
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }


                Toast.makeText(Record.this, "Recording has been stopped..",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Record.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(Record.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Record.this,"PermissionDenied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}









