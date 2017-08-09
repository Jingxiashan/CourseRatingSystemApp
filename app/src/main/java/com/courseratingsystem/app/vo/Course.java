package com.courseratingsystem.app.vo;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {
    private int courseId;
    private String courseName;
    private float recommendationScore, averageRatingsUsefulness, averageRatingsVividness, averageRatingsSpareTimeOccupation, averageRatingsScoring, averageRatingsRollCall;
    private int peopleCount;
    private List<TeacherBrief> teacherList;

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
        int teacherid;

        public TeacherBrief(String teacherName, int teacherid) {
            this.teacherName = teacherName;
            this.teacherid = teacherid;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public int getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(int teacherid) {
            this.teacherid = teacherid;
        }
    }
}