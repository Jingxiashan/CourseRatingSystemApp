package com.courseratingsystem.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.vo.Comment;
import com.courseratingsystem.app.vo.Course;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_teacher)
public class TeacherActivity extends AppCompatActivity {

    public static final String EXTRA_TEACHER_ID = "teacherid";
    @ViewInject(R.id.activity_teacher_imgview_photo)
    ImageView teacherphoto;
    @ViewInject(R.id.activiyt_teacher_listview_commentlist)
    ListView commentlist;
    @ViewInject(R.id.activity_teacher_scrollview)
    ScrollView scrollView;
    @ViewInject(R.id.activity_teacher_linearlayout_coursenames)
    LinearLayout coursenames;
    @ViewInject(R.id.activity_teacher_linearlayout_checkallcomments)
    LinearLayout checkall;
    private int teacherid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent = getIntent();
        teacherid = intent.getIntExtra(EXTRA_TEACHER_ID, -1);
        if (teacherid == -1) finish();
        commentlist.setFocusable(false);
        scrollView.scrollTo(0,0);

        //for test
        final ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course(1,"圣经与西方文化",4.5f,2,3,4,5,5,1000,new ArrayList<Course.TeacherBrief>()));
        courses.add(new Course(1,"圣经与西方文化",4.5f,2,3,4,5,5,1000,new ArrayList<Course.TeacherBrief>()));
        courses.add(new Course(1,"圣经与西方文化",4.5f,2,3,4,5,5,1000,new ArrayList<Course.TeacherBrief>()));

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
                Intent intent = new Intent(TeacherActivity.this,CommentActivity.class);
                startActivity(intent);
            }
        });

        coursenames.removeAllViews();
        LinearLayout.LayoutParams courseNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        courseNameParams.setMargins(0,5,0,5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams avgrecommParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        avgrecommParams.setMargins(0,5,20,5);
        for(int i=0;i<courses.size();i++){
            LinearLayout layout = new LinearLayout(TeacherActivity.this);
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            TextView courseName = new TextView(TeacherActivity.this);
            courseName.setText(courses.get(i).getCourseName());
            final int courseid = courses.get(i).getCourseId();
            courseName.setTextSize(16);
            courseName.setPadding(5,5,5,5);
            courseName.setTextColor(getResources().getColor(R.color.lightGreyAlpha));
            courseName.setLayoutParams(courseNameParams);

            TextView avgrecomm = new TextView(TeacherActivity.this);
            avgrecomm.setLayoutParams(avgrecommParams);
            avgrecomm.setText(String.valueOf(courses.get(i).getRecommendationScore()));
            avgrecomm.setTextSize(16);
            avgrecomm.setPadding(5,5,5,5);
            avgrecomm.setGravity(Gravity.END);
            avgrecomm.setTextColor(getResources().getColor(R.color.lightGreyAlpha));

            courseName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Clicked","CourseName");
                    Intent intent = new Intent(TeacherActivity.this,CourseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("courseid",courseid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            layout.addView(courseName);
            layout.addView(avgrecomm);
            coursenames.addView(layout);
        }
        CommentsAdapter commentsAdapter = new CommentsAdapter(TeacherActivity.this,comments);
        commentlist.setAdapter(commentsAdapter);
    }
    private static class CommentsViewHolder {
        ImageView avatar;
        RatingBar ratingBar;
        TextView nickName, timeStamp, commentContent, likeCount, courseName;
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
            CommentsViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new CommentsViewHolder();
                convertView = inflater.inflate(R.layout.item_fragment_discover_hot_comment_list, null);
                viewHolder.avatar = (ImageView) convertView.findViewById(R.id.item_fragment_discover_hot_comment_image_avatar);
                viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.item_fragment_discover_hot_comment_ratingbar_commentRating);
                viewHolder.nickName = (TextView) convertView.findViewById(R.id.item_fragment_discover_hot_comment_text_nickName);
                viewHolder.timeStamp = (TextView) convertView.findViewById(R.id.item_fragment_discover_hot_comment_text_timestamp);
                viewHolder.commentContent = (TextView) convertView.findViewById(R.id.item_fragment_discover_hot_comment_text_commentContent);
                viewHolder.likeCount = (TextView) convertView.findViewById(R.id.item_fragment_discover_hot_comment_text_likeCount);
                viewHolder.showDetail = (ImageButton) convertView.findViewById(R.id.item_fragment_discover_hot_comment_button_detail);
                viewHolder.clickLike = (ImageButton) convertView.findViewById(R.id.item_fragment_discover_hot_comment_button_like);
                viewHolder.courseName = (TextView) convertView.findViewById(R.id.item_fragment_discover_hot_comment_text_courseName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (TeacherActivity.CommentsViewHolder) convertView.getTag();
            }
            //设置头像显示
            viewHolder.ratingBar.setNumStars((int) tmpComment.getRecstar());
            viewHolder.nickName.setText(tmpComment.getNickname());
            viewHolder.timeStamp.setText(tmpComment.getTimestamp());
            viewHolder.commentContent.setText(tmpComment.getContent());
            viewHolder.likeCount.setText(String.valueOf(tmpComment.getLikecount()));

            viewHolder.showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TeacherActivity.this, CommentPopupActivity.class);
                    intent.putExtra(CommentPopupActivity.COMMENT_KEY, tmpComment);
                    startActivity(intent);
                }
            });
            viewHolder.courseName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TeacherActivity.this, CourseActivity.class);
                    intent.putExtra(CourseActivity.EXTRA_COURSE_ID, tmpComment.getCourseid());
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
                }
            });
            viewHolder.nickName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:进入个人主页
                }
            });

            return convertView;
        }
    }
}
