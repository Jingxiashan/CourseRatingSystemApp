package com.courseratingsystem.app.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.fragment.LoginFragment;
import com.mcxtzhang.pathanimlib.PathAnimView;
import com.mcxtzhang.pathanimlib.utils.SvgPathParser;

import java.text.ParseException;

public class LoginActivity extends AppCompatActivity {

    FrameLayout mFrameLayout;
    PathAnimView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFrameLayout = (FrameLayout) findViewById(R.id.activity_login_container);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.activity_login_container, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        mLogo = (PathAnimView) findViewById(R.id.fragment_login_anim_logo);
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
}
