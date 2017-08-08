package com.courseratingsystem.app.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.activity.ChangePasswordActivity;
import com.courseratingsystem.app.activity.ChangeProfileActivity;
import com.courseratingsystem.app.activity.LoginActivity;
import com.courseratingsystem.app.activity.MyCommentsActivity;
import com.courseratingsystem.app.activity.MyFavoritesActivity;
import com.courseratingsystem.app.application.MyCourseApplication;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_user)
public class UserFragment extends Fragment implements View.OnClickListener {

    @ViewInject(R.id.fragment_user_info_first_relative_layout)
    private RelativeLayout mFirstInfoLayout;
    @ViewInject(R.id.fragment_user_info_second_relative_layout)
    private RelativeLayout mSecondInfoLayout;
    @ViewInject(R.id.fragment_user_text_nickname)
    private TextView mNickname;
    @ViewInject(R.id.fragment_user_text_grade)
    private TextView mGrade;
    @ViewInject(R.id.fragment_user_text_username)
    private TextView mUsername;
    @ViewInject(R.id.fragment_user_text_wechat)
    private TextView mWeChat;
    @ViewInject(R.id.fragment_user_text_intro)
    private TextView mIntro;
    @ViewInject(R.id.fragment_user_layout_myFavorite)
    private LinearLayout mMyFavorite;
    @ViewInject(R.id.fragment_user_layout_myComments)
    private LinearLayout mMyComments;
    @ViewInject(R.id.fragment_user_layout_changeProfile)
    private LinearLayout mChangeProfile;
    @ViewInject(R.id.fragment_user_layout_changePass)
    private LinearLayout mChangPass;
    @ViewInject(R.id.fragment_user_button_logout)
    private Button mLogout;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = x.view().inject(this, inflater, container);
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        MyCourseApplication application = (MyCourseApplication) getActivity().getApplication();
        Integer userid = application.getUserId();
        if (userid == null) {
            //未登录
            setLogoutState();
        } else {
            //已登录
            setLoginState();
            //TODO:根据userid从网络获取信息并显示
        }
    }

    //未登录时点击的监听器
    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), getString(R.string.fragment_user_please_login), Toast.LENGTH_LONG).show();
    }

    private void setLogoutState() {
        mNickname.setText(getString(R.string.fragment_user_unloggedin));
        mGrade.setText("");
        mUsername.setText(getString(R.string.fragment_user_click_to_login));
        mFirstInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mSecondInfoLayout.setVisibility(View.GONE);
        mLogout.setVisibility(View.INVISIBLE);
        mMyFavorite.setOnClickListener(this);
        mMyComments.setOnClickListener(this);
        mChangeProfile.setOnClickListener(this);
        mChangPass.setOnClickListener(this);
    }

    private void setLoginState() {
        mFirstInfoLayout.setOnClickListener(null);
        mSecondInfoLayout.setVisibility(View.VISIBLE);
        mLogout.setVisibility(View.VISIBLE);
        mMyFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyFavoritesActivity.class);
                startActivity(intent);
            }
        });
        mMyComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCommentsActivity.class);
                startActivity(intent);
            }
        });
        mChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeProfileActivity.class);
                startActivity(intent);
            }
        });
        mChangPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCourseApplication application = (MyCourseApplication) getActivity().getApplication();
                application.logout();
                setLogoutState();
            }
        });
    }
}
