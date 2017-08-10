package com.courseratingsystem.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.courseratingsystem.app.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_teacher)
public class TeacherActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_teacher_imgview_photo)
    ImageView teacherphoto;
    @ViewInject(R.id.activiyt_teacher_listview_commentlist)
    ListView commentlist;
    @ViewInject(R.id.activity_teacher_scrollview)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        commentlist.setFocusable(false);
        scrollView.scrollTo(0,0);
    }


}
