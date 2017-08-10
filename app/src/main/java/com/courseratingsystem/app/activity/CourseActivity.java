package com.courseratingsystem.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.view.CustomizedHorizontalBarChart;
import com.courseratingsystem.app.view.ListViewNoScroll;
import com.courseratingsystem.app.view.ObservableScrollView;
import com.courseratingsystem.app.vo.Comment;
import com.courseratingsystem.app.vo.Course;
import com.nineoldandroids.view.ViewHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

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
    @ViewInject(R.id.activity_course_relativelayout_header)
    RelativeLayout header;
    @ViewInject(R.id.activity_course_view_headbg)
    View headbg;
    @ViewInject(R.id.activity_course_textview_coursename)
    TextView courseName;
    @ViewInject(R.id.activity_course_linearlayout_teachers)
    LinearLayout teachers;
    @ViewInject(R.id.activity_course_linearlayout_checkallcomments)
    LinearLayout checkall;
    @ViewInject(R.id.activity_course_linearlayout_head)
    LinearLayout head;
    @ViewInject(R.id.activity_course_textview_avgreccom)
    TextView getAvgrecommStatic;

    float scale = 1.0f;
    float alpha = 1.0f;
    float ratio = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        //for test

        Course course = new Course(1,"圣经与西方文化",4.5f,2.4f,4,2.1f,5,1.8f,1000,new ArrayList<Course.TeacherBrief>());
        course.getTeacherList().add(course.new TeacherBrief("鲁迪",1));
        course.getTeacherList().add(course.new TeacherBrief("黄嘉星",2));
        course.getTeacherList().add(course.new TeacherBrief("孔啸",3));

        List<Comment> comments = new ArrayList<>();
        for (int i = 0;i<12;i++){
            comments.add(new Comment(3,1,1,"鲁迪","2017-05-23","呵呵呵呵呵呵呵呵","圣经与西方文化",55));
        }
        //end of for test

        checkall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转全部评论页
                Log.i("Clicked","Checkall");
                Intent intent = new Intent(CourseActivity.this,CommentActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout.LayoutParams teacherParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        teacherParam.setMargins(5,5,5,5);
        teachers.removeAllViews();
        for (int i =0;i<course.getTeacherList().size();i++){
            TextView teacher = new TextView(CourseActivity.this);
            teacher.setText(course.getTeacherList().get(i).getTeacherName());
            teacher.setLayoutParams(teacherParam);
            teacher.setBackground(getResources().getDrawable(R.drawable.activity_course_teacher_bg));
            teacher.setPadding(10,5,10,5);
            teacher.setTextSize(15);
            teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 跳转教师页面
                    Log.i("Clicked","Teacher");
                    Intent intent = new Intent(CourseActivity.this,TeacherActivity.class);
                    startActivity(intent);
                }
            });
            teachers.addView(teacher);
        }

        barChart.setBar_color(getResources().getColor(R.color.lightGreyAlpha));
        barChart.setValue_color(getResources().getColor(R.color.Grey));
        barChart.setScores(course.getAverageRatingsRollCall(),course.getAverageRatingsScoring(),course.getAverageRatingsSpareTimeOccupation(),
                course.getAverageRatingsVividness(),course.getAverageRatingsUsefulness());
        barChart.initChart();

        courseName.setText(course.getCourseName());
        avgrecomm.setText(String.valueOf(course.getRecommendationScore()));
        getAvgrecommStatic.setText(String.valueOf(course.getRecommendationScore()));
        avgrecomm.setVisibility(View.GONE);


        CommentsAdapter commentsAdapter = new CommentsAdapter(CourseActivity.this,comments);
        commentlist.setAdapter(commentsAdapter);

        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int l, int t, int oldl, int oldt) {
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
                    avgrecomm.setVisibility(View.GONE);
                }
                if(scrollView.getScrollY()>=-10){
                    scale =  (((2*(float)header.getHeight())-(float)scrollView.getScrollY())/(2*(float)header.getHeight()));
                    ratio = ((float)scrollView.getScrollY()/(float)header.getHeight());
                    if(scale<0.5){
                        scale=0.5f;
                    }
                    else if(scale>1){
                        scale=1;
                    }
                    if(ratio >1){
                        ratio =1;
                    }
                    ViewHelper.setPivotX(avgrecomm, 1000);
                    ViewHelper.setPivotY(avgrecomm, -640);
                    ViewHelper.setScaleX(avgrecomm, scale);
                    ViewHelper.setScaleY(avgrecomm, scale);
                    ViewHelper.setAlpha(header,alpha);
                    ViewHelper.setAlpha(headbg, ratio);
                    ViewHelper.setPivotX(courseName, 0);
                    ViewHelper.setPivotY(courseName, courseName.getHeight()/2);
                    ViewHelper.setScaleX(courseName, 1-(ratio *0.2f));
                    ViewHelper.setScaleY(courseName, 1-(ratio *0.2f));
                    ViewHelper.setTranslationX(courseName,-ratio *250);
                    head.setElevation(ratio*5);
                }
                if(scrollView.getScrollY()>0&&scrollView.getScrollY()<=header.getHeight()){
                    avgrecomm.setVisibility(View.VISIBLE);
                    getAvgrecommStatic.setVisibility(View.GONE);
                }
                else {

                    getAvgrecommStatic.setVisibility(View.VISIBLE);
                }


                    Log.i("scrollY",((float)scrollView.getScrollY()/(2*(float)header.getHeight()))+"");
            }
        });
        commentlist.setFocusable(false);
        scrollView.scrollTo(0,0);
        headbg.setAlpha(ratio);


    }
    @Event(value = {R.id.activity_course_btn_addcomment}, type = View.OnClickListener.class)
    private void doEvent(View view){
        Intent intent = new Intent(CourseActivity.this,AddCommentActivity.class);
        startActivity(intent);
    }
    private static class CommentsViewHolder {
        ImageView avatar;
        RatingBar ratingBar;
        TextView nickName, timeStamp, commentContent, likeCount;
        ImageButton showDetail, clickLike;
    }

    private class CommentsAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Comment> commentList;

        public CommentsAdapter(Context context, List<Comment> commentList) {
            inflater = LayoutInflater.from(context);
            this.commentList = commentList;
        }

        public void setCommentList(List<Comment> commentList) {
            this.commentList = commentList;
        }

        @Override
        public int getCount() {
            //comment数目
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Comment tmpComment = commentList.get(position);
            CourseActivity.CommentsViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new CourseActivity.CommentsViewHolder();
                convertView = inflater.inflate(R.layout.item_activity_comment_list_custom, null);
                viewHolder.avatar = (ImageView) convertView.findViewById(R.id.item_activity_comment_image_avatar);
                viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.item_activity_comment_ratingbar_commentRating);
                viewHolder.nickName = (TextView) convertView.findViewById(R.id.item_activity_comment_text_nickName);
                viewHolder.timeStamp = (TextView) convertView.findViewById(R.id.item_activity_comment_text_timestamp);
                viewHolder.commentContent = (TextView) convertView.findViewById(R.id.item_activity_comment_text_commentContent);
                viewHolder.likeCount = (TextView) convertView.findViewById(R.id.item_activity_comment_text_likeCount);
                viewHolder.showDetail = (ImageButton) convertView.findViewById(R.id.item_activity_comment_button_detail);
                viewHolder.clickLike = (ImageButton) convertView.findViewById(R.id.item_activity_comment_button_like);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CourseActivity.CommentsViewHolder) convertView.getTag();
            }
            //设置头像显示
            viewHolder.ratingBar.setNumStars(tmpComment.getRecstar());
            viewHolder.nickName.setText(tmpComment.getNickname());
            viewHolder.timeStamp.setText(tmpComment.getTimestamp());
            viewHolder.commentContent.setText(tmpComment.getContent());
            viewHolder.likeCount.setText(String.valueOf(tmpComment.getLikecount()));

            viewHolder.showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseActivity.this, CommentPopupActivity.class);
                    intent.putExtra(CommentPopupActivity.COMMENT_KEY, tmpComment);
                    startActivity(intent);
                }
            });

            viewHolder.clickLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:点赞
                }
            });

            viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:进入个人主页
                    Intent intent = new Intent(CourseActivity.this,PersonalPopupActivity.class);
                    startActivity(intent);
                }
            });
            viewHolder.nickName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:进入个人主页
                    Intent intent = new Intent(CourseActivity.this,PersonalPopupActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

}
