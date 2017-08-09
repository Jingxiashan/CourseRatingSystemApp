package com.courseratingsystem.app.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.activity.CommentPopupActivity;
import com.courseratingsystem.app.view.ListViewNoScroll;
import com.courseratingsystem.app.vo.Comment;
import com.courseratingsystem.app.vo.Course;
import com.stone.pile.libs.PileLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.courseratingsystem.app.activity.CommentPopupActivity.COMMENT_KEY;

@ContentView(R.layout.fragment_discover)
public class DiscoverFragment extends Fragment {

    @ViewInject(R.id.fragment_discover_layout)
    RelativeLayout mRelativeLayout;
    @ViewInject(R.id.fragment_discover_scroll)
    ScrollView mScrollView;
    @ViewInject(R.id.fragment_discover_layout_pile)
    PileLayout mPileLayout;
    @ViewInject(R.id.fragment_discover_hot_comments_list)
    ListViewNoScroll mCommentList;
    HotCoursesAdapter coursesAdapter;
    HotCommentsAdapter commentsAdapter;
    private List<Course> hotCoursesList;
    private List<Comment> hotCommentsList;

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
        hotCoursesList = new ArrayList<>();
        Course tmpCourse;
        for (int i = 0; i < 20; i++) {
            tmpCourse = new Course();
            tmpCourse.setCourseName("泥人张技艺教授与传承");
            tmpCourse.setAverageRatingsRollCall(3.6f);
            tmpCourse.setAverageRatingsScoring(3.7f);
            tmpCourse.setAverageRatingsSpareTimeOccupation(3.8f);
            tmpCourse.setAverageRatingsUsefulness(3.9f);
            tmpCourse.setAverageRatingsVividness(4.0f);
            tmpCourse.setRecommendationScore(4.9f);
            tmpCourse.setPeopleCount(1999);
            tmpCourse.setTeacherList(new ArrayList<Course.TeacherBrief>());
            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("张彦泽", 1));
            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("黄嘉星", 2));
            tmpCourse.getTeacherList().add(tmpCourse.new TeacherBrief("鲁迪", 3));
            hotCoursesList.add(tmpCourse);
        }
        hotCommentsList = new ArrayList<>();
        Comment tmpComment;
        for (int i = 0; i < 20; i++) {
            tmpComment = new Comment(4, i, i, "asdsadf" + i, "asddsa", "哈哈哈这是第几个评论你哈哈哈这是第几个评论你哈哈哈这是第几个评论你哈哈哈这是第几个评论你", "啥子课哦", i + 1221);
            hotCommentsList.add(tmpComment);
        }
        //TODO:从网络加载数据
        coursesAdapter = new HotCoursesAdapter(hotCoursesList);
        commentsAdapter = new HotCommentsAdapter(this.getActivity(), hotCommentsList);
    }

    private void initView() {
        mCommentList.setFocusable(false);
        mScrollView.scrollTo(0, 0);
        mPileLayout.setAdapter(coursesAdapter);
        mCommentList.setAdapter(commentsAdapter);
    }


    private static class CommentViewHolder {
        ImageView avatar;
        RatingBar ratingBar;
        TextView nickName, timeStamp, commentContent, likeCount;
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
            viewHolder.ratings.setNumStars(tmpCourse.getPeopleCount());
            viewHolder.peopleCount.setText(String.format(getString(R.string.item_fragment_discover_peopleCount), tmpCourse.getPeopleCount()));
            viewHolder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:打开课程页面
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
                viewHolder = (CommentViewHolder) convertView.getTag();
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
