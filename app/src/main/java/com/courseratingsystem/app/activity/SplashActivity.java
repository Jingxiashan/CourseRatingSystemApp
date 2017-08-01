package com.courseratingsystem.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.courseratingsystem.app.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLogo = (ImageView) findViewById(R.id.activity_splash_image_logo);
        mLogo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_fade_in));
    }
}
