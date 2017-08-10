package com.courseratingsystem.app.activity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.fragment.LoginFragment;
import com.courseratingsystem.app.fragment.RegisterFragment;
import com.courseratingsystem.app.view.LoadingAnimView;
import com.courseratingsystem.app.view.SuccessAnimView;
import com.mcxtzhang.pathanimlib.PathAnimView;
import com.mcxtzhang.pathanimlib.utils.SvgPathParser;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    public static final String BUNDLE_FRAGMENT_SELECTION_KEY = "fragment";
    public static final int SUCCESS_ANIM_LENGTH = 2500;
    //网络相关
    private final String LOGIN_URL = "/login";
    private final String REGISTER_URL = "/register";
    private final String[] LOGIN_POST_KEY = new String[]{"username", "password"};
    private final String[] REGISTER_POST_KEY = new String[]{"username", "password", "nickname", "grade"};
    //控件
    @ViewInject(R.id.activity_login_layout)
    RelativeLayout mRelativeLayout;
    @ViewInject(R.id.activity_login_tablayout)
    TabLayout mTabLayout;
    @ViewInject(R.id.activity_login_viewpager)
    ViewPager mViewPager;
    @ViewInject(R.id.fragment_login_anim_logo)
    PathAnimView mLogo;
    @ViewInject(R.id.activity_login_return_button)
    Button mReturn;
    LoadingAnimView mLoadingAnimView;
    SuccessAnimView mSuccessAnimView;
    private String username;
    private final Handler successHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            MyCourseApplication application = (MyCourseApplication) getApplication();
            int userId = (int) msg.obj;
            application.login(userId, username);
            showSuccessAnim(true);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    LoginActivity.this.finish();
                }
            }, SUCCESS_ANIM_LENGTH);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //选择登录/注册
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            FragmentsSelection selection = (FragmentsSelection) bundle.get(BUNDLE_FRAGMENT_SELECTION_KEY);
            if (selection != null) {
                switch (selection) {
                    case LOGIN_FRAGMENT:
                        mViewPager.setCurrentItem(0);
                        break;
                    case REGISTER_FRAGMENT:
                        mViewPager.setCurrentItem(1);
                        break;
                }
            }
        }

        mViewPager.setAdapter(new ScreenSlidePagerAdapter(this.getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                ViewGroup vg = ((ViewGroup) mTabLayout.getChildAt(0));
                vg.setAlpha(0.8f);
                vg.animate()
                        .alpha(0.4f)
                        .setStartDelay(600)
                        .setDuration(400)
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .start();

                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                vgTab.setScaleX(0.8f);
                vgTab.setScaleY(0.8f);
                vgTab.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(450)
                        .start();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SvgPathParser svgPathParser = new SvgPathParser();
        String part_0, part_1, part_2, part_3;
        part_0 = getString(R.string.path_icon_rect);
        part_1 = getString(R.string.path_icon_char_part_1);
        part_2 = getString(R.string.path_icon_char_part_2);
        part_3 = getString(R.string.path_icon_char_part_3);
        try {
            Path path = svgPathParser.parsePath(part_1 + part_2 + part_3 + part_0);
            mLogo.setSourcePath(path);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mLogo
                .setAnimTime(2000)
                .setColorBg(getResources().getColor(R.color.splashBackColor))
                .setColorFg(getResources().getColor(R.color.pureWhite))
                .setAnimInfinite(false)
                .startAnim();

    }

    public void attemptLogin(final String username, final String encryptedPass, final Handler handler) {

        showLoadingAnim(true);

        OkHttpClient client = ((MyCourseApplication) getApplication()).getOkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(LOGIN_POST_KEY[0], username)
                .add(LOGIN_POST_KEY[1], encryptedPass)
                .build();
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL + LOGIN_URL)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            Message msg = new Message();

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.obj = getString(R.string.internet_connection_failed);
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseJson = new JSONObject(responseBody);
                        int statusCode = responseJson.getInt(MyCourseApplication.JSON_RESULT_CODE);
                        if (statusCode == MyCourseApplication.JSON_RESULT_CODE_200) {
                            //成功，获取userid并跳转
                            JSONObject resultJson = responseJson.getJSONObject(MyCourseApplication.JSON_RESULT);
                            LoginActivity.this.username = username;
                            msg.obj = resultJson.getInt("userid");
                            successHandler.sendMessage(msg);
                        } else {
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void attemptRegister(final String username, final String encryptedPass, final String nickname, final String grade, final Handler handler) {

        showLoadingAnim(true);

        OkHttpClient client = ((MyCourseApplication) getApplication()).getOkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(REGISTER_POST_KEY[0], username)
                .add(REGISTER_POST_KEY[1], encryptedPass)
                .add(REGISTER_POST_KEY[2], nickname)
                .add(REGISTER_POST_KEY[3], grade)
                .build();
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL + REGISTER_URL)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            Message msg = new Message();

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.obj = getString(R.string.internet_connection_failed);
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseJson = new JSONObject(responseBody);
                        int statusCode = responseJson.getInt(MyCourseApplication.JSON_RESULT_CODE);
                        if (statusCode == MyCourseApplication.JSON_RESULT_CODE_200) {
                            //成功，获取userid并跳转
                            JSONObject resultJson = responseJson.getJSONObject(MyCourseApplication.JSON_RESULT);
                            LoginActivity.this.username = username;
                            msg.obj = resultJson.getInt("userid");
                            successHandler.sendMessage(msg);
                        } else {
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Event(value = R.id.activity_login_return_button, type = View.OnClickListener.class)
    private void onReturnClicked(View view) {
        this.onBackPressed();
    }

    //显示加载动画
    public void showLoadingAnim(boolean ifShow) {
        if (ifShow) {
            if (mLoadingAnimView == null) {
                mLoadingAnimView = new LoadingAnimView(this, LoadingAnimView.BgColor.DARK);
                mRelativeLayout.addView(mLoadingAnimView);
            }
        } else {
            mRelativeLayout.removeView(mLoadingAnimView);
            mLoadingAnimView = null;
        }
    }

    //显示完成动画
    public void showSuccessAnim(boolean ifShow) {
        if (ifShow) {
            mSuccessAnimView = new SuccessAnimView(this, SuccessAnimView.BgColor.DARK);
            mRelativeLayout.addView(mSuccessAnimView);
        } else {
            mRelativeLayout.removeView(mLoadingAnimView);
        }
    }
    public enum FragmentsSelection {LOGIN_FRAGMENT, REGISTER_FRAGMENT}

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (position == 0) return new LoginFragment();
            return new RegisterFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
