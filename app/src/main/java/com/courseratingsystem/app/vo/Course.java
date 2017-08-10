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

    public static List<Course> parseJsonList(JSONArray courseJsonList) throws JSONException {
        List<Course> courseList = new ArrayList<>();
        //处理courseList
        for (int i = 0; i < courseJsonList.length(); i++) {
            JSONObject jsonCourse = courseJsonList.getJSONObject(i);
            Course tmpCourse = new Course(
                    jsonCourse.getInt("courseId"),
                    jsonCourse.getString("courseName"),
                    (float) jsonCourse.getDouble("recScore"),
                    (float) jsonCourse.getDouble("useScore"),
                    (float) jsonCourse.getDouble("vivScore"),
                    (float) jsonCourse.getDouble("ocuScore"),
                    (float) jsonCourse.getDouble("scoScore"),
                    (float) jsonCourse.getDouble("rolScore"),
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