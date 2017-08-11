package com.courseratingsystem.app.vo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private int courseId;
    private String courseName;
    private float recommendationScore, averageRatingsUsefulness, averageRatingsVividness, averageRatingsSpareTimeOccupation, averageRatingsScoring, averageRatingsRollCall;
    private int peopleCount;
    private List<TeacherBrief> teacherList;
    private boolean ifFavorite;
    private List<Comment> commentList;

    public Course() {
    }

    public Course(int courseId, String courseName, float recommendationScore, float averageRatingsUsefulness, float averageRatingsVividness, float averageRatingsSpareTimeOccupation, float averageRatingsScoring, float averageRatingsRollCall, int peopleCount) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.recommendationScore = recommendationScore;
        this.averageRatingsUsefulness = averageRatingsUsefulness;
        this.averageRatingsVividness = averageRatingsVividness;
        this.averageRatingsSpareTimeOccupation = averageRatingsSpareTimeOccupation;
        this.averageRatingsScoring = averageRatingsScoring;
        this.averageRatingsRollCall = averageRatingsRollCall;
        this.peopleCount = peopleCount;
        this.teacherList = new ArrayList<>();
    }

    public Course(int courseId, String courseName, float recommendationScore, float averageRatingsUsefulness, float averageRatingsVividness, float averageRatingsSpareTimeOccupation, float averageRatingsScoring, float averageRatingsRollCall, int peopleCount, List<TeacherBrief> teacherList) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.recommendationScore = recommendationScore;
        this.averageRatingsUsefulness = averageRatingsUsefulness;
        this.averageRatingsVividness = averageRatingsVividness;
        this.averageRatingsSpareTimeOccupation = averageRatingsSpareTimeOccupation;
        this.averageRatingsScoring = averageRatingsScoring;
        this.averageRatingsRollCall = averageRatingsRollCall;
        this.peopleCount = peopleCount;
        this.teacherList = teacherList;
    }

    public Course(JSONObject jsonCourse) throws JSONException {
        this.courseId = jsonCourse.getInt("courseId");
        this.courseName = jsonCourse.getString("courseName");
        this.recommendationScore = (float) jsonCourse.getDouble("recommendationScore");
        this.averageRatingsUsefulness = (float) jsonCourse.getDouble("averageRatingUsefulness");
        this.averageRatingsVividness = (float) jsonCourse.getDouble("averageRatingsVividness");
        this.averageRatingsSpareTimeOccupation = (float) jsonCourse.getDouble("averageRatingsSpareTimeOccupation");
        this.averageRatingsScoring = (float) jsonCourse.getDouble("averageRatingsScoring");
        this.averageRatingsRollCall = (float) jsonCourse.getDouble("averageRatingsRollCall");
        this.peopleCount = jsonCourse.getInt("peopleCount");
        this.ifFavorite = jsonCourse.getBoolean("ifFavorite");
        this.teacherList = new ArrayList<>();
        JSONArray teacherJsonList = jsonCourse.getJSONArray("teacherList");
        for (int j = 0; j < teacherJsonList.length(); j++) {
            JSONObject jsonTeacher = teacherJsonList.getJSONObject(j);
            this.teacherList.add(new TeacherBrief(jsonTeacher.getString("teacherName"), jsonTeacher.getInt("teacherId")));
        }
        this.commentList = new ArrayList<>();
        JSONArray commentJsonArray = jsonCourse.getJSONArray("commentList");
        for (int j = 0; j < commentJsonArray.length(); j++) {
            JSONObject jsonComment = commentJsonArray.getJSONObject(j);
            this.commentList.add(new Comment(jsonComment));
        }
    }

    public static List<Course> parseJsonList(JSONArray courseJsonList) throws JSONException {
        List<Course> courseList = new ArrayList<>();
        //处理courseList
        for (int i = 0; i < courseJsonList.length(); i++) {
            JSONObject jsonCourse = courseJsonList.getJSONObject(i);
            Course tmpCourse = new Course(
                    jsonCourse.getInt("courseId"),
                    jsonCourse.getString("courseName"),
                    (float) jsonCourse.getDouble("recommendationScore"),
                    (float) jsonCourse.getDouble("averageRatingUsefulness"),
                    (float) jsonCourse.getDouble("averageRatingsVividness"),
                    (float) jsonCourse.getDouble("averageRatingsSpareTimeOccupation"),
                    (float) jsonCourse.getDouble("averageRatingsScoring"),
                    (float) jsonCourse.getDouble("averageRatingsRollCall"),
                    jsonCourse.getInt("peopleCount")
            );
            JSONArray teacherJsonList = jsonCourse.getJSONArray("teacherList");
            List<Course.TeacherBrief> teacherList = new ArrayList<>();
            for (int j = 0; j < teacherJsonList.length(); j++) {
                JSONObject jsonTeacher = teacherJsonList.getJSONObject(j);
                teacherList.add(tmpCourse.new TeacherBrief(jsonTeacher.getString("teacherName"), jsonTeacher.getInt("teacherId")));
            }
            tmpCourse.setTeacherList(teacherList);
            courseList.add(tmpCourse);
        }
        return courseList;
    }

    public static List<Course> parseJsonListNoTeacher(JSONArray courseJsonList) throws JSONException {
        List<Course> courseList = new ArrayList<>();
        //处理courseList
        for (int i = 0; i < courseJsonList.length(); i++) {
            JSONObject jsonCourse = courseJsonList.getJSONObject(i);
            Course tmpCourse = new Course(
                    jsonCourse.getInt("courseId"),
                    jsonCourse.getString("courseName"),
                    (float) jsonCourse.getDouble("recommendationScore"),
                    (float) jsonCourse.getDouble("averageRatingUsefulness"),
                    (float) jsonCourse.getDouble("averageRatingsVividness"),
                    (float) jsonCourse.getDouble("averageRatingsSpareTimeOccupation"),
                    (float) jsonCourse.getDouble("averageRatingsScoring"),
                    (float) jsonCourse.getDouble("averageRatingsRollCall"),
                    jsonCourse.getInt("peopleCount")
            );
            courseList.add(tmpCourse);
        }
        return courseList;
    }

    public boolean isIfFavorite() {
        return ifFavorite;
    }

    public void setIfFavorite(boolean ifFavorite) {
        this.ifFavorite = ifFavorite;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public float getRecommendationScore() {
        return recommendationScore;
    }

    public void setRecommendationScore(float recommendationScore) {
        this.recommendationScore = recommendationScore;
    }

    public float getAverageRatingsUsefulness() {
        return averageRatingsUsefulness;
    }

    public void setAverageRatingsUsefulness(float averageRatingsUsefulness) {
        this.averageRatingsUsefulness = averageRatingsUsefulness;
    }

    public float getAverageRatingsVividness() {
        return averageRatingsVividness;
    }

    public void setAverageRatingsVividness(float averageRatingsVividness) {
        this.averageRatingsVividness = averageRatingsVividness;
    }

    public float getAverageRatingsSpareTimeOccupation() {
        return averageRatingsSpareTimeOccupation;
    }

    public void setAverageRatingsSpareTimeOccupation(float averageRatingsSpareTimeOccupation) {
        this.averageRatingsSpareTimeOccupation = averageRatingsSpareTimeOccupation;
    }

    public float getAverageRatingsScoring() {
        return averageRatingsScoring;
    }

    public void setAverageRatingsScoring(float averageRatingsScoring) {
        this.averageRatingsScoring = averageRatingsScoring;
    }

    public float getAverageRatingsRollCall() {
        return averageRatingsRollCall;
    }

    public void setAverageRatingsRollCall(float averageRatingsRollCall) {
        this.averageRatingsRollCall = averageRatingsRollCall;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public List<TeacherBrief> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherBrief> teacherList) {
        this.teacherList = teacherList;
    }

    public class TeacherBrief implements Serializable {
        String teacherName;
        int teacherId;

        public TeacherBrief(String teacherName, int teacherId) {
            this.teacherName = teacherName;
            this.teacherId = teacherId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public int getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(int teacherId) {
            this.teacherId = teacherId;
        }
    }
}