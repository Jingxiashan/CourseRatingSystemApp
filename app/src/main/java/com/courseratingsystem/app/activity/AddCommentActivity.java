package com.courseratingsystem.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.courseratingsystem.app.R;

public class AddCommentActivity extends AppCompatActivity {
    public static final String COURSE_INFO = "course_info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
    }
}
