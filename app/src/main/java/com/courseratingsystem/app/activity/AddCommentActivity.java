package com.courseratingsystem.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.vo.Comment;
import com.courseratingsystem.app.vo.Course;
import com.courseratingsystem.app.vo.Teacher;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_add_comment)

public class AddCommentActivity extends AppCompatActivity {
    public static final String COURSE_INFO = "course_info";
    private final String ADD_COMMENT_URL = "/addComment";
    //public List<String> teachernameList=new ArrayList<>();

//    @ViewInject(R.id.activity_addcomment_layout_pile)
//    PileLayout attributePile;

//    @ViewInject(R.id.activity_addcomment_linear_teacherlayout)
//    LinearLayout teacherLayout;
private final int SUCCEEDED = 0;
    private final int FAILED = 1;
    //    public List<attribute> attributeList;
    public Comment comment = new Comment();
    @ViewInject(R.id.activity_addcomment_button_submitComment)
    Button commentSubmit;
    @ViewInject(R.id.acticity_addcomment_editText_commentContent)
    private EditText commentcontent;
    @ViewInject(R.id.activity_addcomment_spinner_courseTeacherSelect)
    private Spinner teacherName;
    @ViewInject(R.id.activity_addcomment_text_courseName)
    private TextView courseName;
    @ViewInject(R.id.activity_addcomment_linear_teacherlayout)
    private LinearLayout teacherLayout;
    @ViewInject(R.id.activity_addcomment_rating_rollcall)
    private RatingBar rollcallRating;
    @ViewInject(R.id.activity_addcomment_rating_usefulness)
    private RatingBar usefulnessRating;
    @ViewInject(R.id.activity_addcomment_rating_vividness)
    private RatingBar vivdnessRating;
    @ViewInject(R.id.activity_addcomment_rating_scorehigh)
    private RatingBar scorehighRating;
    @ViewInject(R.id.activity_addcomment_rating_timeoccupation)
    private RatingBar timeoccuRating;
    @ViewInject(R.id.activity_addcomment_rating_courseRecommendation)
    private RatingBar courseRecRating;
    private String[] ADD_COMMENT_POST_KEY = new String[]{
            "teacherId"
            ,"courseId"
            ,"userId"
            ,"ratingUsefulness"
            ,"ratingVividness"
            ,"ratingSpareTimeOccupation"
            ,"ratingScoring"
            ,"ratingRollCall"
            ,"recommandScore"
            ,"critics"};
    private Course course;

    private final Handler addCommentHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SUCCEEDED:
                    Toast.makeText(AddCommentActivity.this,"评论成功",Toast.LENGTH_LONG);
                    MyCourseApplication application = (MyCourseApplication) getApplication();
                    comment.setContent(commentcontent.getText().toString());
                    comment.setCoursename(course.getCourseName());
                    comment.setNickname(application.getUsername());
                    comment.setTimestamp(getTime());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("my_currentcomment", comment);
                    startActivity(new Intent(AddCommentActivity.this, CommentPopupActivity.class).putExtras(bundle));
                    finish();
                    break;
                case FAILED:
                    Toast.makeText(AddCommentActivity.this,"发表评论失败！",Toast.LENGTH_LONG);
            }
            return false;
        }
    });
    private String getTime(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

    private void postComment(){
        MyCourseApplication application = (MyCourseApplication) getApplication();
        OkHttpClient client = ((MyCourseApplication)getApplication()).getOkHttpClient();
        okhttp3.RequestBody formbody = new FormBody.Builder()
                .add(ADD_COMMENT_POST_KEY[0],String.valueOf(course.getTeacherList().get(teacherName.getSelectedItemPosition()).getTeacherId()))
                .add(ADD_COMMENT_POST_KEY[1],String.valueOf(course.getCourseId()))
                .add(ADD_COMMENT_POST_KEY[2],String.valueOf(application.getUserId()))
                .add(ADD_COMMENT_POST_KEY[3],String.valueOf(comment.getUsefulness()))
                .add(ADD_COMMENT_POST_KEY[4],String.valueOf(comment.getVivdness()))
                .add(ADD_COMMENT_POST_KEY[5],String.valueOf(comment.getSparetimeoccupation()))
                .add(ADD_COMMENT_POST_KEY[6],String.valueOf(comment.getScorehigh()))
                .add(ADD_COMMENT_POST_KEY[7],String.valueOf(comment.getRollcall()))
                .add(ADD_COMMENT_POST_KEY[8],String.valueOf(comment.getRecstar()))
                .add(ADD_COMMENT_POST_KEY[3],comment.getContent())
                .build();
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL+ADD_COMMENT_URL)
                .post(formbody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            Message msg = new Message();
            @Override
            public void onFailure(Call call, IOException e) {
                msg.what = FAILED;
                addCommentHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body()!=null){
                    String responseBody = response.body().string();
                    try{
                        JSONObject responseJson = new JSONObject(responseBody);
                        int statusCode = responseJson.getInt(MyCourseApplication.JSON_RESULT_CODE);
                        if(statusCode == MyCourseApplication.JSON_RESULT_CODE_200){
                            msg.what = SUCCEEDED;
                            addCommentHandler.sendMessage(msg);
                        }
                        else {
                            msg.what = FAILED;
                            addCommentHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("课程评论");

        course = (Course) getIntent().getSerializableExtra(COURSE_INFO);
        courseName.setText(course.getCourseName());
        List<String> teachernameList = new ArrayList<>();
        for (int i = 0; i < course.getTeacherList().size(); i++) {
            Teacher teacher = new Teacher();
            teacher.setTeachername(course.getTeacherList().get(i).getTeacherName());
            teachernameList.add(teacher.getTeachername());

            TextView teacherText = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            teacherText.setBackground(getDrawable(R.drawable.activity_comment_teacher_button_bg));
            teacherText.setGravity(Gravity.CENTER);
            teacherText.setLayoutParams(layoutParams);
            teacherText.setTextSize(15);
            teacherText.setText(course.getTeacherList().get(i).getTeacherName());
            teacherText.setHeight(60);
            teacherText.setWidth(180);
            teacherText.setTextColor(getResources().getColor(R.color.teacher_blue_light));
            teacherLayout.addView(teacherText);

        }


        ArrayAdapter teacherAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, teachernameList);
        //ArrayAdapter teacherAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,course.getTeacherList());
        teacherName.setAdapter(teacherAdapter);

//        for(int i=0;i<course.getTeacherList().size();i++){
//            final TextView teacherText = new TextView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(10, 10, 10, 10);
//            teacherText.setBackground(getDrawable(R.drawable.activity_comment_teacher_button_bg));
//            teacherText.setGravity(Gravity.CENTER);
//            teacherText.setLayoutParams(layoutParams);
//            teacherText.setTextSize(15);
//            teacherText.setText(course.getTeacherList().get(i).getTeacherName());
//            teacherText.setHeight(60);
//            teacherText.setWidth(160);
//            teacherText.setTextColor(getResources().getColor(R.color.teacher_blue_light));
//            teacherLayout.addView(teacherText);
//
//            teacherText.setOnClickListener(new View.OnClickListener() {
//                //TODO 点击后弹出对应教师popupwindow
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }

        courseRecRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                comment.setRecstar(rating);
            }
        });



        usefulnessRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                comment.setUsefulness(rating);
            }
        });

        vivdnessRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                comment.setVivdness(rating);
            }
        });

        scorehighRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                comment.setScorehigh(rating);
            }
        });

        rollcallRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                comment.setRollcall(rating);
            }
        });

        scorehighRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                comment.setScorehigh(rating);
            }
        });

        timeoccuRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                comment.setSparetimeoccupation(rating);
            }
        });

        //评论提交，弹出刚填写好的评论popupwindow

        //TODO:获得更新后该课程推荐星级、弹出窗口的用户信息时间戳等
        commentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentcontent.getText().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCommentActivity.this);
                    builder.setTitle(getResources().getString(R.string.activity_addcomment_alertdialog_title));
                    builder.setIcon(R.drawable.alert);
                    builder.setMessage(getResources().getString(R.string.activity_addcomment_warn_nocommentcontent));
                    builder.setPositiveButton(getResources().getString(R.string.activity_addcomment_reply_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                } else {
                    postComment();
                }
            }
        });


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

//    private void initView() {
//        attribute attribute1 = new attribute(getResources().getString(R.string.activity_addcomment_attrname_usefulness),
//                getResources().getString(R.string.activity_addcomment_attrdescrip_usefulness));
//        attribute attribute2 = new attribute(getResources().getString(R.string.activity_addcomment_attrname_vividness),
//                getResources().getString(R.string.activity_addcomment_attrdescrip_vividness));
//        attribute attribute3 = new attribute(getResources().getString(R.string.activity_addcomment_attrname_timeoccupation),
//                getResources().getString(R.string.activity_addcomment_attrdescrip_vividness));
//        attribute attribute4 = new attribute(getResources().getString(R.string.activity_addcomment_attrname_scorehigh),
//                getResources().getString(R.string.activity_addcomment_attrdescrip_scorehigh));
//        attribute attribute5 = new attribute(getResources().getString(R.string.activity_addcomment_attrname_rollcall),
//                getResources().getString(R.string.activity_addcomment_attrdescrip_rollcall));
//
//        attributeList = new ArrayList<>();
//        attributeList.add(attribute1);
//        attributeList.add(attribute2);
//        attributeList.add(attribute3);
//        attributeList.add(attribute4);
//        attributeList.add(attribute5);
//
//        AttributeAdapter attributeAdapter = new AttributeAdapter();
//        attributePile.setAdapter(attributeAdapter);
//
//
//    }
//
//    private class attribute {
//        private String attrname;
//        private String attrdescrip;
//
//        attribute(String tname, String tdescrip) {
//            attrname = tname;
//            attrdescrip = tdescrip;
//        }
//    }
//
//    private class AttributeAdapter extends PileLayout.Adapter {
//
//        public int getLayoutId() {
//            return R.layout.item_activity_addcomment_attribute;
//        }
//
//        @Override
//        public int getItemCount() {
//            return attributeList.size();
//        }
//
//        @Override
//        public void bindView(View view, final int index) {
//            super.bindView(view, index);
//            final attribute tattribute = attributeList.get(index);
//
//            TextView attributeName = (TextView) view.findViewById(R.id.item_addcomment_text_attributename);
//            TextView attributeDescrip = (TextView) view.findViewById(R.id.item_addcomment_text_attributedescrip);
//            final RatingBar attributeRating = (RatingBar) view.findViewById(R.id.item_addcomment_rating_attributerating);
//
//            attributeName.setText(tattribute.attrname);
//            attributeDescrip.setText(tattribute.attrdescrip);
//            //属性默认评分
//            attributeRating.setNumStars(0);
//            attributeRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                @Override
//                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                    if (index == 0) {
//                        comment.setUsefulness((int) rating);
//                    }
//                    if (index == 1) {
//                        comment.setVivdness((int) rating);
//                    }
//                    if (index == 2) {
//                        comment.setSparetimeoccupation((int) rating);
//                    }
//                    if (index == 3) {
//                        comment.setScorehigh((int) rating);
//                    }
//                    if (index == 4) {
//                        comment.setRollcall((int) rating);
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void displaying(int position) {
//            super.displaying(position);
//        }
//
//        @Override
//        public void onItemClick(View view, int position) {
//            super.onItemClick(view, position);
//        }
//    }
}
