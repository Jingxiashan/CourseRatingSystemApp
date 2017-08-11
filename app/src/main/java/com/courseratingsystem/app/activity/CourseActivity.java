package com.courseratingsystem.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.view.CustomizedHorizontalBarChart;
import com.courseratingsystem.app.view.ListViewNoScroll;
import com.courseratingsystem.app.view.LoadingAnimView;
import com.courseratingsystem.app.view.ObservableScrollView;
import com.courseratingsystem.app.vo.Comment;
import com.courseratingsystem.app.vo.Course;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_course)
public class CourseActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE_ID = "courseid";
    private static final String GET_COURSE_URL = "/getCourseWithTenComments?";
    private static final String ADD_FAVORITE_URL = "/addFavorateCourse?";
    private static final String DELETE_FAVORITE_URL = "/deleteFavorateCourse?";
    private static final String PARAM_USER_ID = "userId=";
    private static final String PARAM_COURSE_ID = "&courseId=";
    private static final int ADD_FAVORITE = 0;
    private static final int DELETE_FAVORITE = 1;
    private static final int FAILED = 2;
    private static final int SUCCESS = 3; //for getinfo
    private static final String ADD_FAVORITE_ACTION = "addFavoriteCourse";
    private static final String DELETE_FAVORITE_ACTION = "deleteFavoriteCourse";
    @ViewInject(R.id.activity_course_layout)
    RelativeLayout mRelativeLayout;
    @ViewInject(R.id.activity_course_horizontalbarchart)
    CustomizedHorizontalBarChart mBarChart;
    @ViewInject(R.id.activity_course_scrollview)
    ObservableScrollView mScrollView;
    @ViewInject(R.id.activity_course_textview_avgreccom_2)
    TextView mAvgRecomm;
    @ViewInject(R.id.activity_course_list_commentlist)
    ListViewNoScroll mCommentlist;
    @ViewInject(R.id.activity_course_relativelayout_header)
    RelativeLayout mHeader;
    @ViewInject(R.id.activity_course_view_headbg)
    View mViewHeadbg;
    @ViewInject(R.id.activity_course_textview_coursename)
    TextView mCourseName;
    @ViewInject(R.id.activity_course_linearlayout_teachers)
    LinearLayout mLayoutTeachers;
    @ViewInject(R.id.activity_course_linearlayout_checkallcomments)
    LinearLayout mButtonCheckall;
    @ViewInject(R.id.activity_course_linearlayout_head)
    LinearLayout mLayoutHead;
    @ViewInject(R.id.activity_course_textview_avgreccom)
    TextView mAvgrecommStatic;
    @ViewInject(R.id.activity_course_textview_commentcount)
    TextView commentcount;
    @ViewInject(R.id.activity_course_btn_addfavorite)
    Button addFavoriteBtn;
    float scale = 1.0f;
    float alpha = 1.0f;
    float ratio = 0;
    LoadingAnimView mLoadingAnimView;
    private int courseid;
    private final Handler favoriteHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_FAVORITE:
                    setFavoriteButtonStatus(DELETE_FAVORITE_ACTION);
                    Toast.makeText(CourseActivity.this, "收藏成功！", Toast.LENGTH_LONG).show();
                    break;
                case DELETE_FAVORITE:
                    setFavoriteButtonStatus(ADD_FAVORITE_ACTION);
                    Toast.makeText(CourseActivity.this, "移除收藏成功！", Toast.LENGTH_LONG).show();
                    break;
                case FAILED:
                    Toast.makeText(CourseActivity.this, "收藏失败！", Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });

    private final Handler getCourseInfoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case FAILED:
                    String result_without_internet = (String)msg.obj;
                    Toast.makeText(CourseActivity.this,
                            getString(R.string.internet_connection_failed),Toast.LENGTH_LONG).show();
                    break;
                default:
                    initView((Course) msg.obj);
                    showLoadingAnim(false);
                    break;
            }
            return false;
        }
    });
    private Course course;

    private void setFavoriteButtonStatus(String action){
        switch (action){
            case ADD_FAVORITE_ACTION:
                addFavoriteBtn.setText("我要收藏");
                addFavoriteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFavorite(ADD_FAVORITE_ACTION);
                    }
                });
                break;
            case DELETE_FAVORITE_ACTION:
                addFavoriteBtn.setText("已收藏");
                addFavoriteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFavorite(DELETE_FAVORITE_ACTION);
                    }
                });

        }
    }

    private void setFavorite(final String action){
        MyCourseApplication application = (MyCourseApplication) CourseActivity.this.getApplication();
        final int userid = application.getUserId();
        OkHttpClient client = application.getOkHttpClient();

        String action_url = null;
        switch (action){
            case ADD_FAVORITE_ACTION:
                action_url = ADD_FAVORITE_URL+ PARAM_USER_ID + userid +PARAM_COURSE_ID + courseid;
                break;
            case DELETE_FAVORITE_ACTION:
                action_url = DELETE_FAVORITE_URL+ PARAM_USER_ID + userid + PARAM_COURSE_ID + courseid;

        }
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL + action_url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            Message msg =new Message();
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.what = FAILED;
                favoriteHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.body() != null){
                    String responseBody = response.body().string();
                    try{
                        JSONObject responseJson = new JSONObject(responseBody);
                        int statusCode = responseJson.getInt(MyCourseApplication.JSON_RESULT_CODE);
                        if(statusCode == MyCourseApplication.JSON_RESULT_CODE_200){
                            switch (action){
                                case ADD_FAVORITE_ACTION:
                                    msg.what = ADD_FAVORITE;
                                    favoriteHandler.sendMessage(msg);
                                    break;
                                case DELETE_FAVORITE_ACTION:
                                    msg.what = DELETE_FAVORITE;
                                    favoriteHandler.sendMessage(msg);
                            }
                        }
                        else {
                            msg.what = FAILED;
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                            favoriteHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initData(){
        showLoadingAnim(true);
        MyCourseApplication application = (MyCourseApplication) getApplication();
        OkHttpClient okHttpClient = application.getOkHttpClient();
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL + GET_COURSE_URL
                        + PARAM_USER_ID + application.getUserId()
                        + PARAM_COURSE_ID + courseid)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            Message msg = new Message();
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.what = FAILED;
                msg.obj = e.toString();
                getCourseInfoHandler.sendMessage(msg);
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
                            Course tmpCourse = new Course(resultJson);
                            msg.what = SUCCESS;
                            msg.obj = tmpCourse;
                        } else {
                            msg.what = FAILED;
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                        }
                        getCourseInfoHandler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initView(final Course course){

        mCommentlist.setFocusable(false);
        mScrollView.scrollTo(0,0);
        setFavoriteButtonStatus(course.isIfFavorite() ? DELETE_FAVORITE_ACTION : ADD_FAVORITE_ACTION);
        mButtonCheckall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this,CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(CommentActivity.BUNDLE_TYPE, CommentActivity.CommentType.COURSE_COMMENTS);
                bundle.putInt(CommentActivity.BUNDLE_ID, course.getCourseId());
                bundle.putString(CommentActivity.BUNDLE_NAME, course.getCourseName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        commentcount.setText(String.valueOf(course.getPeopleCount()));

        LinearLayout.LayoutParams teacherParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        teacherParam.setMargins(5,5,5,5);
        mLayoutTeachers.removeAllViews();
        List<Course.TeacherBrief> teachers = course.getTeacherList();
        for (int i =0;i<teachers.size();i++){
            TextView teacher = new TextView(CourseActivity.this);
            teacher.setText(teachers.get(i).getTeacherName());
            teacher.setLayoutParams(teacherParam);
            teacher.setBackground(getResources().getDrawable(R.drawable.activity_course_teacher_bg, null));
            teacher.setPadding(10,5,10,5);
            teacher.setTextSize(15);
            final int teacherid = teachers.get(i).getTeacherId();
            teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseActivity.this,TeacherActivity.class);
                    intent.putExtra(TeacherActivity.EXTRA_TEACHER_ID, teacherid);
                    startActivity(intent);
                }
            });
            mLayoutTeachers.addView(teacher);
        }

        mBarChart.setBar_color(getResources().getColor(R.color.teacher_blue_light));
        mBarChart.setValue_color(getResources().getColor(R.color.lightGrey));
        mBarChart.setScores(course.getAverageRatingsRollCall(),course.getAverageRatingsScoring(),course.getAverageRatingsSpareTimeOccupation(),
                course.getAverageRatingsVividness(),course.getAverageRatingsUsefulness());
        mBarChart.initChart();

        mCourseName.setText(course.getCourseName());
        mAvgRecomm.setText(String.valueOf(course.getRecommendationScore()));
        mAvgrecommStatic.setText(String.valueOf(course.getRecommendationScore()));
        mAvgRecomm.setVisibility(View.GONE);

        List<Comment> comments = course.getCommentList();
        CommentsAdapter commentsAdapter = new CommentsAdapter(CourseActivity.this,comments);
        mCommentlist.setAdapter(commentsAdapter);

        mScrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
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
                    mAvgRecomm.setVisibility(View.GONE);
                }
                if(scrollView.getScrollY()>=-10){
                    scale =  (((2*(float) mHeader.getHeight())-(float)scrollView.getScrollY())/(2*(float) mHeader.getHeight()));
                    ratio = ((float)scrollView.getScrollY()/(float) mHeader.getHeight());
                    if(scale<0.5){
                        scale=0.5f;
                    }
                    else if(scale>1){
                        scale=1;
                    }
                    if(ratio >1){
                        ratio =1;
                    }
                    ViewHelper.setPivotX(mAvgRecomm, 1000);
                    ViewHelper.setPivotY(mAvgRecomm, -640);
                    ViewHelper.setScaleX(mAvgRecomm, scale);
                    ViewHelper.setScaleY(mAvgRecomm, scale);
                    ViewHelper.setAlpha(mHeader,alpha);
                    ViewHelper.setAlpha(mViewHeadbg, ratio);
                    ViewHelper.setPivotX(mCourseName, 0);
                    ViewHelper.setPivotY(mCourseName, mCourseName.getHeight()/2);
                    ViewHelper.setScaleX(mCourseName, 1-(ratio *0.2f));
                    ViewHelper.setScaleY(mCourseName, 1-(ratio *0.2f));
                    ViewHelper.setTranslationX(mCourseName,-ratio *250);
                    mLayoutHead.setElevation(ratio*5);
                }
                if(scrollView.getScrollY()>0&&scrollView.getScrollY()<= mHeader.getHeight()){
                    mAvgRecomm.setVisibility(View.VISIBLE);
                    mAvgrecommStatic.setVisibility(View.GONE);
                }
                else {

                    mAvgrecommStatic.setVisibility(View.VISIBLE);
                }


                Log.i("scrollY",((float)scrollView.getScrollY()/(2*(float) mHeader.getHeight()))+"");
            }
        });
        mViewHeadbg.setAlpha(ratio);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent = getIntent();
        courseid = intent.getIntExtra(EXTRA_COURSE_ID, -1);
        if (courseid == -1) finish();

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

    @Event(value = {R.id.activity_course_btn_addcomment}, type = View.OnClickListener.class)
    private void doEvent(View view){
        //TODO:打开评论页
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
            viewHolder.ratingBar.setNumStars((int) tmpComment.getRecstar());
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
