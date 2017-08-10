package com.courseratingsystem.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.activity.AddCommentActivity;
import com.courseratingsystem.app.activity.CourseActivity;
import com.courseratingsystem.app.activity.IndexActivity;
import com.courseratingsystem.app.activity.TeacherActivity;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.view.CourseListSwipeRefreshView;
import com.courseratingsystem.app.vo.Course;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
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

@ContentView(R.layout.fragment_course_list)
public class CourseListFragment extends Fragment {

    private static final int REFRESH_DATA_RESET = 0;
    private static final int REFRESH_DATA_APPEND = 1;
    @ViewInject(R.id.fragment_courselist_refresh)
    CourseListSwipeRefreshView mSwipeRefresh;
    private final Handler refreshFailedHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_failed), Toast.LENGTH_LONG).show();
            mSwipeRefresh.setRefreshing(false);
            mSwipeRefresh.setLoadingMore(false);
            return false;
        }
    });
    @ViewInject(R.id.fragment_courselist_list_course)
    ListView mCourseList;
    @ViewInject(R.id.fragment_courselist_floating_menu)
    FloatingActionsMenu floatingActionsMenu;
    @ViewInject(R.id.fragment_courselist_floating_child_rec)
    FloatingActionButton floatingRec;
    @ViewInject(R.id.fragment_courselist_floating_child_use)
    FloatingActionButton floatingUse;
    @ViewInject(R.id.fragment_courselist_floating_child_viv)
    FloatingActionButton floatingViv;
    @ViewInject(R.id.fragment_courselist_floating_child_ocu)
    FloatingActionButton floatingOcu;
    @ViewInject(R.id.fragment_courselist_floating_child_sco)
    FloatingActionButton floatingSco;
    @ViewInject(R.id.fragment_courselist_floating_child_rol)
    FloatingActionButton floatingRol;
    @ViewInject(R.id.fragment_courselist_floating_toTop)
    private FloatingActionButton buttonToTop;
    private CourseListSession listSession;
    private CourseListAdapter listAdapter;
    private final Handler refreshDataHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            List<Course> newCourseList = (ArrayList<Course>) msg.obj;
            switch (msg.what) {
                case REFRESH_DATA_RESET:
                    listAdapter.setListToShow(newCourseList);
                    listAdapter.notifyDataSetChanged();
                    returnToTop();
                    break;
                case REFRESH_DATA_APPEND:
                    List<Course> listToShow = listAdapter.getListToShow();
                    listToShow.addAll(newCourseList);
                    listAdapter.setListToShow(listToShow);
                    listAdapter.notifyDataSetChanged();
                    break;
            }
            mSwipeRefresh.setRefreshing(false);
            mSwipeRefresh.setLoadingMore(false);
            return false;
        }
    });
    private String defaultSearchText;
    private Integer defaultSearchType;

    public CourseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = x.view().inject(this, inflater, container);
        initData();
        initView();
        return fragView;
    }

    private void initView() {
        View headerView = new View(getActivity());
        headerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.activity_index_search_height)));
        headerView.setBackgroundColor(Color.parseColor("#00000000"));
        mCourseList.addHeaderView(headerView);
        mCourseList.setDividerHeight(0);
        mSwipeRefresh.setProgressViewOffset(true, 0, getResources().getDimensionPixelOffset(R.dimen.activity_index_search_height));
        mSwipeRefresh.setColorSchemeColors(Color.CYAN, Color.BLUE, Color.GREEN, Color.RED);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listSession.refresh();
            }
        });
        mSwipeRefresh.setOnSwipeListener(new CourseListSwipeRefreshView.OnSwipeListener() {
            @Override
            public void onSwipe(CourseListSwipeRefreshView.SwipeDirect swipeDirect) {
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
        mSwipeRefresh.setOnLoadMoreListener(new CourseListSwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                listSession.loadMore();
            }
        });
        mCourseList.setAdapter(listAdapter);
    }

    private void initData() {
        listAdapter = new CourseListAdapter(getActivity(), new ArrayList<Course>());
        listSession = new CourseListSession(null, null, null);
    }

    @Event(value = {R.id.fragment_courselist_floating_child_rec,
            R.id.fragment_courselist_floating_child_use,
            R.id.fragment_courselist_floating_child_viv,
            R.id.fragment_courselist_floating_child_ocu,
            R.id.fragment_courselist_floating_child_sco,
            R.id.fragment_courselist_floating_child_rol}, type = View.OnClickListener.class)
    private void onFloatingMenuClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_courselist_floating_child_rec:
                listSession.setSortBy(CourseListSession.SORT_BY_REC);
                break;
            case R.id.fragment_courselist_floating_child_use:
                listSession.setSortBy(CourseListSession.SORT_BY_USE);
                break;
            case R.id.fragment_courselist_floating_child_viv:
                listSession.setSortBy(CourseListSession.SORT_BY_VIV);
                break;
            case R.id.fragment_courselist_floating_child_ocu:
                listSession.setSortBy(CourseListSession.SORT_BY_OCU);
                break;
            case R.id.fragment_courselist_floating_child_sco:
                listSession.setSortBy(CourseListSession.SORT_BY_SCO);
                break;
            case R.id.fragment_courselist_floating_child_rol:
                listSession.setSortBy(CourseListSession.SORT_BY_ROL);
                break;
        }
        floatingActionsMenu.collapse();
    }

    @Event(value = R.id.fragment_courselist_floating_toTop, type = View.OnClickListener.class)
    private void onToTopClicked(View view) {
        returnToTop();
    }

    public void returnToTop() {
        mCourseList.smoothScrollToPosition(0);
    }

    public void search(String searchText, int searchType) {
        listSession = new CourseListSession(searchText, searchType, null);
    }

    private static class CourseViewHolder {
        Button mAddComment;
        TextView mCourseName, mRecScore, mRecComment, mCommentCount, mOtherScores;
        LinearLayout mTeacherLayout;
    }

    public class CourseListSession {
        public static final int SEARCH_BY_COURSE = 0;
        public static final int SEARCH_BY_TEACHER = 1;
        static final int TYPE_REFRESH = 0;
        static final int TYPE_LOADMORE = 1;
        static final String SORT_BY_USE = "averageRatingsUsefulness";
        static final String SORT_BY_VIV = "averageRatingsVividness";
        static final String SORT_BY_OCU = "averageRatingsSpareTimeOccupation";
        static final String SORT_BY_SCO = "averageRatingsScoring";
        static final String SORT_BY_ROL = "averageRatingsRollCall";
        static final String SORT_BY_REC = "recommendationScore";
        private static final String GET_COURSE_LIST_URL = "/getCourseList";
        private static final String URL_PARAM_CURRENT_PAGE = "?currentPage=";
        private static final String URL_PARAM_SEARCH_TYPE = "&searchType=";
        private static final String URL_PARAM_SEARCH_TEXT = "&searchText=";
        private static final String URL_PARAM_SORT_BY = "&sortBy=";
        private String sortBy;
        private String searchText;
        private int searchType;
        private int totalPage;
        private int currentMaxLoadedPage;
        private OkHttpClient client;

        CourseListSession(@Nullable String searchText, @Nullable Integer searchType, @Nullable String sortBy) {
            client = ((MyCourseApplication) getActivity().getApplication()).getOkHttpClient();
            this.sortBy = sortBy;
            this.searchText = searchText;
            if (searchType == null) this.searchType = SEARCH_BY_COURSE;
            else this.searchType = searchType;
            currentMaxLoadedPage = 0;
            loadData(1);
        }

        void setCurrentMaxLoadedPage(int currentMaxLoadedPage) {
            this.currentMaxLoadedPage = currentMaxLoadedPage;
        }

        void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        void setSortBy(String sortBy) {
            this.sortBy = sortBy;
            loadData(1);
        }

        void loadMore() {
            if (currentMaxLoadedPage < totalPage) {
                loadData(currentMaxLoadedPage + 1);
            } else {
                //已经到达最后一页
                mSwipeRefresh.setLoadingMore(false);
                if (mCourseList.getFooterViewsCount() == 0) {
                    //添加一个已经到最低 的 footerview
                    mCourseList.addFooterView(View.inflate(getActivity(), R.layout.footer_fragment_discover_hot_courses_list, null));
                }
            }
        }

        void refresh() {
            loadData(1);
        }

        private void loadData(final int pageNumToLoad) {
            mSwipeRefresh.setRefreshing(true);
            String url = MyCourseApplication.SERVER_URL + GET_COURSE_LIST_URL + URL_PARAM_CURRENT_PAGE + pageNumToLoad;
            //可选择的参数
            if (sortBy != null) url += URL_PARAM_SORT_BY + sortBy;
            if (searchText != null)
                url += URL_PARAM_SEARCH_TYPE + searchType + URL_PARAM_SEARCH_TEXT + searchText;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {

                Message msg = new Message();

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    msg.obj = getString(R.string.internet_connection_failed);
                    refreshFailedHandler.sendMessage(msg);
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
                                setTotalPage(resultJson.getInt("totalPage"));
                                setCurrentMaxLoadedPage(resultJson.getInt("currentPage"));
                                JSONArray courseJsonList = resultJson.getJSONArray("courseList");
                                msg.obj = Course.parseJsonList(courseJsonList);
                                if (pageNumToLoad == 1) {
                                    //如果加载了第一页，就重设整个listview的list
                                    msg.what = REFRESH_DATA_RESET;
                                } else {
                                    //如果加载了其他页，就在原来的list基础上追加新的list
                                    msg.what = REFRESH_DATA_APPEND;
                                }
                                refreshDataHandler.sendMessage(msg);
                            } else {
                                msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                                refreshFailedHandler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
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
                convertView = inflater.inflate(R.layout.item_fragment_courselist_course, null);
                viewHolder.mAddComment = (Button) convertView.findViewById(R.id.item_fragment_courseList_button_comment);
                viewHolder.mCourseName = (TextView) convertView.findViewById(R.id.item_fragment_courseList_text_courseName);
                viewHolder.mRecScore = (TextView) convertView.findViewById(R.id.item_fragment_courseList_text_recScore);
                viewHolder.mRecComment = (TextView) convertView.findViewById(R.id.item_fragment_courseList_text_recComment);
                viewHolder.mCommentCount = (TextView) convertView.findViewById(R.id.item_fragment_courseList_text_commentCount);
                viewHolder.mOtherScores = (TextView) convertView.findViewById(R.id.item_fragment_courseList_text_otherScores);
                viewHolder.mTeacherLayout = (LinearLayout) convertView.findViewById(R.id.item_fragment_courseList_layout_teacherLinear);
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
            viewHolder.mTeacherLayout.removeAllViews();
            for (final Course.TeacherBrief tmpTeacher : tmpCourse.getTeacherList()) {
                TeacherTextView teacherTextView = new TeacherTextView(CourseListFragment.this.getActivity());
                teacherTextView.setText(tmpTeacher.getTeacherName());
                teacherTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TeacherActivity.class);
                        intent.putExtra(TeacherActivity.EXTRA_TEACHER_ID, tmpTeacher.getTeacherId());
                        startActivity(intent);
                    }
                });
                viewHolder.mTeacherLayout.addView(teacherTextView);
            }
            viewHolder.mAddComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddCommentActivity.class);
                    intent.putExtra(AddCommentActivity.COURSE_INFO, tmpCourse);
                    startActivity(intent);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CourseActivity.class);
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
