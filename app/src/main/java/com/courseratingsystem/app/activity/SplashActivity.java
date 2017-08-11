package com.courseratingsystem.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

    private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟2秒
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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

//        startIntent = new Intent(SplashActivity.this, IndexActivity.class);
//        new Timer().schedule(timerTask, 2000);

        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferences.getBoolean("firststart", true)) {
                    editor = preferences.edit();
                    //将登录标志位设置为false，下次登录时不再显示首次登录界面
                    editor.putBoolean("firststart", false);
                    editor.commit();
                    Intent intent=new Intent();
                    intent.setClass(SplashActivity.this,ViewPagerActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }else{
                    Intent intent=new Intent();
                    intent.setClass(SplashActivity.this,IndexActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        },SPLASH_DISPLAY_LENGHT);

        MyCourseApplication application = (MyCourseApplication) getApplication();
        application.loadData();
    }


//    private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟2秒
//    private SharedPreferences preferences;
//    private SharedPreferences.Editor editor;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_welcome);
//
//        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (preferences.getBoolean("firststart", true)) {
//                    editor = preferences.edit();
//                    //将登录标志位设置为false，下次登录时不再显示首次登录界面
//                    editor.putBoolean("firststart", false);
//                    editor.commit();
//                    Intent intent=new Intent();
//                    intent.setClass(WelcomeActivity.this,ViewPagerActivity.class);
//                    WelcomeActivity.this.startActivity(intent);
//                    WelcomeActivity.this.finish();
//                }else{
//                    Intent intent=new Intent();
//                    intent.setClass(WelcomeActivity.this,IndexActivity.class);
//                    WelcomeActivity.this.startActivity(intent);
//                    WelcomeActivity.this.finish();
//                }
//
//            }
//        },SPLASH_DISPLAY_LENGHT);
//    }

}
