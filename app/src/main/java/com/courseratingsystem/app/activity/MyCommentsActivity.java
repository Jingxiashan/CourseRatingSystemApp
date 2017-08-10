package com.courseratingsystem.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.vo.Comment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_my_comments)
public class MyCommentsActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_my_comments_listView)
    private ListView listView;

    private List<Map<String,Object>> lists = new ArrayList<>();

    private static final int WITHOUT_INTERNET = 0;
    private static final int GET_MYCOMMENTS_IS_SUCCESS = 1;
    private static final int GET_MYCOMMENTS_IS_FAIL = 2;

    OkHttpClient okHttpClient = new OkHttpClient();
    //Handler 主线程创建---消息的处理主线程
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WITHOUT_INTERNET:
                    String result_without_internet = (String)msg.obj;
                    Toast.makeText(MyCommentsActivity.this,
                            "网络异常！请稍后再试",Toast.LENGTH_SHORT).show();
                    break;
                case GET_MYCOMMENTS_IS_SUCCESS:
                    String result_get_information = (String)msg.obj;
                    //根据得到的result_get_information设置信息
                    setText(result_get_information);
                    break;
                case GET_MYCOMMENTS_IS_FAIL:
                    Toast.makeText(MyCommentsActivity.this,
                            "发表评论获取失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("发表评论管理");

        String url = new String("http://www.baidu.com");//获得用户评论的url
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        exec_getMyComments(request);
    }

    private void setText(String result_get_information){
        if(true)
            return;
        //获得个人信息成功，填写……
        //将结果转换为JSON类型
        try{
            JSONObject responseJson = new JSONObject(result_get_information);//细化，现在有问题
        }catch (JSONException e){
            e.printStackTrace();
        }

        //……最终得到许多Comment对象，构成一个Comment List

        CommentsAdapter commentsAdapter =
                new CommentsAdapter(MyCommentsActivity.this, new ArrayList<Comment>());//将得到的Comment List放入
        listView.setAdapter(commentsAdapter);

//        //获得用户评论成功，填写……
//        //将结果转换为JSON类型
//
//        String[] keys = {
//                "image","nickname","time", "critics",
//                "teacher","course","likeCount","delete"
//        };
//        int[] ids = {
//                R.id.item_activity_my_comments_image,
//                R.id.item_activity_my_comments_nickname,
//                R.id.item_activity_my_comments_time,
//                R.id.item_activity_my_comments_critics,
//                R.id.item_activity_my_comments_teacher,
//                R.id.item_activity_my_comments_course,
//                R.id.item_activity_my_comments_likeCount,
//                R.id.item_activity_my_comments_delete
//        };
//        SimpleAdapter simpleAdapter =
//                new SimpleAdapter(
//                        MyCommentsActivity.this,
//                        lists,
//                        R.layout.item_activity_my_comments,
//                        keys,ids
//                );
//        listView.setAdapter(simpleAdapter);
//        for(int i=0;i<20;i++){
//            Map<String,Object> map = new HashMap<>();
//            map.put("image",R.drawable.default_personal_image);//修改为用户头像
//            map.put("nickname","然也");
//            map.put("time","2016-03-01 06:44:00");
//            map.put("critics","略严格");
//            map.put("teacher","冯剑丰");
//            map.put("course","海洋与人类文明");
//            map.put("likeCount","3");
//            map.put("delete",R.drawable.delete);
//            lists.add(map);
//        }
    }

    //获得用户评论返回信息处理
    private void exec_getMyComments(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("无网络连接","--->"+e);
                Message message = new Message();
                message.what = WITHOUT_INTERNET;
                message.obj = e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = handler.obtainMessage();
                if(response.isSuccessful()){
                    message.what = GET_MYCOMMENTS_IS_SUCCESS;
                    message.obj = response.body().string();
                    Log.i("发表评论获取成功","--->"+message.obj);
                    handler.sendMessage(message);
                }else {
                    Log.i("发表评论获取失败","--->");
                    handler.sendEmptyMessage(GET_MYCOMMENTS_IS_FAIL);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private static class CommentsViewHolder {
        ImageView avatar;//用户头像
        TextView nickName, timeStamp, commentContent, teacher, course, likeCount;
        ImageButton showDetail, clickDelete;
    }

    private class CommentsAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Comment> commentList;

        public CommentsAdapter(Context context, List<Comment> commentList) {
            inflater = LayoutInflater.from(context);
            this.commentList = commentList;
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
            CommentsViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new CommentsViewHolder();
                convertView = inflater.inflate(R.layout.item_activity_my_comments, null);

                viewHolder.avatar = (ImageView) convertView.findViewById(R.id.item_activity_my_comments_image);
                viewHolder.nickName = (TextView) convertView.findViewById(R.id.item_activity_my_comments_nickname);
                viewHolder.timeStamp = (TextView) convertView.findViewById(R.id.item_activity_my_comments_time);
                viewHolder.commentContent = (TextView) convertView.findViewById(R.id.item_activity_my_comments_critics);
                viewHolder.teacher = (TextView)convertView.findViewById(R.id.item_activity_my_comments_teacher);
                viewHolder.course = (TextView)convertView.findViewById(R.id.item_activity_my_comments_course);
                viewHolder.likeCount = (TextView) convertView.findViewById(R.id.item_activity_my_comments_likeCount);
                viewHolder.clickDelete = (ImageButton) convertView.findViewById(R.id.item_activity_my_comments_delete);
                viewHolder.showDetail = (ImageButton) convertView.findViewById(R.id.item_activity_my_comments_detail);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CommentsViewHolder) convertView.getTag();
            }

            //设置头像显示
            viewHolder.nickName.setText(tmpComment.getNickname());
            viewHolder.timeStamp.setText(tmpComment.getTimestamp());
            viewHolder.commentContent.setText(tmpComment.getContent());
            viewHolder.teacher.setText(tmpComment.getTeachername());
            viewHolder.course.setText(tmpComment.getCoursename());
            viewHolder.likeCount.setText(tmpComment.getLikecount());

            viewHolder.teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:进入教师界面
                }
            });

            viewHolder.course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:进入课程界面
                }
            });

            viewHolder.showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyCommentsActivity.this, CommentPopupActivity.class);
                    intent.putExtra(CommentPopupActivity.COMMENT_KEY, tmpComment);
                    startActivity(intent);
                }
            });

            viewHolder.clickDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:删除
                }
            });

            return convertView;
        }
    }

}
