package com.courseratingsystem.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.tools.PhotoProgress;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_change_password)
public class ChangePasswordActivity extends AppCompatActivity {

    private MyCourseApplication myCourseApplication;
    private Integer userId;

    @ViewInject(R.id.activity_change_password_oldPassword)
    private EditText oldPassword_text;
    @ViewInject(R.id.activity_change_password_newPassword)
    private EditText newPassword_text;
    @ViewInject(R.id.activity_change_password_secondInputNewPassword)
    private EditText secondInputNewPassword_text;
    @ViewInject(R.id.activity_change_password_oldPassword_textInputLayout)
    private TextInputLayout oldPassword_textInputLayout;
    @ViewInject(R.id.activity_change_password_newPassword_textInputLayout)
    private TextInputLayout newPassword_textInputLayout;
    @ViewInject(R.id.activity_change_password_secondInputnewPassword_textInputLayout)
    private TextInputLayout secondInputNewPassword_textInputLayout;

    @ViewInject(R.id.activity_change_password_personal_image)
    private ImageView personal_image;

    private static final int WITHOUT_INTERNET = 0;
    private static final int GET_IMAGE_IS_SUCCESS = 1;
    private static final int GET_IMAGE_IS_FAIL = 2;
    private static final int CHANGE_PASSWORD_IS_SUCCESS = 3;
    private static final int CHANGE_PASSWORD_IS_FAIL = 4;

    OkHttpClient okHttpClient = new OkHttpClient();
    //Handler 主线程创建---消息的处理主线程
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WITHOUT_INTERNET:
                    String result_without_internet = (String)msg.obj;
                    Toast.makeText(ChangePasswordActivity.this,
                            "网络异常！请稍后再试",Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_PASSWORD_IS_SUCCESS:
                    String result_change_password_is_success = (String)msg.obj;
                    Toast.makeText(ChangePasswordActivity.this,
                            "密码修改成功！",Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_PASSWORD_IS_FAIL:
                    Toast.makeText(ChangePasswordActivity.this,
                            "密码修改失败！",Toast.LENGTH_SHORT).show();
                    break;
                case GET_IMAGE_IS_SUCCESS:
                    byte[] result_get_image = (byte[])msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(result_get_image,0,result_get_image.length);
                    bitmap = PhotoProgress.toRoundBitmap(bitmap,null);//这个时候图片被处理成圆形
                    personal_image.setImageBitmap(bitmap);
                    break;
                case GET_IMAGE_IS_FAIL:
                    Toast.makeText(ChangePasswordActivity.this,
                            "个人头像下载失败！",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("修改个人密码");

        myCourseApplication=(MyCourseApplication)getApplication();
        userId =myCourseApplication.getUserId();

        //获得用户头像的url
        String url_image = new String("http://192.168.40.51:8080/CourseRatingSystem/images/stevie.jpg");
        Request request_image = new Request.Builder()
                .url(url_image)
                .get()
                .build();
        exec_getImage(request_image);

        newPassword_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(newPassword_textInputLayout.getEditText().getText().toString().length()<6)
                    newPassword_textInputLayout.setError("密码太短啦！！这么不安全的密码都不敢给你存！");
                else
                    newPassword_textInputLayout.setError("哇！密码通过啦！");
            }
        });

        secondInputNewPassword_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(newPassword_textInputLayout.getEditText().getText().toString().length()>=6){
                    if(!secondInputNewPassword_text.getText().toString().equals(
                            newPassword_text.getText().toString()
                    ))
                        secondInputNewPassword_textInputLayout.setError("手残嘛！两次密码都输得不一样！");
                    else
                        secondInputNewPassword_textInputLayout.setError("哇！密码通过啦！");
                }
                else
                    newPassword_textInputLayout.setError("密码太短啦！！这么不安全的密码都不敢给你存！");
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
        String oldpassword_text = oldPassword_text.getText().toString();
        String newpassword_text = newPassword_text.getText().toString();
        String secondinputpassword_text = secondInputNewPassword_text.getText().toString();

        if(newpassword_text.length()<6){
            //新密码太短
            Toast.makeText(ChangePasswordActivity.this,
                    "密码太短啦！！这么不安全的密码都不敢给你存！",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if(secondinputpassword_text.equals(newpassword_text)){
                //向后台发信息，更改密码
                //修改用户密码的url
                String url = new String("http://www.baidu.com");
                FormBody.Builder build = new FormBody.Builder();
                FormBody formBody = build
                        .add("oldPassword",oldpassword_text)
                        .add("newPassword",newpassword_text)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(url)
                        .post(formBody)
                        .build();
                exec_changePassword(request);
            }
            else{
                //两次新密码输入不一致
                Toast.makeText(ChangePasswordActivity.this,
                        "手残嘛！两次密码都输得不一样！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    //获得用户头像返回信息处理
    private void exec_getImage(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("无网络连接","--->"+e);
                Message message = new Message();
                message.what = WITHOUT_INTERNET;
                message.obj = e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = handler.obtainMessage();
                if(response.isSuccessful()){
                    message.what = GET_IMAGE_IS_SUCCESS;
                    message.obj = response.body().bytes();
                    Log.i("下载个人头像成功","--->"+message.obj);
                    handler.sendMessage(message);
                }else{
                    Log.i("下载个人头像失败","--->");
                    handler.sendEmptyMessage(GET_IMAGE_IS_FAIL);
                }
            }
        });
    }

    //修改个人密码返回信息处理
    private void exec_changePassword(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("无网络连接","--->"+e);
                Message message = new Message();
                message.what = WITHOUT_INTERNET;
                message.obj = e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = handler.obtainMessage();
                if(response.isSuccessful()){
                    message.what = CHANGE_PASSWORD_IS_SUCCESS;
                    message.obj = response.body().string();
                    Log.i("修改个人密码成功","--->"+message.obj);
                    handler.sendMessage(message);
                }else{
                    Log.i("修改个人密码失败","--->");
                    handler.sendEmptyMessage(CHANGE_PASSWORD_IS_FAIL);
                }
            }
        });
    }
}
