package com.courseratingsystem.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.view.LoadingAnimView;
import com.courseratingsystem.app.vo.Course;
import com.courseratingsystem.app.vo.User;

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

@ContentView(R.layout.activity_my_favorites)
public class MyFavoritesActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "user";
    private static final String MY_FAVORITE_URL = "/getFavoriteCourseList?userId=";
    private static final int FAILED = 0;
    private static final int SUCCESS = 1;
    LoadingAnimView mLoadingAnimView;
    @ViewInject(R.id.activity_my_favorite_layout)
    private RelativeLayout mRelativeLayout;
    @ViewInject(R.id.activity_my_favourites_nickname)
    private TextView mNickname;
    @ViewInject(R.id.activity_my_favourites_favouritesCount)
    private TextView mFavoriteCount;
    @ViewInject(R.id.activity_my_favorite_listView)
    private ListView mCourseList;
    private User user;

    //Handler 主线程创建---消息的处理主线程
    Handler getFinishedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FAILED:
                    String result_without_internet = (String)msg.obj;
                    Toast.makeText(MyFavoritesActivity.this,
                            "网络异常！请稍后再试",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    List<Course> courseList = (List<Course>) msg.obj;
                    mCourseList.setAdapter(new CourseListAdapter(MyFavoritesActivity.this, courseList));
                    mNickname.setText(user.getNickname());
                    mFavoriteCount.setText(String.valueOf(courseList.size()));
                    showLoadingAnim(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(EXTRA_USER);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("课程收藏管理");

        initData();
    }

    private void initData() {
        showLoadingAnim(true);
        MyCourseApplication application = (MyCourseApplication) getApplication();
        OkHttpClient okHttpClient = application.getOkHttpClient();
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL + MY_FAVORITE_URL + application.getUserId())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            Message msg = new Message();
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.what = FAILED;
                msg.obj = e.toString();
                getFinishedHandler.sendMessage(msg);
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

                            JSONArray courseJsonList = resultJson.getJSONArray("favoriteCourseList");
                            msg.what = SUCCESS;
                            msg.obj = Course.parseJsonListNoTeacher(courseJsonList);
                        } else {
                            msg.what = FAILED;
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                        }
                        getFinishedHandler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

    //ActionBar返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class CourseViewHolder {
        Button mAddComment;
        TextView mCourseName, mRecScore, mRecComment, mCommentCount, mOtherScores;
        LinearLayout mTeacherLayout;
    }

    private class CourseListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Course> listToShow;

        public CourseListAdapter(Context context, List<Course> courseList) {
            inflater = LayoutInflater.from(context);
            listToShow = courseList;
        }

        public List<Course> getListToShow() {
            return listToShow;
        }

        public void setListToShow(List<Course> listToShow) {
            this.listToShow = listToShow;
        }

        @Override
        public int getCount() {
            return listToShow.size();
        }

        @Override
        public Object getItem(int position) {
            return listToShow.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Course tmpCourse = listToShow.get(position);
            CourseViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new CourseViewHolder();
                convertView = inflater.inflate(R.layout.item_activity_my_favorites, null);
                viewHolder.mCourseName = (TextView) convertView.findViewById(R.id.item_activity_my_favorites_courseName);
                viewHolder.mRecScore = (TextView) convertView.findViewById(R.id.item_activity_my_favorites_recScore);
                viewHolder.mRecComment = (TextView) convertView.findViewById(R.id.item_activity_my_favorites_recComment);
                viewHolder.mCommentCount = (TextView) convertView.findViewById(R.id.item_activity_my_favorites_commentCount);
                viewHolder.mOtherScores = (TextView) convertView.findViewById(R.id.item_activity_my_favorites_otherScores);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CourseViewHolder) convertView.getTag();
            }
//            设置显示内容
            viewHolder.mCourseName.setText(tmpCourse.getCourseName());
            viewHolder.mRecScore.setText(String.format(getString(R.string.item_fragment_courselist_recScore), tmpCourse.getRecommendationScore()));
            if (tmpCourse.getRecommendationScore() >= 4) {
                viewHolder.mRecComment.setText(getString(R.string.item_fragment_courselist_recComment_4));
            } else if (tmpCourse.getRecommendationScore() >= 3) {
                viewHolder.mRecComment.setText(getString(R.string.item_fragment_courselist_recComment_3));
            } else if (tmpCourse.getRecommendationScore() >= 2) {
                viewHolder.mRecComment.setText(getString(R.string.item_fragment_courselist_recComment_2));
            } else if (tmpCourse.getRecommendationScore() >= 1) {
                viewHolder.mRecComment.setText(getString(R.string.item_fragment_courselist_recComment_1));
            } else {
                viewHolder.mRecComment.setText(getString(R.string.item_fragment_courselist_recComment_0));
            }
            viewHolder.mCommentCount.setText(String.format(getString(R.string.item_fragment_courselist_commentCount), tmpCourse.getPeopleCount()));
            viewHolder.mOtherScores.setText(String.format(getString(R.string.item_fragment_courselist_otherScores),
                    tmpCourse.getAverageRatingsUsefulness(),
                    tmpCourse.getAverageRatingsVividness(),
                    tmpCourse.getAverageRatingsSpareTimeOccupation(),
                    tmpCourse.getAverageRatingsRollCall(),
                    tmpCourse.getAverageRatingsScoring()));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyFavoritesActivity.this, CourseActivity.class);
                    intent.putExtra(CourseActivity.EXTRA_COURSE_ID, tmpCourse.getCourseId());
                    startActivity(intent);
                }
            });
            return convertView;
        }

        private class TeacherTextView extends android.support.v7.widget.AppCompatTextView {

            public TeacherTextView(Context context) {
                super(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(context.getResources().getDimensionPixelSize(R.dimen.item_fragment_courselist_marginside), 0, 0, 0);
                this.setLayoutParams(params);
                this.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.item_fragment_courselist_textsize_details));
                this.setTextColor(context.getResources().getColor(R.color.lightGrey));
            }
        }
    }

}
