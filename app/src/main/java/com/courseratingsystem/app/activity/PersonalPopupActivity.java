package com.courseratingsystem.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.vo.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_personal_popup)
public class PersonalPopupActivity extends AppCompatActivity {

    @ViewInject(R.id.activity_personalpopup_textview_nickname)
    TextView nickName;
    @ViewInject(R.id.activity_personalpopup_textview_wechat)
    TextView wechat;
    @ViewInject(R.id.activity_personalpopup_imgview_photo)
    ImageView photo;
    @ViewInject(R.id.activity_personalpopup_textview_description)
    TextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


        User user = new User();
        nickName.setText(user.getNickname());
        wechat.setText(user.getWeChat());

        description.setText(user.getIntro());
    }
}
