package com.courseratingsystem.app.activity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.fragment.LoginFragment;
import com.courseratingsystem.app.fragment.RegisterFragment;
import com.mcxtzhang.pathanimlib.PathAnimView;
import com.mcxtzhang.pathanimlib.utils.SvgPathParser;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.text.ParseException;

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
    //网络相关
    private final String LOGIN_URL = "/login.action";
    private final String REGISTER_URL = "/register_checkAndRegister.action";
    private final String[] LOGIN_POST_KEY = new String[]{"username", "password"};
    private final String[] REGISTER_POST_KEY = new String[]{"username", "password", "nickname", "grade"};
    private final String SUCCESS = "success";
    private final String FAIL = "fail";
    @ViewInject(R.id.activity_login_tablayout)
    TabLayout mTabLayout;
    @ViewInject(R.id.activity_login_viewpager)
    ViewPager mViewPager;
    @ViewInject(R.id.fragment_login_anim_logo)
    PathAnimView mLogo;
    @ViewInject(R.id.activity_login_return_button)
    Button mReturn;

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
            LoginFragment.LoginStatus status;

            @Override
            public void onFailure(Call call, IOException e) {
                status = LoginFragment.LoginStatus.CONNECTION_FAILED;
                msg.obj = status;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                //TODO:解析JSON
                if (SUCCESS.equals(responseBody)) {
                    status = LoginFragment.LoginStatus.LOGIN_SUCCESSFULLY;
                } else {
                    status = LoginFragment.LoginStatus.WRONG_CREDENTIALS;
                }
                msg.obj = status;
                handler.sendMessage(msg);
            }
        });
    }

    public void attemptRegister(final String username, final String encryptedPass, final String nickname, final String grade, final Handler handler) {
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
            RegisterFragment.RegisterStatus status;

            @Override
            public void onFailure(Call call, IOException e) {
                status = RegisterFragment.RegisterStatus.CONNECTION_FAILED;
                msg.obj = status;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //TODO:解析JSON
                String responseBody = response.body().string();
                if (SUCCESS.equals(responseBody)) {
                    status = RegisterFragment.RegisterStatus.REGISTER_SUCCESFULLLY;
                } else {
                    status = RegisterFragment.RegisterStatus.DUPLICATE_USERNAME;
                }
                msg.obj = status;
                handler.sendMessage(msg);
            }
        });
    }

    @Event(value = R.id.activity_login_return_button, type = View.OnClickListener.class)
    private void onReturnClicked(View view) {
        this.onBackPressed();
    }

    public enum FragmentsSelection {LOGIN_FRAGMENT, REGISTER_FRAGMENT}

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager supportFragmentManager) {
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
