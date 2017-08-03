package com.courseratingsystem.app.activity;

import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.fragment.LoginFragment;
import com.courseratingsystem.app.fragment.RegisterFragment;
import com.mcxtzhang.pathanimlib.PathAnimView;
import com.mcxtzhang.pathanimlib.utils.SvgPathParser;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;

@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_login_tablayout)
    TabLayout mTabLayout;
    @ViewInject(R.id.activity_login_viewpager)
    ViewPager mViewPager;

    @ViewInject(R.id.fragment_login_anim_logo)
    PathAnimView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

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

    public LoginFragment.LoginStatus attemptLogin(final String username, final String encryptedPass) {
        //TODO:网络连接
        return LoginFragment.LoginStatus.LOGIN_SUCCESSFULLY;
    }

    public RegisterFragment.RegisterStatus attemptRegister(final String username, final String encryptedPass, final String nickname, final String grade) {
        //TODO:网络连接
        return RegisterFragment.RegisterStatus.REGISTER_SUCCESFULLLY;
    }

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
