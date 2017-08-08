package com.courseratingsystem.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.activity.LoginActivity;
import com.courseratingsystem.app.tools.EncyptionHelper;
import com.courseratingsystem.app.view.LoadingAnimView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_login)
public class LoginFragment extends Fragment {
    //TODO:第三方登录

    LoadingAnimView loadingAnimView;

    @ViewInject(R.id.fragment_login_layout)
    private RelativeLayout relativeLayout;
    @ViewInject(R.id.fragment_login_input_username)
    private EditText mUsername;
    @ViewInject(R.id.fragment_login_input_password)
    private EditText mPassword;
    @ViewInject(R.id.fragment_login_button_login)
    private Button mLogin;
    @ViewInject(R.id.fragment_login_button_wechat)
    private Button mWeChat;
    @ViewInject(R.id.fragment_login_button_qq)
    private Button mQQ;
    @ViewInject(R.id.fragment_login_button_weibo)
    private Button mWeibo;
    @ViewInject(R.id.fragment_login_text_warn)
    private TextView mWarning;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginActivity loginActivity = (LoginActivity) getActivity();
            String status = (String) msg.obj;
            mWarning.setText(status);
            mWarning.setVisibility(View.VISIBLE);
            loginActivity.showLoadingAnim(false);
        }
    };
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Event(type = View.OnClickListener.class, value = R.id.fragment_login_button_login)
    private void attemptLogin(View view) {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        //判断空
        if (username.isEmpty()) {
            mWarning.setText(getString(R.string.fragment_login_warn_emptyUsername));
            mWarning.setVisibility(View.VISIBLE);
            return;
        }
        if (password.isEmpty()) {
            mWarning.setText(getString(R.string.fragment_login_warn_emptyPassword));
            mWarning.setVisibility(View.VISIBLE);
            return;
        }
        //加密并调用Activity方法登录
        String passwordEncrypted = EncyptionHelper.shaEncrypt(password);
        ((LoginActivity) getActivity()).attemptLogin(username, passwordEncrypted, handler);

    }

}

