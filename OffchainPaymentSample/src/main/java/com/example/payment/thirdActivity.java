package com.example.payment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.tbouron.shakedetector.library.ShakeDetector;

public class thirdActivity extends AppCompatActivity {
    TextView information;
    String recipientAddress, myAddress;
    VideoView videoView;
    long current;
    Integer i = 0;
    long lastMillis;
    Runnable whileRunnable = new Runnable(){
        @Override
        public void run() {
            Log.d("SundaTest","four");
            while(true){
                try{
                    Thread.sleep(700);
                } catch(InterruptedException e){
                    Log.v("exc", "Exception Thrown : "+ e);
                }
                if(System.currentTimeMillis()-lastMillis>500){
                    videoView.pause();
                    videoView.seekTo(0);
                }
                else{
                    i++;
                    if(i%5==0){
                        Log.d("zun", "one");
                        CelerClientAPIHelper.sendPayment(recipientAddress,"1");
                        Log.d("zun", "two");
//                        CelerClientAPIHelper.checkBalance();
                        runOnUiThread(textRunnable);
                        Log.d("zun", "three");
                    }
                    Log.d("SundaTest ",i.toString());
                    videoView.start();

                }
            }
        }
    };
    Thread whileThread = new Thread(whileRunnable);
    Runnable textRunnable = new Runnable(){
        @Override
        public void run() {
            information.setText("my address: "+ myAddress+"\nrecipient address: "+recipientAddress+"\nmybalance: "+CelerClientAPIHelper.checkBalance());
        }
    };
    Thread textThread = new Thread(textRunnable);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        information = findViewById(R.id.information);
        Intent intent = getIntent();
        recipientAddress = intent.getStringExtra("recipientAddress");
        myAddress = KeyStoreHelper.getAddress();
        information.setText("my address: "+ myAddress+"\nrecipient address: "+recipientAddress+"\nmybalance: "+CelerClientAPIHelper.checkBalance());

//                CelerClientAPIHelper.checkBalance();
//        CelerClientAPIHelper.sendPayment(recipientAddress,"1");
        lastMillis = System.currentTimeMillis();
        Log.d("SundaTest","one");

        videoView = findViewById(R.id.exerciseVideo);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.vid_danc);
        Log.v("Zimb", "android.resource://" + getPackageName() + "/"
                + R.raw.vid_danc);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        Log.d("SundaTest","two");
        videoView.start();
        videoView.seekTo(0);
        videoView.pause();
        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {
                lastMillis = System.currentTimeMillis();

            }
        });
        ShakeDetector.updateConfiguration(2, 1);
        Log.d("SundaTest","three");

        whileThread.start();

    }
    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShakeDetector.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShakeDetector.destroy();
    }
}
