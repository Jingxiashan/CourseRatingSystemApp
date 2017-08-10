package com.courseratingsystem.app.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.vo.Comment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_my_favorites)
public class MyFavoritesActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_my_favorite_listView)
    private ListView listView;
    private List<Map<String,Object>> lists = new ArrayList<>();

    private static final int WITHOUT_INTERNET = 0;
    private static final int GET_MYFAVORITES_IS_SUCCESS = 1;
    private static final int GET_MYFAVORITES_IS_FAIL = 2;

    OkHttpClient okHttpClient = new OkHttpClient();
    //Handler 主线程创建---消息的处理主线程
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WITHOUT_INTERNET:
                    String result_without_internet = (String)msg.obj;
                    Toast.makeText(MyFavoritesActivity.this,
                            "网络异常！请稍后再试",Toast.LENGTH_SHORT).show();
                    break;
                case GET_MYFAVORITES_IS_SUCCESS:
                    String result_get_information = (String)msg.obj;
                    //根据得到的result_get_information设置信息
                    setText(result_get_information);
                    break;
                case GET_MYFAVORITES_IS_FAIL:
                    Toast.makeText(MyFavoritesActivity.this,
                            "收藏课程获取失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("课程收藏管理");

        String url = new String("http://www.baidu.com");//获得用户评论的url
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        exec_getMyFavorites(request);
    }

    private void setText(String result_get_information) {
        //获得用户评论成功，填写……
        //将结果转换为JSON类型

        String[] keys = {
                "courseName",
                "comment",
                "recScore",
                "recComment",
                "commentCount",
                "otherScores",
                "teacher",
                "delete"
        };
        int[] ids = {
                R.id.item_activity_my_favorites_courseName,
                R.id.item_activity_my_favorites_comment,
                R.id.item_activity_my_favorites_recScore,
                R.id.item_activity_my_favorites_recComment,
                R.id.item_activity_my_favorites_commentCount,
                R.id.item_activity_my_favorites_otherScores,
                R.id.item_activity_my_favorites_teacher,
                R.id.item_activity_my_favorites_delete
        };
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                        MyFavoritesActivity.this, lists,
                        R.layout.item_activity_my_favorites, keys,ids);
        listView.setAdapter(simpleAdapter);
        for(int i=0;i<20;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("courseName","葫芦丝演奏与提高");
            map.put("comment","老司机有话说");
            map.put("recScore","4.5分");
            map.put("recComment","非常棒");
            map.put("commentCount","99条评论");
            map.put("otherScores","有用3.0 有趣2.0 占时3.5 给分3.5 点名2.0");
            map.put("teacher","冯剑锋");
            map.put("delete",R.drawable.delete);
            lists.add(map);
        }
    }

    //获得用户收藏课程返回信息处理
    private void exec_getMyFavorites(Request request) {
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
                    message.what = GET_MYFAVORITES_IS_SUCCESS;
                    message.obj = response.body().string();
                    Log.i("收藏课程获得成功","--->"+message.obj);
                    handler.sendMessage(message);
                }else {
                    Log.i("收藏课程获得失败","--->");
                    handler.sendEmptyMessage(GET_MYFAVORITES_IS_FAIL);
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

}
