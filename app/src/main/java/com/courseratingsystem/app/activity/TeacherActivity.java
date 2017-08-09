package com.courseratingsystem.app.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.courseratingsystem.app.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_teacher)
public class TeacherActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_teacher_imgview_photo)
    ImageView teacherphoto;
    @ViewInject(R.id.activiyt_teacher_listview_commentlist)
    ListView commentlist;
    @ViewInject(R.id.activity_teacher_scrollview)
    ScrollView scrollView;

    private int objectId[] = {R.id.activity_comment_image_courseImag, R.id.activity_comment_text_userName,
            R.id.activity_comment_text_timestamp, R.id.activity_comment_text_commentContent,
            R.id.activity_comment_text_likeCount, R.id.activity_comment_ratingbar_commentRating};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        MyAdapter myAdapter = new MyAdapter(TeacherActivity.this);
        commentlist.setAdapter(myAdapter);
        commentlist.setFocusable(false);
        scrollView.scrollTo(0,0);
    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;

        public MyAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            //comment数目
            return 12;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //动态加载布局
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.activity_comment_list_item, null);

            final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.activity_comment_ratingbar_commentRating);
            ImageView courseImage = (ImageView) view.findViewById(R.id.activity_comment_image_courseImag);
            final TextView userName = (TextView) view.findViewById(R.id.activity_comment_text_userName);
            final TextView timeStamp = (TextView) view.findViewById(R.id.activity_comment_text_timestamp);
            final TextView commentContent = (TextView) view.findViewById(R.id.activity_comment_text_commentContent);
            TextView likeCount = (TextView) view.findViewById(R.id.activity_comment_text_likeCount);

            ratingBar.setNumStars(3);
            userName.setText("User_Name");
            timeStamp.setText("Comment_Timestamp");
            commentContent.setText("current comment is written by someone else.");
            likeCount.setText("10");

            final ImageButton showDetail = (ImageButton) view.findViewById(R.id.activity_comment_button_detail);
            showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View commentPopupView = layoutInflater.inflate(R.layout.activity_comment_popup, null);

                    TextView coursenamePop = (TextView) commentPopupView.findViewById(R.id.activity_commentpopup_text_coursename);
                    TextView usernamePop = (TextView) commentPopupView.findViewById(R.id.activity_commentpopup_text_userName);
                    TextView timestampPop = (TextView) commentPopupView.findViewById(R.id.activity_commentpopup_text_timestamp);
                    TextView commentcontentPop = (TextView) commentPopupView.findViewById(R.id.activity_commentpopup_text_commentContent);
                    RatingBar ratingBarPop = (RatingBar) commentPopupView.findViewById(R.id.activity_commentpopup_ratingbar_commentRating);

                    ratingBarPop.setNumStars(ratingBar.getNumStars());
                    coursenamePop.setText("课程1");
                    timestampPop.setText(timeStamp.getText());
                    commentcontentPop.setText(commentContent.getText());
                    usernamePop.setText(userName.getText());

                    PopupWindow commentPopupWindow = new PopupWindow(commentPopupView, 1000, 1510);

                    commentPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                    commentPopupWindow.setFocusable(true);
                    commentPopupWindow.setOutsideTouchable(true);
                    commentPopupWindow.update();


                    commentPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


                }
            });

            return view;
        }
    }
}
