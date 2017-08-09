package com.courseratingsystem.app.application;

import android.app.Application;
import android.content.SharedPreferences;

import org.xutils.x;

import okhttp3.OkHttpClient;

/**
 * Created by kongx on 2017/8/2 0002.
 */

public class MyCourseApplication extends Application {

    public static final String SERVER_URL = "http://192.168.137.1:8080/CourseRatingSystem";
    public static final String PREF_FILE_NAME = "prefs";
    public static final String PREF_INT_USERID = "userid";
    public OkHttpClient okHttpClient;

    private Integer userId;

//    public int screenWidth, screenHeight;


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        okHttpClient = new OkHttpClient();
        userId = null;
        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getPreferences();
        userId = sharedPreferences.getInt(PREF_INT_USERID, -1) == -1 ? null : sharedPreferences.getInt(PREF_INT_USERID, -1);
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public SharedPreferences getPreferences() {
        return getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
    }

    public Integer getUserId() {
        return userId;
    }

    public void login(String userid) {
        if (userid != null) {
            try {
                this.userId = Integer.parseInt(userid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferences sharedPreferences = getPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(PREF_INT_USERID, userId);
            editor.apply();
        }
    }

    public void logout() {
        userId = null;
        SharedPreferences sharedPreferences = getPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PREF_INT_USERID);
        editor.apply();
    }

    public boolean isLoggenIn() {
        return userId != null;
    }
}
