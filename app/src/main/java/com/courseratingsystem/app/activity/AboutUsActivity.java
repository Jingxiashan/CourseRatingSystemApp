package com.courseratingsystem.app.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.tools.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    private ViewPager ViewPager_kongxiao,ViewPager_zhangyanze,
            ViewPager_huangjiaxing,ViewPager_ludi;

    private int[] mImgIds_kongxiao = new int[] {
            R.drawable.kongxiao_boom,
            R.drawable.kongxiao};
    private int[] mImgIds_zhangyanze = new int[] {
            R.drawable.zhangyanze_pretty,
            R.drawable.zhangyanze};
    private int[] mImgIds_huangjiaxing = new int[] {
            R.drawable.huangjiaxing_here,
            R.drawable.huangjiaxing};
    private int[] mImgIds_ludi = new int[] {
            R.drawable.ludi_love,
            R.drawable.ludi};

    private List<ImageView> mImageViews_kongxiao = new ArrayList<ImageView>();
    private List<ImageView> mImageViews_zhangyanze = new ArrayList<ImageView>();
    private List<ImageView> mImageViews_huangjiaxing = new ArrayList<ImageView>();
    private List<ImageView> mImageViews_ludi = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("关于我们");

        initData(mImgIds_kongxiao,mImageViews_kongxiao);
        initData(mImgIds_zhangyanze,mImageViews_zhangyanze);
        initData(mImgIds_huangjiaxing,mImageViews_huangjiaxing);
        initData(mImgIds_ludi,mImageViews_ludi);

        ViewPager_kongxiao = (ViewPager)findViewById(R.id.activity_about_us_kongxiao);
        ViewPager_zhangyanze = (ViewPager)findViewById(R.id.activity_about_us_zhangyanze);
        ViewPager_huangjiaxing = (ViewPager)findViewById(R.id.activity_about_us_huangjiaxing);
        ViewPager_ludi= (ViewPager)findViewById(R.id.activity_about_us_ludi);

        ViewPager_kongxiao.setPageTransformer(true, new DepthPageTransformer());
        ViewPager_zhangyanze.setPageTransformer(true, new DepthPageTransformer());
        ViewPager_huangjiaxing.setPageTransformer(true, new DepthPageTransformer());
        ViewPager_ludi.setPageTransformer(true, new DepthPageTransformer());

        ViewPager_kongxiao.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {

                container.addView(mImageViews_kongxiao.get(position));
                return mImageViews_kongxiao.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {

                container.removeView(mImageViews_kongxiao.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object)
            {
                return view == object;
            }

            @Override
            public int getCount()
            {
                return mImgIds_kongxiao.length;
            }
        });

        ViewPager_zhangyanze.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {

                container.addView(mImageViews_zhangyanze.get(position));
                return mImageViews_zhangyanze.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {

                container.removeView(mImageViews_zhangyanze.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object)
            {
                return view == object;
            }

            @Override
            public int getCount()
            {
                return mImgIds_zhangyanze.length;
            }
        });

        ViewPager_huangjiaxing.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {

                container.addView(mImageViews_huangjiaxing.get(position));
                return mImageViews_huangjiaxing.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {

                container.removeView(mImageViews_huangjiaxing.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object)
            {
                return view == object;
            }

            @Override
            public int getCount()
            {
                return mImgIds_huangjiaxing.length;
            }
        });

        ViewPager_ludi.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {

                container.addView(mImageViews_ludi.get(position));
                return mImageViews_ludi.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {

                container.removeView(mImageViews_ludi.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object)
            {
                return view == object;
            }

            @Override
            public int getCount()
            {
                return mImgIds_ludi.length;
            }
        });
    }

    private void initData(int[] mImgIds,List<ImageView> mImageViews)
    {
        for (int imgId : mImgIds)
        {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            mImageViews.add(imageView);
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
