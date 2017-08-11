package com.courseratingsystem.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.courseratingsystem.app.R;

public class TimeoccupaPopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeoccupa_popup);
        getIntent();
    }
}
