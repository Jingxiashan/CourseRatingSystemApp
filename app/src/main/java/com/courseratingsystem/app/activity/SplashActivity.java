package com.courseratingsystem.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_splash_image_logo)
    private ImageView mLogo;

    private boolean componentReady = true;
    private Intent startIntent = null;
    final TimerTask timerTask = new TimerTask() {
        public void run() {
            synchronized (this) {
                while (!componentReady) try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            SplashActivity.this.startActivity(startIntent);
            SplashActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        mLogo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_fade_in));

        startIntent = new Intent(SplashActivity.this, IndexActivity.class);
        new Timer().schedule(timerTask, 2000);

        MyCourseApplication application = (MyCourseApplication) getApplication();
        application.loadData();
    }

}
