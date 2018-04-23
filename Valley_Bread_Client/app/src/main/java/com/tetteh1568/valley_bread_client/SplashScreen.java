package com.tetteh1568.valley_bread_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends Activity {


    Animation uptodown,downtoup;
    ImageView i;
    TextView b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        b=(TextView) findViewById(R.id.t1);
        i=(ImageView)findViewById(R.id.i1);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.updown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downup);
        i.setAnimation(uptodown);
        b.setAnimation(downtoup);

        Thread th=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent i=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }
}
