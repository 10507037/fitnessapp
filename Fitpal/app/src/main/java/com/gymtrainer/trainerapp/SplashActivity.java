package com.gymtrainer.trainerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gymtrainer.trainerapp.Activities.SignInActivity;

public class SplashActivity extends AppCompatActivity {

    protected boolean _active = true;
    protected int _splashTime = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        runThread();

    }


    private void runThread()
    {
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {
                        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                        finish();
                }
            };
        };
        splashTread.start();
    }

}
