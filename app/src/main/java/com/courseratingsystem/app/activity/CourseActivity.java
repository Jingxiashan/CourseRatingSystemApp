package com.courseratingsystem.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.view.CustomizedHorizontalBarChart;
import com.courseratingsystem.app.view.ListViewNoScroll;
import com.courseratingsystem.app.view.ObservableScrollView;
import com.nineoldandroids.view.ViewHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@ContentView(R.layout.activity_course)
public class CourseActivity extends AppCompatActivity {
    @ViewInject(R.id.activity_course_horizontalbarchart)
    CustomizedHorizontalBarChart barChart;
    @ViewInject(R.id.activity_course_scrollview)
    ObservableScrollView scrollView;
    @ViewInject(R.id.activity_course_textview_avgreccom_2)
    TextView avgrecomm;
    @ViewInject(R.id.activity_course_list_commentlist)
    ListViewNoScroll commentlist;
    @ViewInject(R.id.activity_course_textview_coursename)
    TextView courseName;
    @ViewInject(R.id.activity_course_relativelayout_header)
    RelativeLayout header;
    @ViewInject(R.id.activity_course_view_headbg)
    View headbg;

    float scale = 1.0f;
    float alpha = 1.0f;
    float alpha2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        final float originalRecommHeight = avgrecomm.getLayoutParams().height;
        barChart.setBar_color(getResources().getColor(android.R.color.holo_orange_light));
        barChart.init(new float[]{1.1f,2.2f,3.3f,4.4f,4.9f});
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int l, int t, int oldl, int oldt) {
//                Log.i("ScrollView","Scroll Observed");
                float scrollY = oldt - t;
                alpha +=scrollY/400f;
                if(alpha<0){
                    alpha=0;
                }
                else if(alpha>1){
                    alpha=1;
                }
                if(scrollView.getScrollY()==0){
                    scale = 1;
                    alpha =1;
                }
                if(scrollView.getScrollY()>=-10){
                    scale =  (((2*(float)header.getHeight())-(float)scrollView.getScrollY())/(2*(float)header.getHeight()));
                    alpha2 = ((float)scrollView.getScrollY()/(float)header.getHeight());
                    if(scale<0.5){
                        scale=0.5f;
                    }
                    else if(scale>1){
                        scale=1;
                    }
                    ViewHelper.setPivotX(avgrecomm, 400);
                    ViewHelper.setPivotY(avgrecomm, -580);
                    ViewHelper.setScaleX(avgrecomm, scale);
                    ViewHelper.setScaleY(avgrecomm, scale);}
                    ViewHelper.setAlpha(header,alpha);
                    ViewHelper.setAlpha(headbg,alpha2);
                    Log.i("scrollY",((float)scrollView.getScrollY()/(2*(float)header.getHeight()))+"");
            }
        });
        commentlist.setFocusable(false);
        scrollView.scrollTo(0,0);
        headbg.setAlpha(alpha2);
    }

}
