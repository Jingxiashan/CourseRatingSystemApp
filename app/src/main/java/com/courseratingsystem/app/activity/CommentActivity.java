package com.courseratingsystem.app.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
import android.widget.TextView;

import com.courseratingsystem.app.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_comment)
public class CommentActivity extends AppCompatActivity {
    private List<Map<String, Object>> allcommentslists = new ArrayList<>();
    @ViewInject(R.id.activity_comment_listview_allcomments)
    private ListView allcomments_listView;

    @ViewInject(R.id.activity_comment_text_courseName)
    private TextView courseName;

    private int objectId[] = {R.id.activity_comment_image_courseImag, R.id.activity_comment_text_userName,
            R.id.activity_comment_text_timestamp, R.id.activity_comment_text_commentContent,
            R.id.activity_comment_text_likeCount, R.id.activity_comment_ratingbar_commentRating};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        MyAdapter myAdapter = new MyAdapter(CommentActivity.this);
        allcomments_listView.setAdapter(myAdapter);

//        String[] keys={"comment_img","comment_username","comment_timestamp",
//                "comment_content","comment_likecount"};
//        int[] ids={R.id.activity_comment_image_courseImag,R.id.activity_comment_text_userName,
//        R.id.activity_comment_text_timestamp,R.id.activity_comment_text_commentContent,
//        R.id.activity_comment_text_likeCount};
//
//
//
//
//        for(int i=1;i<12;i++){
//            Map<String,Object> map=new HashMap<>();
//            map.put(keys[0],R.drawable.comment_like_button_icon);
//            map.put(keys[1],"User_Name");
//            map.put(keys[2],"Comment_Timestamp");
//            map.put(keys[3],"current comment is written by someone else.");
//            map.put(keys[4],"100");
////            RatingBar comment_ratingbar=(RatingBar)findViewById(R.id.activity_comment_ratingbar_commentRating);
////            comment_ratingbar.setNumStars(3);
//            allcommentslists.add(map);
//        }
//        SimpleAdapter simpleAdapter=new SimpleAdapter(CommentActivity.this,allcommentslists,R.layout.activity_comment_list_item,
//                keys,ids);
//        allcomments_listView.setAdapter(simpleAdapter);
    }

    private int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowsPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        //获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        //获取屏幕宽高
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int screenHeight = metrics.widthPixels;
        final int screenWidth = metrics.heightPixels;
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //计算conetentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getWidth();
        windowsPos[0] = screenWidth - windowWidth;
        windowsPos[1] = anchorLoc[1] + anchorHeight;
        return windowsPos;
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
                    coursenamePop.setText(courseName.getText());
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

