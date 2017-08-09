package com.courseratingsystem.app.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.view.CommentListView;
import com.courseratingsystem.app.vo.Comment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_comment)
public class CommentActivity extends AppCompatActivity {
    public final static String COMMENT_KEY = "my_currentcomment";

    AnimatorSet hideElementAnimatorSet;//这是隐藏头尾元素使用的动画
    AnimatorSet showElementAnimatorSet;//这是显示头尾元素使用的动画

    @ViewInject(R.id.activity_comment_layout_courseheaderinfo)
    LinearLayout courseHeaderInfor;
    @ViewInject(R.id.activity_comment_listview_allcomments)
    private CommentListView allcomments_listView;
    @ViewInject(R.id.activity_comment_text_courseName)
    private TextView courseName;
    @ViewInject(R.id.activity_comment_linear_teacherlayout)
    private LinearLayout teacherLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View headerView = new View(this);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        courseHeaderInfor.measure(w, h);
        headerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) courseHeaderInfor.getMeasuredHeight()));
        headerView.setBackgroundColor(Color.parseColor("#00000000"));
        allcomments_listView.addHeaderView(headerView);
        allcomments_listView.setOnSwipeListener(new CommentListView.OnSwipeListener() {
            @Override
            public void onSwipe(CommentListView.SwipeDirect swipeDirect) {
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView teacherText = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        teacherText.setBackground(getDrawable(R.drawable.activity_comment_teacher_button_bg));
        teacherText.setGravity(Gravity.CENTER);
        teacherText.setLayoutParams(layoutParams);
        teacherText.setTextSize(15);
        teacherText.setText("孔啸");
        teacherText.setHeight(60);
        teacherText.setWidth(180);
        teacherText.setTextColor(getResources().getColor(R.color.teacher_blue_light));
        teacherLayout.addView(teacherText);

        teacherText.setOnClickListener(new View.OnClickListener() {
            //TODO 点击后弹出对应教师popupwindow
            @Override
            public void onClick(View v) {

            }
        });


        actionBar.setTitle("课程评论");
        CommentsAdapter commentsAdapter = new CommentsAdapter(CommentActivity.this, new ArrayList<Comment>());
        allcomments_listView.setAdapter(commentsAdapter);
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
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(courseHeaderInfor, "translationY", courseHeaderInfor.getTranslationY(), -courseHeaderInfor.getHeight());//将ToolBar隐藏到上面
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
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(courseHeaderInfor, "translationY", courseHeaderInfor.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            showElementAnimatorSet.setDuration(300);
            showElementAnimatorSet.playTogether(animators);
            showElementAnimatorSet.start();
        }
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
            viewHolder.ratingBar.setNumStars(tmpComment.getRecstar());
            viewHolder.nickName.setText(tmpComment.getNickname());
            viewHolder.timeStamp.setText(tmpComment.getTimestamp());
            viewHolder.commentContent.setText(tmpComment.getContent());
            viewHolder.likeCount.setText(tmpComment.getLikecount());

            viewHolder.showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommentActivity.this, CommentPopupActivity.class);
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

