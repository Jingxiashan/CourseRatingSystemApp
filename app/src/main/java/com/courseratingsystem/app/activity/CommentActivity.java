package com.courseratingsystem.app.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.view.CourseListSwipeRefreshView;
import com.courseratingsystem.app.vo.Comment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


@ContentView(R.layout.activity_comment)
public class CommentActivity extends AppCompatActivity {
    public static final String COURSE_INFO = "course_info";
    public static final String BUNDLE_TYPE = "type";
    public static final String BUNDLE_ID = "id";
    public static final String BUNDLE_NAME = "name";
    private static final String COURSE_COMMENT_URL = "/getCommentListByCourseId?courseId=";
    private static final String COMMENT_LIKE_URL = "/addLikeCount?commentId=";
    private static final String TEACHER_COMMENT_URL = "/getCommentListByTeacherId?teacherId=";
    private static final String PARAM_CURRENT_PAGE = "&currentPage=";
    private static final int FAILED = -1;
    private static final int LOAD_RESET = 1;
    private static final int LOAD_APPEND = 2;
    private static int TYPE_REFRESH = 0;
    private static int TYPE_LOADMORE = 1;
    AnimatorSet hideElementAnimatorSet;//这是隐藏头尾元素使用的动画
    AnimatorSet showElementAnimatorSet;//这是显示头尾元素使用的动画
    @ViewInject(R.id.activity_comment_layout)
    private RelativeLayout mRelativeLayout;
    @ViewInject(R.id.activity_comment_layout_courseheaderinfo)
    private LinearLayout mCourseHeaderInfo;
    @ViewInject(R.id.activity_comment_listview_allcomments)
    private ListView mAllCommentsList;
    @ViewInject(R.id.activity_comment_text_courseName)
    private TextView mCourseName;
    @ViewInject(R.id.activity_comment_linear_teacherlayout)
    private LinearLayout mTeacherLayout;
    @ViewInject(R.id.activity_commentlist_refresh)
    private CourseListSwipeRefreshView mCommentSwipeRefresh;
    private View mFooterView;
    private CommentsAdapter commentsAdapter;
    private final Handler getCommentHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_RESET:
                    List<Comment> commentList = (List<Comment>) msg.obj;
                    commentsAdapter = new CommentsAdapter(CommentActivity.this, commentList);
                    mAllCommentsList.setAdapter(commentsAdapter);
                    break;
                case LOAD_APPEND:
                    List<Comment> newCommentList = (List<Comment>) msg.obj;
                    List<Comment> oldCommentList = commentsAdapter.getCommentList();
                    oldCommentList.addAll(newCommentList);
                    commentsAdapter.notifyDataSetChanged();
                    break;
                case FAILED:
                    break;
            }
            mCommentSwipeRefresh.setRefreshing(false);
            return false;
        }
    });
    private int totalPage;
    private int currentMaxLoadedPage;
    private CommentType commentType;
    private int id;
    private String name;
    ;

    private void initData() {
        currentMaxLoadedPage = 0;
        loadData(1);
    }

    private void loadMore() {
        if (currentMaxLoadedPage < totalPage) {
            if (mAllCommentsList.getFooterViewsCount() > 1) {
                mAllCommentsList.removeFooterView(mFooterView);
            }
            loadData(currentMaxLoadedPage + 1);
        } else {
            //已经到达最后一页
            mCommentSwipeRefresh.setLoadingMore(false);
            if (mAllCommentsList.getFooterViewsCount() == 0) {
                //添加一个已经到最低 的 footerview
                mAllCommentsList.addFooterView(mFooterView);
            }
        }
    }

    private void loadData(final int page) {
        mCommentSwipeRefresh.setRefreshing(true);
        String url = MyCourseApplication.SERVER_URL;
        switch (commentType) {
            case COURSE_COMMENTS:
                url += COURSE_COMMENT_URL + id;
                break;
            case TEACHER_COMMENTS:
                url += TEACHER_COMMENT_URL + id;
                break;
        }
        url += PARAM_CURRENT_PAGE + page;
        MyCourseApplication application = (MyCourseApplication) getApplication();
        OkHttpClient okHttpClient = application.getOkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            Message msg = new Message();

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.what = FAILED;
                msg.obj = e.toString();
                getCommentHandler.sendMessage(msg);
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

                            totalPage = resultJson.getInt("totalPage");
                            currentMaxLoadedPage = resultJson.getInt("currentPage");
                            JSONArray commentJsonList = resultJson.getJSONArray("commentList");
                            msg.obj = Comment.parseJsonList(commentJsonList);
                            if (page == 1) {
                                //如果加载了第一页，就重设整个listview的list
                                msg.what = LOAD_RESET;
                            } else {
                                //如果加载了其他页，就在原来的list基础上追加新的list
                                msg.what = LOAD_APPEND;
                            }
                        } else {
                            msg.what = FAILED;
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                        }
                        getCommentHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initView() {
        mFooterView = View.inflate(this, R.layout.footer_fragment_discover_hot_courses_list, null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        View headerView = new View(this);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mCourseHeaderInfo.measure(w, h);
        headerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) mCourseHeaderInfo.getMeasuredHeight()));
        headerView.setBackgroundColor(Color.parseColor("#00000000"));
        mAllCommentsList.addHeaderView(headerView);


        mCourseName.setText(name);


        actionBar.setTitle("全部评论");

        //下拉刷新
        mCommentSwipeRefresh.setProgressViewOffset(true, 0, getResources().getDimensionPixelOffset(R.dimen.activity_index_search_height));
        mCommentSwipeRefresh.setColorSchemeColors(Color.GRAY, Color.BLACK, getResources()
                .getColor(R.color.teacher_blue_light));
        mCommentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        mCommentSwipeRefresh.setOnLoadMoreListener(new CourseListSwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        mCommentSwipeRefresh.setOnSwipeListener(new CourseListSwipeRefreshView.OnSwipeListener() {
            @Override
            public void onSwipe(CourseListSwipeRefreshView.SwipeDirect swipeDirect) {
                switch (swipeDirect) {
                    case UP:
                        startHiddingToolbar();
                        break;
                    case DOWN:
                        startShowingToolbar();
                        break;
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) finish();
        commentType = (CommentType) bundle.get(BUNDLE_TYPE);
        id = bundle.getInt(BUNDLE_ID);
        name = bundle.getString(BUNDLE_NAME);
        initData();
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startHiddingToolbar() {
//先清除其他动画
        if (showElementAnimatorSet != null && showElementAnimatorSet.isRunning()) {
            showElementAnimatorSet.cancel();
        }
        if (hideElementAnimatorSet != null && hideElementAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            hideElementAnimatorSet = new AnimatorSet();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(mCourseHeaderInfo, "translationY", mCourseHeaderInfo.getTranslationY(), -mCourseHeaderInfo.getHeight());//将ToolBar隐藏到上面
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            hideElementAnimatorSet.setDuration(200);
            hideElementAnimatorSet.playTogether(animators);
            hideElementAnimatorSet.start();
        }
    }

    public void startShowingToolbar() {
        //先清除其他动画
        if (hideElementAnimatorSet != null && hideElementAnimatorSet.isRunning()) {
            hideElementAnimatorSet.cancel();
        }
        if (showElementAnimatorSet != null && showElementAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            showElementAnimatorSet = new AnimatorSet();
            //下面两句是将头尾元素放回初始位置。
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(mCourseHeaderInfo, "translationY", mCourseHeaderInfo.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            showElementAnimatorSet.setDuration(300);
            showElementAnimatorSet.playTogether(animators);
            showElementAnimatorSet.start();
        }
    }

    public static enum CommentType {COURSE_COMMENTS, TEACHER_COMMENTS}

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
            final CommentsViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new CommentsViewHolder();
                convertView = inflater.inflate(R.layout.item_activity_comment_list, null);
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
                viewHolder = (CommentsViewHolder) convertView.getTag();
            }
            //设置头像显示
            viewHolder.ratingBar.setNumStars((int) tmpComment.getRecstar());
            viewHolder.nickName.setText(tmpComment.getNickname());
            viewHolder.timeStamp.setText(tmpComment.getTimestamp());
            viewHolder.commentContent.setText(tmpComment.getContent());
            viewHolder.likeCount.setText("" + tmpComment.getLikecount());

            viewHolder.showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommentActivity.this, CommentPopupActivity.class);
                    intent.putExtra(CommentPopupActivity.COMMENT_KEY, tmpComment);
                    startActivity(intent);
                }
            });

            viewHolder.clickLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:点赞
                    clickList(viewHolder.likeCount, tmpComment.getCommentid());
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
    private final Handler likeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case FAILED:
                    break;
                default:
                    TextView likeCount = (TextView) msg.obj;
                    likeCount.setText(String.valueOf(msg.what));
                    break;
            }
            return false;
        }
    });
    private void clickList(final TextView view, int commentId){
        MyCourseApplication application = (MyCourseApplication) getApplication();
        OkHttpClient okHttpClient = application.getOkHttpClient();
        Request request = new Request.Builder()
                .url(COMMENT_LIKE_URL + commentId)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            Message msg = new Message();

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.what = FAILED;
                msg.obj = e.toString();
                likeHandler.sendMessage(msg);
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
                            msg.what = resultJson.getInt("likeCount");
                            msg.obj = view;
                        } else {
                            msg.what = FAILED;
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                        }
                        likeHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}

