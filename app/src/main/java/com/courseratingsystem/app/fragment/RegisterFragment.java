package com.courseratingsystem.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.activity.LoginActivity;
import com.courseratingsystem.app.tools.EncyptionHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_register)
public class RegisterFragment extends Fragment {

    @ViewInject(R.id.fragment_register_input_username)
    private EditText mUsername;
    ;
    @ViewInject(R.id.fragment_register_input_password1)
    private EditText mPassword1;
    @ViewInject(R.id.fragment_register_input_password2)
    private EditText mPassword2;
    @ViewInject(R.id.fragment_register_input_nickname)
    private EditText mNickname;
    @ViewInject(R.id.fragment_register_input_grade)
    private Spinner mGrade;
    @ViewInject(R.id.fragment_register_button_register)
    private Button mRegister;
    @ViewInject(R.id.fragment_register_text_warn)
    private TextView mWarning;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Event(type = View.OnClickListener.class, value = R.id.fragment_register_button_register)
    private void attemptRegister(View view) {
        String username = mUsername.getText().toString();
        String password1 = mPassword1.getText().toString();
        String password2 = mPassword2.getText().toString();
        String nickname = mNickname.getText().toString();
        String grade = mGrade.getSelectedItem().toString();
        if (grade.length() > 4) {
            grade = grade.substring(0, 4);
        }
        //基本检查
        if (username.isEmpty()) {
            mWarning.setText(getString(R.string.fragment_register_warn_emptyUsername));
            mWarning.setVisibility(View.VISIBLE);
            return;
        }
        if (password1.isEmpty() || password2.isEmpty()) {
            mWarning.setText(getString(R.string.fragment_register_warn_emptyPassword));
            mWarning.setVisibility(View.VISIBLE);
            return;
        }
        if (nickname.isEmpty()) {
            mWarning.setText(getString(R.string.fragment_register_warn_emptyNickname));
            mWarning.setVisibility(View.VISIBLE);
            return;
        }
        if (!password1.equals(password2)) {
            mWarning.setText(getString(R.string.fragment_register_warn_diffPasswords));
            mWarning.setVisibility(View.VISIBLE);
            return;
        }
        if (username.length() < 4) {
            mWarning.setText(getString(R.string.fragment_register_warn_shortUsername));
            mWarning.setVisibility(View.VISIBLE);
            return;
        }
        if (password1.length() < 6) {
            mWarning.setText(getString(R.string.fragment_register_warn_shortPassword));
            mWarning.setVisibility(View.VISIBLE);
            return;
        }
        //加密密码调用Activity联网注册
        String passwordEncrypted = EncyptionHelper.shaEncrypt(password1);
        RegisterStatus status = ((LoginActivity) getActivity()).attemptRegister(username, passwordEncrypted, nickname, grade);
        switch (status) {
            case REGISTER_SUCCESFULLLY:
                mWarning.setVisibility(View.GONE);
                break;
            case DUPLICATE_USERNAME:
                mWarning.setText(getString(R.string.fragment_register_warn_duplicateUsername));
                mWarning.setVisibility(View.VISIBLE);
                break;
        }

    }

    public enum RegisterStatus {REGISTER_SUCCESFULLLY, DUPLICATE_USERNAME}
}
