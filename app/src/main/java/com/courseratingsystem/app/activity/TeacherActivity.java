package com.courseratingsystem.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.view.LoadingAnimView;
import com.courseratingsystem.app.vo.Comment;
import com.courseratingsystem.app.vo.Course;
import com.courseratingsystem.app.vo.Teacher;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_teacher)
public class TeacherActivity extends AppCompatActivity {

    public static final String EXTRA_TEACHER_ID = "teacherid";
    private static final String GET_TEACHER_URL = "/getCommentListAndCourseListByTeacherId?teacherId=";
    private static final int FAILED = 0;
    private static final int SUCCESS = 1;
    @ViewInject(R.id.activity_teacher_layout)
    RelativeLayout mRelativeLayout;
    @ViewInject(R.id.activity_teacher_textview_name)
    TextView mTeachername;
    @ViewInject(R.id.activity_teacher_imgview_photo)
    ImageView mTeacherPhoto;
    @ViewInject(R.id.activiyt_teacher_listview_commentlist)
    ListView mCommentList;
    @ViewInject(R.id.activity_teacher_scrollview)
    ScrollView mScrollView;
    @ViewInject(R.id.activity_teacher_linearlayout_coursenames)
    LinearLayout mLayoutCoursenames;
    @ViewInject(R.id.activity_teacher_linearlayout_checkallcomments)
    LinearLayout mButtonCheckall;
    LoadingAnimView mLoadingAnimView;
    private final Handler getTeacherInfoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FAILED:
                    String result_without_internet = (String) msg.obj;
                    Toast.makeText(TeacherActivity.this,
                            getString(R.string.internet_connection_failed), Toast.LENGTH_LONG).show();
                    break;
                default:
                    initView((Teacher) msg.obj);
                    showLoadingAnim(false);
                    break;
            }
            return false;
        }
    });
    private int teacherid;
    private Teacher teacher;

    private void initData() {
        showLoadingAnim(true);
        MyCourseApplication application = (MyCourseApplication) getApplication();
        OkHttpClient okHttpClient = application.getOkHttpClient();
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL + GET_TEACHER_URL + teacherid)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            Message msg = new Message();

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.what = FAILED;
                msg.obj = e.toString();
                getTeacherInfoHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseJson = new JSONObject(responseBody);
                        int statusCode = responseJson.getInt(MyCourseApplication.JSON_RESULT_CODE);
                        if (statusCode == MyCourseApplication.JSON_RESULT_CODE_200) {
                            //成功，处理内容
                            JSONObject resultJson = responseJson.getJSONObject(MyCourseApplication.JSON_RESULT);
                            JSONArray courseJsonList = resultJson.getJSONArray("courseList");
                            JSONArray commentJsonList = resultJson.getJSONArray("commentList");
                            teacher = new Teacher();
                            teacher.setTeacherid(resultJson.getInt("teacherId"));
                            teacher.setTeachername(resultJson.getString("teacherName"));
                            List<Course> courseList = Course.parseJsonListNoTeacher(courseJsonList);
                            List<Comment> commentList = Comment.parseJsonListNoTeacher(commentJsonList);
                            teacher.setCourseList(courseList);
                            teacher.setCommentList(commentList);
                            msg.what = SUCCESS;
                            msg.obj = teacher;
                        } else {
                            msg.what = FAILED;
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                        }
                        getTeacherInfoHandler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initView(final Teacher teacher) {
        mCommentList.setFocusable(false);
        mScrollView.scrollTo(0, 0);

        mTeachername.setText(teacher.getTeachername());

        List<Comment> comments = teacher.getCommentList();
        List<Course> courses = teacher.getCourseList();


        mButtonCheckall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this,CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(CommentActivity.BUNDLE_TYPE, CommentActivity.CommentType.TEACHER_COMMENTS);
                bundle.putInt(CommentActivity.BUNDLE_ID, teacher.getTeacherid());
                bundle.putString(CommentActivity.BUNDLE_NAME, teacher.getTeachername());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mLayoutCoursenames.removeAllViews();
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
                    intent.putExtra(CourseActivity.EXTRA_COURSE_ID, courseid);
                    startActivity(intent);
                }
            });
            layout.addView(courseName);
            layout.addView(avgrecomm);
            mLayoutCoursenames.addView(layout);
        }
        CommentsAdapter commentsAdapter = new CommentsAdapter(TeacherActivity.this,comments);
        mCommentList.setAdapter(commentsAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent = getIntent();
        teacherid = intent.getIntExtra(EXTRA_TEACHER_ID, -1);
        if (teacherid == -1) finish();
        initData();
    }

    //显示加载动画
    private void showLoadingAnim(boolean ifShow) {
        if (ifShow) {
            if (mLoadingAnimView == null) {
                mLoadingAnimView = new LoadingAnimView(this, LoadingAnimView.BgColor.LIGHT);
                mRelativeLayout.addView(mLoadingAnimView);
            }

        } else {
            mRelativeLayout.removeView(mLoadingAnimView);
            mLoadingAnimView = null;
        }
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
            return commentList.size();
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
