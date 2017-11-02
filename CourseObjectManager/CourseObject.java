package com.CourseSchedule.CourseScheduleManager;

import java.util.HashSet;
import java.util.Set;

public class CourseObject {
    private String faculty;
    private String departmentFullName;
    private String departmentShortName;
    private String courseNumber;

    private String courseName;
    private String description;
    private String credits;
    private String reqs;

    private Set<SectionObject> sections;

    public CourseObject(String departmentShortName, String courseNumber) {
        this.departmentShortName = departmentShortName;
        this.courseNumber = courseNumber;

        departmentFullName = "";
        faculty = "";
        courseName = "";
        description = "";
        credits = "";
        reqs = "";
        sections = new HashSet<>();
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public void setReqs(String reqs) {
        this.reqs = reqs;
    }

    public void setSections(Set<SectionObject> sections) {
        this.sections = sections;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getDepartmentShortName() {
        return departmentShortName;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public String getCredits() {
        return credits;
    }

    public String getReqs() {
        return reqs;
    }

    public void setDepartmentFullName(String departmentFullName) {
        this.departmentFullName = departmentFullName;
    }

    public String getDepartmentFullName() {
        return departmentFullName;
    }

    public Set<SectionObject> getSections() {
        return sections;
    }
}
