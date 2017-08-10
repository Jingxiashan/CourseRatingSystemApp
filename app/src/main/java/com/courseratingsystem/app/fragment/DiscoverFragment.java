package com.courseratingsystem.app.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.activity.CommentPopupActivity;
import com.courseratingsystem.app.activity.CourseActivity;
import com.courseratingsystem.app.activity.IndexActivity;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.view.DiscoverScrollView;
import com.courseratingsystem.app.view.ListViewNoScroll;
import com.courseratingsystem.app.view.LoadingAnimView;
import com.courseratingsystem.app.vo.Comment;
import com.courseratingsystem.app.vo.Course;
import com.stone.pile.libs.PileLayout;

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

import static com.courseratingsystem.app.activity.CommentPopupActivity.COMMENT_KEY;

@ContentView(R.layout.fragment_discover)
public class DiscoverFragment extends Fragment {

    //网络相关
    private static final int LOAD_SUCCESSFULLY = 0;
    private static final int LOAD_FAILED = 1;
    private final String GET_HOTS_URL = "/getHotCourseAndComment";
    LoadingAnimView mLoadingAnimView;
    @ViewInject(R.id.fragment_discover_layout)
    RelativeLayout mRelativeLayout;
    @ViewInject(R.id.fragment_discover_scroll)
    DiscoverScrollView mScrollView;
    @ViewInject(R.id.fragment_discover_layout_pile)
    PileLayout mPileLayout;
    @ViewInject(R.id.fragment_discover_hot_comments_list)
    ListViewNoScroll mCommentList;
    HotCoursesAdapter coursesAdapter;
    HotCommentsAdapter commentsAdapter;
    private List<Course> hotCoursesList;
    private List<Comment> hotCommentsList;
    private final Handler getInfoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_SUCCESSFULLY:
                    coursesAdapter = new HotCoursesAdapter(hotCoursesList);
                    commentsAdapter = new HotCommentsAdapter(getActivity(), hotCommentsList);
                    mPileLayout.setAdapter(coursesAdapter);
                    mCommentList.setAdapter(commentsAdapter);
                    showLoadingAnim(false);
                    break;
                case LOAD_FAILED:
                    //TODO:加载失败，显示加载失败的界面
                    break;
            }
            return false;
        }
    });

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = x.view().inject(this, inflater, container);
        initData();
        initView();
        return myView;
    }

    private void initData() {
        //TEST
//        hotCoursesList = new ArrayList<>();
//        Course tmpCourse;
//        for (int i = 0; i < 20; i++) {
//            tmpCourse = new Course();
//            tmpCourse.setCourseName("泥人张技艺教授与传承");
//            tmpCourse.setAverageRatingsRollCall(3.6f);
//            tmpCourse.setAverageRatingsScoring(3.7f);
//            tmpCourse.setAverageRatingsSpareTimeOccupation(3.8f);
//            tmpCourse.setAverageRatingsUsefulness(3.9f);
//            tmpCourse.setAverageRatingsVividness(4.0f);
//            tmpCourse.setRecommendationScore(4.9f);
//            tmpCourse.setPeopleCount(1999);
//            tmpCourse.setTeacherList(new ArrayList<Course.TeacherBrief>());
//            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("张彦泽", 1));
//            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("黄嘉星", 2));
//            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("鲁迪", 3));
//            hotCoursesList.add(tmpCourse);
//        }
//        hotCommentsList = new ArrayList<>();
//        Comment tmpComment;
//        for (int i = 0; i < 20; i++) {
//            tmpComment = new Comment(4, i, i, "asdsadf" + i, "asddsa", "哈哈哈这是第几个评论你哈哈哈这是第几个评论你哈哈哈这是第几个评论你哈哈哈这是第几个评论你", "啥子课哦", i + 1221);
//            hotCommentsList.add(tmpComment);
//        }
        //显示加载
        showLoadingAnim(true);
        //联网
        MyCourseApplication application = (MyCourseApplication) getActivity().getApplication();
        OkHttpClient client = application.getOkHttpClient();
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL + GET_HOTS_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            Message msg = new Message();

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.what = LOAD_FAILED;
                getInfoHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseJson = new JSONObject(responseBody);
                        int statusCode = responseJson.getInt(MyCourseApplication.JSON_RESULT_CODE);
                        if (statusCode == MyCourseApplication.JSON_RESULT_CODE_200) {
                            //成功
                            JSONObject resultJson = responseJson.getJSONObject(MyCourseApplication.JSON_RESULT);
                            JSONArray courseJsonList = resultJson.getJSONArray("courseList");
                            JSONArray commentJsonList = resultJson.getJSONArray("commentList");
                            hotCoursesList = Course.parseJsonList(courseJsonList);
                            hotCommentsList = Comment.parseJsonList(commentJsonList);

                            msg.what = LOAD_SUCCESSFULLY;
                            getInfoHandler.sendMessage(msg);
                        } else {
                            msg.what = LOAD_FAILED;
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                            getInfoHandler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initView() {
        View mFooterView = View.inflate(getActivity(), R.layout.footer_fragment_discover_hot_courses_list, null);
        mCommentList.addFooterView(mFooterView);
        mCommentList.setFocusable(false);
        mScrollView.setOnSwipeListener(new DiscoverScrollView.OnSwipeListener() {
            @Override
            public void onSwipe(DiscoverScrollView.SwipeDirect swipeDirect) {
                IndexActivity indexActivity = (IndexActivity) getActivity();
                switch (swipeDirect) {
                    case UP:
                        indexActivity.startHiddingToolbar();
                        break;
                    case DOWN:
                        indexActivity.startShowingToolbar();
                        break;
                }
            }
        });
    }

    //显示加载动画
    private void showLoadingAnim(boolean ifShow) {
        if (ifShow) {
            if (mLoadingAnimView == null) {
                mLoadingAnimView = new LoadingAnimView(getActivity(), LoadingAnimView.BgColor.LIGHT);
                mRelativeLayout.addView(mLoadingAnimView);
            }

        } else {
            mRelativeLayout.removeView(mLoadingAnimView);
            mLoadingAnimView = null;
        }
    }

    private static class CommentViewHolder {
        ImageView avatar;
        RatingBar ratingBar;
        TextView nickName, timeStamp, commentContent, likeCount, courseName;
        ImageButton showDetail, clickLike;
    }

    private static class CourseViewHolder {
        TextView coursename;
        RatingBar ratings;
        TextView peopleCount;
        Button details;
    }

    private class HotCoursesAdapter extends PileLayout.Adapter {
        List<Course> courseList;

        public HotCoursesAdapter(List<Course> courseList) {
            this.courseList = courseList;
        }

        public List<Course> getCourseList() {
            return courseList;
        }

        public void setCourseList(List<Course> courseList) {
            this.courseList = courseList;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_fragment_discover_hot_course;
        }

        @Override
        public void bindView(View convertView, int index) {
            final Course tmpCourse = courseList.get(index);
            super.bindView(convertView, index);
            CourseViewHolder viewHolder = (CourseViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new CourseViewHolder();
                viewHolder.coursename = (TextView) convertView.findViewById(R.id.item_fragment_discover_hot_course_text_courseName);
                viewHolder.ratings = (RatingBar) convertView.findViewById(R.id.item_fragment_discover_hot_course_rating_recScore);
                viewHolder.peopleCount = (TextView) convertView.findViewById(R.id.item_fragment_discover_hot_course_text_peopleCount);
                viewHolder.details = (Button) convertView.findViewById(R.id.item_fragment_discover_hot_course_button_detail);
                convertView.setTag(viewHolder);
            }
            viewHolder.coursename.setText(tmpCourse.getCourseName().length() > 7 ? tmpCourse.getCourseName().substring(0, 5) + "..." : tmpCourse.getCourseName());
            viewHolder.ratings.setNumStars((int) tmpCourse.getRecommendationScore());
            viewHolder.peopleCount.setText(String.format(getString(R.string.item_fragment_discover_peopleCount), tmpCourse.getPeopleCount()));
            viewHolder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CourseActivity.class);
                    intent.putExtra(CourseActivity.EXTRA_COURSE_ID, tmpCourse.getCourseId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }

        @Override
        public void displaying(int position) {
            super.displaying(position);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
        }
    }

    private class HotCommentsAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Comment> commentList;

        public HotCommentsAdapter(Context context, List<Comment> commentList) {
            inflater = LayoutInflater.from(context);
            this.commentList = commentList;
        }

        public List<Comment> getCommentList() {
            return commentList;
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
            CommentViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new CommentViewHolder();
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
                viewHolder = (CommentViewHolder) convertView.getTag();
            }
            //设置头像显示
            viewHolder.ratingBar.setNumStars((int) tmpComment.getRecstar());
            viewHolder.nickName.setText(tmpComment.getNickname());
            viewHolder.timeStamp.setText(tmpComment.getTimestamp());
            viewHolder.commentContent.setText(tmpComment.getContent());
            viewHolder.likeCount.setText(String.valueOf(tmpComment.getLikecount()));
            viewHolder.courseName.setText(tmpComment.getCoursename());
            viewHolder.showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DiscoverFragment.this.getActivity(), CommentPopupActivity.class);
                    intent.putExtra(COMMENT_KEY, tmpComment);
                    startActivity(intent);
                }
            });

            viewHolder.clickLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:点赞
                }
            });
            viewHolder.courseName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CourseActivity.class);
                    intent.putExtra(CourseActivity.EXTRA_COURSE_ID, tmpComment.getCourseid());
                    startActivity(intent);
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
