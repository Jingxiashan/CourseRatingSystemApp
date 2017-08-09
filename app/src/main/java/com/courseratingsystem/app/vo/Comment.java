package com.courseratingsystem.app.vo;

import java.io.Serializable;

/**
 * Created by Jingxiashan on 2017/8/8.
 */

public class Comment implements Serializable {
    private int recstar;
    private String username;
    private String timestamp;
    private String content;
    private String coursename;
    private int likecount;

    public Comment(String tcoursename, String tusername, String ttimestamp, String tcontent, int trecstar, String tlikecount) {
        likecount = Integer.parseInt(tlikecount);
        recstar = trecstar;
        content = tcontent;
        timestamp = ttimestamp;
        coursename = tcoursename;
        username = tusername;
    }

    public int getRecstar() {
        return recstar;
    }

    public void setRecstar(int recstar) {
        this.recstar = recstar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
