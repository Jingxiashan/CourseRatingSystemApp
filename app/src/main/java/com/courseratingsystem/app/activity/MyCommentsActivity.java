package com.courseratingsystem.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.courseratingsystem.app.R;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@ContentView(R.layout.activity_my_comments)
public class MyCommentsActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_my_comments_listView)
    private ListView listView;

    private List<Map<String,Object>> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("发表评论管理");
        String[] keys = {
                "image","nickname","time", "critics",
                "teacher","course","likeCount","delete"
        };
        int[] ids = {
                R.id.item_activity_my_comments_image,
                R.id.item_activity_my_comments_nickname,
                R.id.item_activity_my_comments_time,
                R.id.item_activity_my_comments_critics,
                R.id.item_activity_my_comments_teacher,
                R.id.item_activity_my_comments_course,
                R.id.item_activity_my_comments_likeCount,
                R.id.item_activity_my_comments_delete
        };
        SimpleAdapter simpleAdapter =
                new SimpleAdapter(
                        MyCommentsActivity.this,
                        lists,
                        R.layout.item_activity_my_comments,
                        keys,ids
                );
        listView.setAdapter(simpleAdapter);
        for(int i=0;i<20;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",R.drawable.default_personal_image);
            map.put("nickname","然也");
            map.put("time","2016-03-01 06:44:00");
            map.put("critics","略严格");
            map.put("teacher","冯剑丰");
            map.put("course","海洋与人类文明");
            map.put("likeCount","3");
            map.put("delete",R.drawable.my_comments_delete);
            lists.add(map);
        }
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
