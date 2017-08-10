package com.courseratingsystem.app.vo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jingxiashan on 2017/8/8.
 */

public class Comment implements Serializable {
    private float recstar;
    private int commentid;
    private int userid;
    private String nickname;
    private String timestamp;
    private String content;
    private String coursename;
    private int courseid;
    private int likecount;
    private int teacherid;
    private String teachername;
    private float usefulness;
    private float sparetimeoccupation;
    private float vivdness;
    private float rollcall;
    private float scorehigh;

    public Comment(float recstar, int commentid, int userid, String nickname, String timestamp, String content, String coursename, int courseid, int likecount, int teacherid, String teachername, float usefulness, float sparetimeoccupation, float vivdness, float rollcall, float scorehigh) {
        this.recstar = recstar;
        this.commentid = commentid;
        this.userid = userid;
        this.nickname = nickname;
        this.timestamp = timestamp;
        this.content = content;
        this.coursename = coursename;
        this.courseid = courseid;
        this.likecount = likecount;
        this.teacherid = teacherid;
        this.teachername = teachername;
        this.usefulness = usefulness;
        this.sparetimeoccupation = sparetimeoccupation;
        this.vivdness = vivdness;
        this.rollcall = rollcall;
        this.scorehigh = scorehigh;
    }

    public Comment() {
    }

    public Comment(float recstar, int commentid, int userid, String nickname, String timestamp, String content, String coursename, int likecount) {
        this.recstar = recstar;
        this.commentid = commentid;
        this.userid = userid;
        this.nickname = nickname;
        this.timestamp = timestamp;
        this.content = content;
        this.coursename = coursename;
        this.likecount = likecount;
    }

    public static List<Comment> parseJsonList(JSONArray commentJsonList) throws JSONException {
        List<Comment> commentList = new ArrayList<>();
        //处理courseList
        for (int i = 0; i < commentJsonList.length(); i++) {
            JSONObject jsonComment = commentJsonList.getJSONObject(i);
            Comment tmpComment = new Comment(
                    (float) jsonComment.getDouble("recommandScore"),
                    jsonComment.getInt("commentId"),
                    jsonComment.getInt("userId"),
                    jsonComment.getString("nickName"),
                    jsonComment.getString("timeStamp"),
                    jsonComment.getString("critics"),
                    jsonComment.getString("courseName"),
                    jsonComment.getInt("courseId"),
                    jsonComment.getInt("likeCount"),
                    jsonComment.getInt("teacherId"),
                    jsonComment.getString("teacherName"),
                    (float) jsonComment.getDouble("ratingUsefulness"),
                    (float) jsonComment.getDouble("ratingSpareTimeOccupation"),
                    (float) jsonComment.getDouble("ratingVividness"),
                    (float) jsonComment.getDouble("ratingRollCall"),
                    (float) jsonComment.getDouble("ratingScoring")
            );
            commentList.add(tmpComment);
        }
        return commentList;
    }

    public int getCourseid() {
        return courseid;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    public int getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public float getUsefulness() {
        return usefulness;
    }

    public void setUsefulness(float usefulness) {
        this.usefulness = usefulness;
    }

    public float getSparetimeoccupation() {
        return sparetimeoccupation;
    }

    public void setSparetimeoccupation(float sparetimeoccupation) {
        this.sparetimeoccupation = sparetimeoccupation;
    }

    public float getVivdness() {
        return vivdness;
    }

    public void setVivdness(float vivdness) {
        this.vivdness = vivdness;
    }

    public float getRollcall() {
        return rollcall;
    }

    public void setRollcall(float rollcall) {
        this.rollcall = rollcall;
    }

    public float getScorehigh() {
        return scorehigh;
    }

    public void setScorehigh(float scorehigh) {
        this.scorehigh = scorehigh;
    }

    public float getRecstar() {
        return recstar;

    }

    public void setRecstar(float recstar) {
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
