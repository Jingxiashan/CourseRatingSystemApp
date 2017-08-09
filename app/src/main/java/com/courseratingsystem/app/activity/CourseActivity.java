package com.courseratingsystem.app.activity;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
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

    private int objectId[] = {R.id.activity_comment_image_courseImag, R.id.activity_comment_text_userName,
            R.id.activity_comment_text_timestamp, R.id.activity_comment_text_commentContent,
            R.id.activity_comment_text_likeCount, R.id.activity_comment_ratingbar_commentRating};
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
        MyAdapter myAdapter = new MyAdapter(CourseActivity.this);
        commentlist.setAdapter(myAdapter);
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
    class MyAdapter extends BaseAdapter {
        private Context mContext;

        public MyAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            //comment数目
            return 12;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //动态加载布局
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.activity_comment_list_item, null);
            final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.activity_comment_ratingbar_commentRating);
            ImageView courseImage = (ImageView) view.findViewById(R.id.activity_comment_image_courseImag);
            final TextView userName = (TextView) view.findViewById(R.id.activity_comment_text_userName);
            final TextView timeStamp = (TextView) view.findViewById(R.id.activity_comment_text_timestamp);
            final TextView commentContent = (TextView) view.findViewById(R.id.activity_comment_text_commentContent);
            TextView likeCount = (TextView) view.findViewById(R.id.activity_comment_text_likeCount);

            ratingBar.setNumStars(3);
            userName.setText("User_Name");
            timeStamp.setText("Comment_Timestamp");
            commentContent.setText("current comment is written by someone else.");
            likeCount.setText("10");

            final ImageButton showDetail = (ImageButton) view.findViewById(R.id.activity_comment_button_detail);
            showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View commentPopupView = layoutInflater.inflate(R.layout.activity_comment_popup, null);

                    TextView coursenamePop = (TextView) commentPopupView.findViewById(R.id.activity_commentpopup_text_coursename);
                    TextView usernamePop = (TextView) commentPopupView.findViewById(R.id.activity_commentpopup_text_userName);
                    TextView timestampPop = (TextView) commentPopupView.findViewById(R.id.activity_commentpopup_text_timestamp);
                    TextView commentcontentPop = (TextView) commentPopupView.findViewById(R.id.activity_commentpopup_text_commentContent);
                    RatingBar ratingBarPop = (RatingBar) commentPopupView.findViewById(R.id.activity_commentpopup_ratingbar_commentRating);

                    ratingBarPop.setNumStars(ratingBar.getNumStars());
                    coursenamePop.setText(courseName.getText());
                    timestampPop.setText(timeStamp.getText());
                    commentcontentPop.setText(commentContent.getText());
                    usernamePop.setText(userName.getText());

                    PopupWindow commentPopupWindow = new PopupWindow(commentPopupView, 1000, 1510);

                    commentPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                    commentPopupWindow.setFocusable(true);
                    commentPopupWindow.setOutsideTouchable(true);
                    commentPopupWindow.update();


                    commentPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


                }
            });

            return view;
        }
    }
}
