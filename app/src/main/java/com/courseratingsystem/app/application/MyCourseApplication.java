package com.courseratingsystem.app.application;

import android.app.Application;

import org.xutils.x;

/**
 * Created by kongx on 2017/8/2 0002.
 */

public class MyCourseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
