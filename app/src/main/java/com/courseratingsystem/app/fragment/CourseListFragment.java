package com.courseratingsystem.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.activity.IndexActivity;
import com.courseratingsystem.app.view.CourseListSwipeRefreshView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_course_list)
public class CourseListFragment extends Fragment {

    private static int TYPE_REFRESH = 0;
    private static int TYPE_LOADMORE = 1;
    private static Handler loadDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //TODO：根据sortType和msg.what加载新数据
            if (msg.what == TYPE_REFRESH) {
                Log.e("LISTSCROLL", "刷新");
            } else if (msg.what == TYPE_LOADMORE) {
                Log.e("LISTSCROLL", "加载新数据");

            }
        }
    };
    ;
    @ViewInject(R.id.fragment_courselist_refresh)
    CourseListSwipeRefreshView mSwipeRefresh;
    @ViewInject(R.id.fragment_courselist_list_course)
    ListView mCourseList;

    private SortType sortType;

    public CourseListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = x.view().inject(this, inflater, container);
        initData();
        initView();
        List<CourseInList> courseList = new ArrayList<>();
        CourseInList tmpCourse;
        for (int i = 0; i < 20; i++) {
            tmpCourse = new CourseInList();
            tmpCourse.setCourseName("泥人张技艺教授与传承");
            tmpCourse.setAverageRatingsRollCall(3.6f);
            tmpCourse.setAverageRatingsScoring(3.7f);
            tmpCourse.setAverageRatingsSpareTimeOccupation(3.8f);
            tmpCourse.setAverageRatingsUsefulness(3.9f);
            tmpCourse.setAverageRatingsVividness(4.0f);
            tmpCourse.setRecommendationScore(4.9f);
            tmpCourse.setPeopleCount(1999);
            tmpCourse.setTeacherList(new ArrayList<CourseInList.TeacherBrief>());
            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("张彦泽", 1));
            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("黄嘉星", 2));
            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("鲁迪", 3));
            courseList.add(tmpCourse);
        }
        mCourseList.setAdapter(new CourseListAdapter(CourseListFragment.this.getActivity(), courseList));
        return fragView;
    }

    private void initView() {
        View headerView = new View(getActivity());
        headerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.activity_index_search_height)));
        headerView.setBackgroundColor(Color.parseColor("#00000000"));
        mCourseList.addHeaderView(headerView);
        mCourseList.setDividerHeight(0);
        mSwipeRefresh.setColorSchemeColors(Color.CYAN, Color.BLUE, Color.GREEN, Color.RED);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message msg = new Message();
                msg.what = TYPE_REFRESH;
                loadDataHandler.sendMessage(msg);
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
                //TODO:加载更多
            }
        });
    }

    private void initData() {
        sortType = SortType.REC_SCORE;
    }

    private enum SortType {REC_SCORE, USEFUL_SCORE, VIVID_SCORE, OCUPY_SCORE, SCORE_SCORE, ROLLCALL_SCORE}

    private static class CourseViewHolder {
        Button mAddComment;
        TextView mCourseName, mRecScore, mRecComment, mCommentCount, mOtherScores;
        LinearLayout mTeacherLayout;
    }

    private class CourseListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<CourseInList> listToShow;

        public CourseListAdapter(Context context, List<CourseInList> courseList) {
            inflater = LayoutInflater.from(context);
            listToShow = courseList;
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
            CourseInList tmpCourse = listToShow.get(position);
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
            for (CourseInList.TeacherBrief tmpTeacher : tmpCourse.getTeacherList()) {
                TeacherTextView teacherTextView = new TeacherTextView(CourseListFragment.this.getActivity());
                teacherTextView.setText(tmpTeacher.getTeacherName());
                teacherTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO:打开对应Teacher的Activity
                    }
                });
                viewHolder.mTeacherLayout.addView(teacherTextView);
            }
            viewHolder.mAddComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:打开发表评论的Activity
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

    private class CourseInList {
        private int courseId;
        private String courseName;
        private float recommendationScore, averageRatingsUsefulness, averageRatingsVividness, averageRatingsSpareTimeOccupation, averageRatingsScoring, averageRatingsRollCall;
        private int peopleCount;
        private List<TeacherBrief> teacherList;

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public float getRecommendationScore() {
            return recommendationScore;
        }

        public void setRecommendationScore(float recommendationScore) {
            this.recommendationScore = recommendationScore;
        }

        public float getAverageRatingsUsefulness() {
            return averageRatingsUsefulness;
        }

        public void setAverageRatingsUsefulness(float averageRatingsUsefulness) {
            this.averageRatingsUsefulness = averageRatingsUsefulness;
        }

        public float getAverageRatingsVividness() {
            return averageRatingsVividness;
        }

        public void setAverageRatingsVividness(float averageRatingsVividness) {
            this.averageRatingsVividness = averageRatingsVividness;
        }

        public float getAverageRatingsSpareTimeOccupation() {
            return averageRatingsSpareTimeOccupation;
        }

        public void setAverageRatingsSpareTimeOccupation(float averageRatingsSpareTimeOccupation) {
            this.averageRatingsSpareTimeOccupation = averageRatingsSpareTimeOccupation;
        }

        public float getAverageRatingsScoring() {
            return averageRatingsScoring;
        }

        public void setAverageRatingsScoring(float averageRatingsScoring) {
            this.averageRatingsScoring = averageRatingsScoring;
        }

        public float getAverageRatingsRollCall() {
            return averageRatingsRollCall;
        }

        public void setAverageRatingsRollCall(float averageRatingsRollCall) {
            this.averageRatingsRollCall = averageRatingsRollCall;
        }

        public int getPeopleCount() {
            return peopleCount;
        }

        public void setPeopleCount(int peopleCount) {
            this.peopleCount = peopleCount;
        }

        public List<TeacherBrief> getTeacherList() {
            return teacherList;
        }

        public void setTeacherList(List<TeacherBrief> teacherList) {
            this.teacherList = teacherList;
        }

        class TeacherBrief {
            String teacherName;
            int teacherid;

            public TeacherBrief(String teacherName, int teacherid) {
                this.teacherName = teacherName;
                this.teacherid = teacherid;
            }

            public String getTeacherName() {
                return teacherName;
            }

            public void setTeacherName(String teacherName) {
                this.teacherName = teacherName;
            }

            public int getTeacherid() {
                return teacherid;
            }

            public void setTeacherid(int teacherid) {
                this.teacherid = teacherid;
            }
        }
    }

}
