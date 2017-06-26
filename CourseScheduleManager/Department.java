package com.CourseSchedule.CourseScheduleManager;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by Will on 2017/5/22.
 */
public class Department implements Serializable, Iterable<Course> {
    private static final long serialVersionUID = 8759237404882125761L;
    private String shortName;
    private String name;
    private String faculty;
    private Set<Course> courses;
    private Set<String> courseNumbers;

    public Department(String shortName) {
        this.shortName = shortName;
        this.name = "";
        this.faculty = "";
        courses = new HashSet<Course>();
        courseNumbers = new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addCourseNumber(String number) {
        courseNumbers.add(number);
    }

    public String getShortName() {
        return shortName;
    }

    public Course getCourse(String number) {
        Course temp = new Course(this, number, null);
        for (Course course : courses)
            if (course.sameCourse(temp))
                return course;
        throw new NoSuchElementException("this course does not exist");
    }

    public void changeToSameDepartment(Department department) {
        this.name = department.getName();
        this.faculty = department.faculty;
        Set<Course> temp = new HashSet<>();
        for (Course course : department.getCourses()) {
            for (Course course1 :this.courses) {
                if (course.equals(course1))
                    course1.changeToSameCourse(course);
                else
                    temp.add(course);
            }
        }
        for (Course course : temp)
            this.courses.add(course);
    }

    public Set<String> getCourseNumbers() {
        return courseNumbers;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;

        Department that = (Department) o;

        return shortName != null ? shortName.equals(that.shortName) : that.shortName == null;
    }

    @Override
    public int hashCode() {
        return shortName != null ? shortName.hashCode() : 0;
    }

    @Override
    public Iterator<Course> iterator() {
        return courses.iterator();
    }
}
