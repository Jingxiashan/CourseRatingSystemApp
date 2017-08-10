package com.courseratingsystem.app.application;

import android.app.Application;
import android.content.SharedPreferences;

import org.xutils.x;

import okhttp3.OkHttpClient;

/**
 * Created by kongx on 2017/8/2 0002.
 */

public class MyCourseApplication extends Application {

    public static final String PREF_FILE_NAME = "prefs";
    public static final String PREF_INT_USERID = "userid";
    public static final String PREF_INT_USERNAME = "username";
    //    for internet connection
    public static final String SERVER_URL = "http://192.168.137.1:8080/CourseRatingSystem/api";
    public static final int JSON_RESULT_CODE_200 = 200;
    public static final String JSON_RESULT_CODE = "result_code";
    public static final String JSON_REASON = "reason";
    public static final String JSON_RESULT = "result";
    public OkHttpClient okHttpClient;

    private Integer userId;
    private String username;


    //    public int screenWidth, screenHeight;


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        okHttpClient = new OkHttpClient();
        userId = null;
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getPreferences();
        userId = sharedPreferences.getInt(PREF_INT_USERID, -1) == -1 ? null : sharedPreferences.getInt(PREF_INT_USERID, -1);
        username = sharedPreferences.getString(PREF_INT_USERNAME, null);
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

    public String getUsername() {
        return username;
    }

    public void login(int userId, String username) {
        this.userId = userId;
        this.username = username;
        SharedPreferences sharedPreferences = getPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_INT_USERID, this.userId);
        editor.putString(PREF_INT_USERNAME, this.username);
        editor.apply();
    }

    public void logout() {
        userId = null;
        username = null;
        SharedPreferences sharedPreferences = getPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PREF_INT_USERID);
        editor.apply();
    }

    public boolean isLoggenIn() {
        return userId != null;
    }
}
