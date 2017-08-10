package com.courseratingsystem.app.vo;

import java.io.Serializable;

/**
 * Created by kongx on 2017/8/9 0009.
 */

public class User implements Serializable {
    private String userid;
    private String username, nickname;
    private String grade;
    private String weChat, intro;
    private String picpath;

    public User() {
    }

    public User(String username, String nickname, String grade, String weChat, String intro, String picpath) {
        this.username = username;
        this.nickname = nickname;
        this.grade = grade;
        this.weChat = weChat;
        this.intro = intro;
        this.picpath = picpath;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }
}
