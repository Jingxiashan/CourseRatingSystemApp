package com.courseratingsystem.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.tools.PhotoProgress;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

@ContentView(R.layout.activity_change_profile)
public class ChangeProfileActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_change_profile_grade)
    private Spinner spinner;
    @ViewInject(R.id.activity_change_profile_personal_icon)
    private ImageView personal_icon;

    @ViewInject(R.id.activity_change_profile_nickname_textview)
    private TextView nickname_textview;
    @ViewInject(R.id.activity_change_profile_nickname)
    private EditText nickname;
    @ViewInject(R.id.activity_change_profile_wechatAccount_textview)
    private TextView wechatAccount_textview;
    @ViewInject(R.id.activity_change_profile_wechatAccount)
    private EditText wechatAccount;
    @ViewInject(R.id.activity_change_profile_introduction_textview)
    private TextView introduction_textview;
    @ViewInject(R.id.activity_change_profile_introduction)
    private EditText introduction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("修改个人信息");

        //设置年级spinner
        final String[] grade = getResources().getStringArray(R.array.grade);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                ChangeProfileActivity.this,android.R.layout.simple_spinner_item,grade
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(5);//根据用户实际情况修改
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ChangeProfileActivity.this,
                        "你点击的是:"+grade[position],Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置用户头像
        personal_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    nickname_textview.setTextColor(getResources().getColor(R.color.myblue));
                else
                    nickname_textview.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        wechatAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    wechatAccount_textview.setTextColor(getResources().getColor(R.color.myblue));
                else
                    wechatAccount_textview.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        introduction.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    introduction_textview.setTextColor(getResources().getColor(R.color.myblue));
                else
                    introduction_textview.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
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


    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static Uri tempUri;

    //显示修改头像的对话框
    protected void showChoosePicDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
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
            personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap){
        //上传至服务器
        //……可以吧Bitmap转换为file，然后得到file的url，做文件上传操作
        //注意这里得到的图片已经是圆形图片了
        //bitmap是没有做过圆形处理的，但已经被裁剪了

        String imagePath = PhotoProgress.savePhoto(
                bitmap,Environment.getExternalStorageDirectory().getAbsolutePath(),
                String.valueOf(System.currentTimeMillis()));
        Log.e("imagePath",imagePath+"");
        if(imagePath != null){
            //拿着imagePath上传
            //……
        }
    }
}
