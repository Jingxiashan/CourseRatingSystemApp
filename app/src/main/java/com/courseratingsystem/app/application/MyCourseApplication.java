package com.courseratingsystem.app.application;

import android.app.Application;

import org.xutils.x;

import okhttp3.OkHttpClient;

/**
 * Created by kongx on 2017/8/2 0002.
 */

public class MyCourseApplication extends Application {

    public static final String SERVER_URL = "http://192.168.137.1:8080/CourseRatingSystem";
    public OkHttpClient okHttpClient;
    public Integer userId;
    public String username;
    public String nickname;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        okHttpClient = new OkHttpClient();
        userId = null;
        username = nickname = null;
    }
}
