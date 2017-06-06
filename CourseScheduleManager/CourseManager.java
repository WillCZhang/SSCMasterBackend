package com.CourseSchedule.CourseScheduleManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Will on 2017/5/20.
 */
public class CourseManager implements Serializable {
    private Set<Department> departments;
    private static CourseManager instance;
    private static final long serialVersionUID = -3493267914403891417L;

    private CourseManager() {
        departments = new HashSet<Department>();
    }

    public static CourseManager getInstance() {
        // Do not modify the implementation of this method!
        if(instance == null) {
            instance = new CourseManager();
        }
        return instance;
    }

    public void addDepartment(Department department) {
        for (Department department1 : departments){
            if (department.equals(department1)) {
                department1.changeToSameDepartment(department);
                return;
            }
        }
        departments.add(department);
    }

    public void addCourse(String course, String number, String name) {
        Department temp = new Department(course);
        for (Department department : departments) {
            if (department.equals(temp)) {
                Course tempCourse = new Course(department, number, name);
                department.addCourse(tempCourse);
            }
        }
    }

    public Course getCourse(String course, String number) {
        Department temp = new Department(course);
        for (Department department : departments)
            if (department.equals(temp))
                return department.getCourse(number);
        return null;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void print() {
        for (Department department : departments)
            for (Course course : department.getCourses())
                course.print();
    }

    public Course hasTheCourse(String name, String number) {
        Department temp = new Department(name);
        Course tempCourse = new Course(temp, number, null);
        for (Department department : departments)
            if (department.equals(temp))
                for (Course course : department.getCourses())
                    if (course.sameCourse(tempCourse))
                        return course;
        return null;
    }

    public String[] getFaculties() {
        List<String> temp = new ArrayList<>();
        for (Department department : departments) {
            if (!temp.contains(department.getFaculty()))
                temp.add(department.getFaculty());
        }
        String[] faculties = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++)
            faculties[i] = temp.get(i);
        return faculties;
    }

    public static void setInstance(CourseManager courseManager) {
        instance = courseManager;
    }
}
