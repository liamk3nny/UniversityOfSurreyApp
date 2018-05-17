package com.example.liamkenny.unionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = (ImageView)findViewById(R.id.splash_logo);

        Animation transition = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        logo.startAnimation(transition);
        final Intent openlogin = new Intent(SplashActivity.this, LoginActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(4000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(openlogin);
                finish();
            }
        };
        timer.start();




    }
}
