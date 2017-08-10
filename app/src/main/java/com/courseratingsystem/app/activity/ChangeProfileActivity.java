package com.courseratingsystem.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.application.MyCourseApplication;
import com.courseratingsystem.app.tools.PhotoProgress;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ContentView(R.layout.activity_change_profile)
public class ChangeProfileActivity extends AppCompatActivity {

    private MyCourseApplication myCourseApplication;
    private Integer userId;
    private String[] old_information_strings = null;

    @ViewInject(R.id.activity_change_profile_grade)
    private Spinner grade_spinner;
    @ViewInject(R.id.activity_change_profile_personal_image)
    private ImageView personal_image;

    @ViewInject(R.id.activity_change_profile_nickname_textview)
    private TextView nickname_textview;
    @ViewInject(R.id.activity_change_profile_nickname)
    private EditText nickname_edit;
    @ViewInject(R.id.activity_change_profile_wechatAccount_textview)
    private TextView wechatAccount_textview;
    @ViewInject(R.id.activity_change_profile_wechatAccount)
    private EditText wechatAccount_edit;
    @ViewInject(R.id.activity_change_profile_introduction_textview)
    private TextView introduction_textview;
    @ViewInject(R.id.activity_change_profile_introduction)
    private EditText introduction_edit;

    private static final int WITHOUT_INTERNET = 0;
    private static final int GET_IMAGE_IS_SUCCESS = 1;
    private static final int GET_IMAGE_IS_FAIL = 2;
    private static final int GET_INFORMATION_IS_SUCCESS = 3;
    private static final int GET_INFORMATION_IS_FAIL = 4;
    private static final int CHANGE_IMAGE_IS_SUCCESS = 5;
    private static final int CHANGE_IMAGE_IS_FAIL = 6;
    private static final int CHANGE_INFORMATION_IS_SUCCESS = 7;
    private static final int CHANGE_INFORMATION_IS_FAIL = 8;

    OkHttpClient okHttpClient = new OkHttpClient();
    //Handler 主线程创建---消息的处理主线程
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WITHOUT_INTERNET:
                    String result_without_internet = (String)msg.obj;
                    Toast.makeText(ChangeProfileActivity.this,
                            "网络异常！请稍后再试",Toast.LENGTH_SHORT).show();
                    break;
                case GET_IMAGE_IS_SUCCESS:
                    byte[] result_get_image = (byte[])msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(result_get_image,0,result_get_image.length);
                    bitmap = PhotoProgress.toRoundBitmap(bitmap,tempUri);//这个时候图片被处理成圆形
                    personal_image.setImageBitmap(bitmap);
                    break;
                case GET_IMAGE_IS_FAIL:
                    Toast.makeText(ChangeProfileActivity.this,
                            "个人头像下载失败！",Toast.LENGTH_SHORT).show();
                    break;
                case GET_INFORMATION_IS_SUCCESS:
                    String result_get_information = (String)msg.obj;
                    setText(result_get_information);
                    break;
                case GET_INFORMATION_IS_FAIL:
                    Toast.makeText(ChangeProfileActivity.this,
                            "个人信息获取失败！",Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_IMAGE_IS_SUCCESS:
                    String result_change_image = (String)msg.obj;
                    Toast.makeText(ChangeProfileActivity.this,
                            "修改个人头像成功！",Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_IMAGE_IS_FAIL:
                    Toast.makeText(ChangeProfileActivity.this,
                            "修改个人头像失败！",Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_INFORMATION_IS_SUCCESS:
                    String result = (String)msg.obj;
                    Toast.makeText(ChangeProfileActivity.this,
                            "修改个人信息成功！",Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_INFORMATION_IS_FAIL:
                    Toast.makeText(ChangeProfileActivity.this,
                            "修改个人信息失败！",Toast.LENGTH_SHORT).show();
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
        getSupportActionBar().setTitle("修改个人信息");

        //输入框获得焦点时，提示信息颜色改变
        nickname_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    nickname_textview.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                else
                    nickname_textview.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        wechatAccount_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    wechatAccount_textview.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                else
                    wechatAccount_textview.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        introduction_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    introduction_textview.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                else
                    introduction_textview.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });

        //修改用户头像
        personal_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        //设置年级spinner
        final String[] grade = getResources().getStringArray(R.array.grade);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                ChangeProfileActivity.this,android.R.layout.simple_spinner_item,grade);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade_spinner.setAdapter(arrayAdapter);
        grade_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        myCourseApplication=(MyCourseApplication)getApplication();
        userId =myCourseApplication.getUserId();

        String url_image = new String("http://192.168.40.51:8080/CourseRatingSystem/images/stevie.jpg");//获得用户头像的url
        Request request_image = new Request.Builder()
                .url(url_image)
                .get()
                .build();
        exec_getImage(request_image);

        String url = new String("http://www.baidu.com");//获得用户信息的url
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        exec_getInformation(request);
    }

    //根据得到的用户信息填写默认信息
    private void setText(String result_get_information){
        if(true)
            return;
        //获得个人信息成功，填写……
        //将结果转换为JSON类型
        try{
            JSONObject responseJson = new JSONObject(result_get_information);
            old_information_strings[0] = responseJson.getString("nickname");
            old_information_strings[1] = responseJson.getString("wechatAccount");
            old_information_strings[2] = responseJson.getString("grade");
            old_information_strings[3] = responseJson.getString("introduction");
        }catch (JSONException e){
            e.printStackTrace();
        }

        nickname_edit.setText(old_information_strings[0]);
        wechatAccount_edit.setText(old_information_strings[1]);
        introduction_edit.setText(old_information_strings[3]);

        String user_grade = old_information_strings[2];
        int user_grade_int = Integer.parseInt(user_grade);
        grade_spinner.setSelection(user_grade_int-2010);
    }

    //点击修改按钮，提交事件处理
    @Event(value = {R.id.activity_change_profile_submit})
    private void doEvent(View view){
        if(nickname_edit.getText().toString().equals("")){
            Toast.makeText(ChangeProfileActivity.this,
                    "昵称不能为空哦",Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new String("http://www.baidu.com");//上传用户信息的url
        FormBody.Builder build = new FormBody.Builder();
        FormBody formBody = build
                .add("nickname",nickname_edit.getText().toString())
                .add("wechatAccount",wechatAccount_edit.getText().toString())
                .add("grade",grade_spinner.getSelectedItem().toString())
                .add("introduction",introduction_edit.getText().toString())
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url)
                .post(formBody)
                .build();
        exec_changeInformation(request);
    }

    //后退导航按钮点击处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //下面是有关头像处理的部分
    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static Uri tempUri;

    //显示修改头像的对话框
    protected void showChoosePicDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改头像");
        String[] items = {"相册","拍照"};
        builder.setNegativeButton("取消",null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case CHOOSE_PICTURE://选择本地图片
                        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent,CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                        //制定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);
                        startActivityForResult(openCameraIntent,TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){//如果返回码是可以用的
            switch (requestCode){
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri);//开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData());
                    break;
                case CROP_SMALL_PICTURE:
                    if(data!=null){
                        setImageToView(data);//让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    //裁剪图片方法实现
    private void startPhotoZoom(Uri uri){
        if(uri == null){
            Log.i("tag","The uri is not exist");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //设置裁剪
        intent.putExtra("crop","true");
        //aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //outputX,outputY是裁剪图片宽高
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,CROP_SMALL_PICTURE);
    }

    //保存裁剪之后的图片数据
    private void setImageToView(Intent data){
        Bundle extras = data.getExtras();
        if(extras != null){
            Bitmap photo = extras.getParcelable("data");
            photo = PhotoProgress.toRoundBitmap(photo,tempUri);//这个时候图片被处理成圆形
            uploadPic(photo);//上传头像
            personal_image.setImageBitmap(photo);
        }
    }

    //上传图片
    private void uploadPic(Bitmap bitmap){
        //上传至服务器
        //……可以把Bitmap转换为file，然后得到file的url，做文件上传操作
        //注意这里得到的图片已经是圆形图片了

        userId =4;
        String userid_string = userId.toString();

        String imagePath = PhotoProgress.savePhoto(
                bitmap,this.getFilesDir().getAbsolutePath(),
                userid_string);
        Log.i("imagePath",imagePath);

        String url_updateImage = new String("http://www.baidu.com");//上传用户头像的url
        File file = new File(imagePath);
        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addFormDataPart("files",null,new MultipartBody.Builder("BbC04y")
                .addPart(Headers.of("Content-Disposition","form-data;filename=\"img.png\""),
                        RequestBody.create(MediaType.parse("image/png"),file))
                .build())
                .build();
        Request request = new Request.Builder()
                .url(url_updateImage)
                .post(body)
                .build();
        exec_changeImage(request);
    }


    //上传头像返回信息处理
    private void exec_changeImage(Request request) {
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
                    message.what = CHANGE_IMAGE_IS_SUCCESS;
                    message.obj = response.body().string();
                    Log.i("修改个人头像成功","--->"+message.obj);
                    handler.sendMessage(message);
                }else {
                    Log.i("修改个人头像失败","--->");
                    handler.sendEmptyMessage(CHANGE_IMAGE_IS_FAIL);
                }
            }
        });
    }

    //获得用户信息返回信息处理
    private void exec_getInformation(Request request) {
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
                    message.what = GET_INFORMATION_IS_SUCCESS;
                    message.obj = response.body().string();
                    Log.i("获得个人信息成功","--->"+message.obj);
                    handler.sendMessage(message);
                }else {
                    Log.i("获得个人信息失败","--->");
                    handler.sendEmptyMessage(GET_INFORMATION_IS_FAIL);
                }
            }
        });
    }

    //修改用户信息返回信息处理
    private void exec_changeInformation(Request request) {
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
                    message.what = CHANGE_INFORMATION_IS_SUCCESS;
                    message.obj = response.body().string();
                    Log.i("修改个人信息成功","--->"+message.obj);
                    handler.sendMessage(message);
                }else {
                    Log.i("修改个人信息失败","--->");
                    handler.sendEmptyMessage(CHANGE_INFORMATION_IS_FAIL);
                }
            }
        });
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

}
