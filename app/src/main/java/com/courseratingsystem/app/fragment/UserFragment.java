package com.courseratingsystem.app.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.courseratingsystem.app.view.LoadingAnimView;
import com.courseratingsystem.app.vo.User;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.fragment_user)
public class UserFragment extends Fragment implements View.OnClickListener {

    //网络相关
    private static final int LOAD_SUCCESSFULLY = 0;
    private static final int LOAD_FAILED = 1;
    private final String GET_USER_URL = "/getUser?userid=";
    LoadingAnimView mLoadingAnimView;
    @ViewInject(R.id.fragment_user_layout)
    private RelativeLayout mRelativeLayout;
    @ViewInject(R.id.fragment_user_info_first_relative_layout)
    private RelativeLayout mFirstInfoLayout;
    @ViewInject(R.id.fragment_user_info_second_relative_layout)
    private RelativeLayout mSecondInfoLayout;
    @ViewInject(R.id.fragment_user_image_person_icon)
    private ImageView mAvatar;
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
    private final Handler getInfoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_SUCCESSFULLY:
                    //加载成功，先加载数据
                    setLoginState((User) msg.obj);
                    //后停止动画
                    showLoadingAnim(false);
                    break;
                case LOAD_FAILED:
                    //TODO:加载失败，显示加载失败的界面
                    break;
            }
            return false;
        }
    });

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyCourseApplication application = (MyCourseApplication) getActivity().getApplication();
        if (!application.isLoggenIn()) {
            //未登录
            setLogoutState();
        } else {
            getData();
        }
    }

    private void getData() {
        //显示加载
        showLoadingAnim(true);
        //联网
        //TODO:加载头像
        MyCourseApplication application = (MyCourseApplication) getActivity().getApplication();
        final String username = application.getUsername();
        OkHttpClient client = application.getOkHttpClient();
        Request request = new Request.Builder()
                .url(MyCourseApplication.SERVER_URL + GET_USER_URL + application.getUserId())
                .build();
        client.newCall(request).enqueue(new Callback() {
            Message msg = new Message();

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                msg.what = LOAD_FAILED;
                getInfoHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseJson = new JSONObject(responseBody);
                        int statusCode = responseJson.getInt(MyCourseApplication.JSON_RESULT_CODE);
                        if (statusCode == MyCourseApplication.JSON_RESULT_CODE_200) {
                            //成功
                            JSONObject resultJson = responseJson.getJSONObject(MyCourseApplication.JSON_RESULT);


                            msg.obj = new User(
                                    username,
                                    resultJson.getString("nickname"),
                                    resultJson.getString("grade"),
                                    resultJson.getString("wechat"),
                                    resultJson.getString("intro"),
                                    resultJson.getString("picpath")
                            );
                            msg.what = LOAD_SUCCESSFULLY;
                            getInfoHandler.sendMessage(msg);
                        } else {
                            msg.what = LOAD_FAILED;
                            msg.obj = responseJson.getString(MyCourseApplication.JSON_REASON);
                            getInfoHandler.sendMessage(msg);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //显示加载动画
    private void showLoadingAnim(boolean ifShow) {
        if (ifShow) {
            if (mLoadingAnimView == null) {
                mLoadingAnimView = new LoadingAnimView(getActivity(), LoadingAnimView.BgColor.LIGHT);
                mRelativeLayout.addView(mLoadingAnimView);
            }

        } else {
            mRelativeLayout.removeView(mLoadingAnimView);
            mLoadingAnimView = null;
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

    private void setLoginState(final User user) {
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
                intent.putExtra("user", user);
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

        //设置显示内容
        //TODO:显示头像
//        mAvatar.setimage
        mNickname.setText(user.getNickname());
        mUsername.setText(user.getUsername());
        mGrade.setText(user.getGrade());
        mWeChat.setText(user.getWeChat() != null
                ? String.format(getString(R.string.fragment_user_wechat), user.getWeChat())
                : String.format(getString(R.string.fragment_user_wechat), getString(R.string.fragment_user_profile_undefined))
        );
        mIntro.setText(user.getIntro() != null
                ? String.format(getString(R.string.fragment_user_intro), user.getIntro())
                : String.format(getString(R.string.fragment_user_intro), getString(R.string.fragment_user_profile_undefined))
        );
    }
}
