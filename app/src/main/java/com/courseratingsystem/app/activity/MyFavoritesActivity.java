package com.courseratingsystem.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.courseratingsystem.app.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_my_favorites)
public class MyFavoritesActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_my_favorite_listView)
    private ListView listView;

    private List<Map<String,Object>> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("课程收藏管理");
        String[] keys = {
                "coursename",
//                "recommendationScore",
                "clickToCourse",
                "clickToDelete"
        };
        int[] ids = {
                R.id.item_activity_my_favorites_coursename,
//                R.id.item_activity_my_favorites_recommendationScore,
                R.id.item_activity_my_favorites_clickToCourse,
                R.id.item_activity_my_favorites_clickToDelete
        };
        SimpleAdapter simpleAdapter =
                new SimpleAdapter(
                        MyFavoritesActivity.this,
                        lists,
                        R.layout.item_activity_my_favorites,
                        keys,ids
                );
        listView.setAdapter(simpleAdapter);
        for(int i=0;i<20;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("coursename","葫芦丝演奏与提高");
//            map.put("recommendationScore","1");
            map.put("clickToCourse","戳详情");
            map.put("clickToDelete","戳删除");
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
