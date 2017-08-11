package com.courseratingsystem.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.courseratingsystem.app.R;

public class UsefullnessPopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usefullness_popup);
        getIntent();
    }
}
