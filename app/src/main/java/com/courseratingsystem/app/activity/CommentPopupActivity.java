package com.courseratingsystem.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.vo.Comment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_comment_popup)
public class CommentPopupActivity extends AppCompatActivity {
    public final static String COMMENT_KEY = "my_currentcomment";

    @ViewInject(R.id.activity_commentpopup_ratingbar_commentRating)
    private RatingBar ratingBarPop;
    @ViewInject(R.id.activity_commentpopup_text_commentContent)
    private TextView contentPop;
    @ViewInject(R.id.activity_commentpopup_text_coursename)
    private TextView coursenamePop;
    @ViewInject(R.id.activity_commentpopup_text_timestamp)
    private TextView timestampPop;
    @ViewInject(R.id.activity_commentpopup_text_userName)
    private TextView usernamePop;
    @ViewInject(R.id.activity_commentpopup_text_likeCount)
    private TextView likecountPop;
    @ViewInject(R.id.activity_commentpopup_button_like)
    private ImageButton likebuttonPop;
    @ViewInject(R.id.activity_commentpopup_button_back)
    private ImageButton backPop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        Comment comment = (Comment) getIntent().getSerializableExtra(COMMENT_KEY);
        ratingBarPop.setRating(comment.getRecstar());
        usernamePop.setText(comment.getNickname());
        contentPop.setText(comment.getContent());
        timestampPop.setText(comment.getTimestamp());
        coursenamePop.setText(comment.getCoursename());

        likebuttonPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 点赞
            }
        });

        backPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

}
