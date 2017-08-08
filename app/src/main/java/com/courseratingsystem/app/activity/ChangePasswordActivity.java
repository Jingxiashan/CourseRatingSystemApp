package com.courseratingsystem.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.courseratingsystem.app.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_change_password)
public class ChangePasswordActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_change_password_oldPassword)
    private EditText oldPassword;
    @ViewInject(R.id.activity_change_password_newPassword)
    private EditText newPassword;
    @ViewInject(R.id.activity_change_password_secondInputNewPassword)
    private EditText secondInputNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("修改个人密码");

        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("info","内容改变之前调用:"+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("info","内容改变,可以去告诉服务器:"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("info","内容改变之后调用:"+s);
            }
        });

        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("info","内容改变之前调用:"+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("info","内容改变,可以去告诉服务器:"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("info","内容改变之后调用:"+s);
            }
        });

        secondInputNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("info","内容改变之前调用:"+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("info","内容改变,可以去告诉服务器:"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("info","内容改变之后调用:"+s);
            }
        });
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

    @Event(value = {R.id.activity_change_password_submit})
    private void doEvent(View view){
        if(oldPassword.getText().equals("000000")){

        }else{
            Toast.makeText(ChangePasswordActivity.this,
                    "密码输入错误！",Toast.LENGTH_SHORT).show();
            return;
        }
    }

}
