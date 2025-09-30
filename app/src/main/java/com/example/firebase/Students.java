package com.example.firebase;

public class Students {
    private int gradeClass;
    private int stuClass;
    private String stuName;
    private String stuID;

    // Constructor with parameters
    public Students(int gradeClass, int stuClass, String stuName, String stuID) {
        this.gradeClass = gradeClass;
        this.stuClass = stuClass;
        this.stuName = stuName;
        this.stuID = stuID;
    }

    // Default constructor
    public Students() {}

    // Getters and setters
    public int getGradeClass() {
        return gradeClass;
    }

    public void setGradeClass(int gradeClass) {
        this.gradeClass = gradeClass;
    }

    public int getStuClass() {
        return stuClass;
    }

    public void setStuClass(int stuClass) {
        this.stuClass = stuClass;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }
}