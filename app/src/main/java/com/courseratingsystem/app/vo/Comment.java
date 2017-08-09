package com.courseratingsystem.app.vo;

import java.io.Serializable;

/**
 * Created by Jingxiashan on 2017/8/8.
 */

public class Comment implements Serializable {
    private int recstar;
    private int commentid;
    private int userid;
    private String nickname;
    private String timestamp;
    private String content;
    private String coursename;
    private int likecount;

    public Comment(int recstar, int commentid, int userid, String nickname, String timestamp, String content, String coursename, int likecount) {
        this.recstar = recstar;
        this.commentid = commentid;
        this.userid = userid;
        this.nickname = nickname;
        this.timestamp = timestamp;
        this.content = content;
        this.coursename = coursename;
        this.likecount = likecount;
    }

    public int getRecstar() {
        return recstar;

    }

    public void setRecstar(int recstar) {
        this.recstar = recstar;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

}
