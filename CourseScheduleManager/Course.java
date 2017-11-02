package com.CourseSchedule.CourseScheduleManager;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Will on 2017/5/20.
 */
public class Course implements Serializable, Iterable<Section> {
    private static final long serialVersionUID = -5046968324155930043L;

    private Department department;
    private String courseNumber;
    private String courseName;
    private String description;
    private String credits;
    private String reqs;
    private Set<Section> sections;
    private List<String> sectionsList;

    private Set<Instructor> instructorsWhoOfferThisCourse;
    private Set<Classroom> classrooms;

    public Course(Department department, String courseNumber, String courseName) {
        this.department = department;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.description = "";
        this.credits = "Credits: 3";

        sections = new HashSet<Section>();
        sectionsList = new ArrayList<>();
        instructorsWhoOfferThisCourse = new HashSet<Instructor>();
        classrooms = new HashSet<Classroom>();

        department.addCourse(this);
    }

    public List<String> getSectionsList() {
        return sectionsList;
    }

    public void addSection(String section) {
        sectionsList.add(section);
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public void setReqs(String reqs) {
        this.reqs = reqs;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addSection(Section section) {
        for (Section section1 : sections) {
            if (section1.equals(section)) {
                section1.changeToSameSection(section);
                addInstructorAndClassroom(section);
                return;
            }
        }
        sections.add(section);
        addInstructorAndClassroom(section);
    }

    private void addInstructorAndClassroom(Section section) {
//        try {
            instructorsWhoOfferThisCourse.add(section.getInstructor());
//        } catch (InstructorTBAException ignored) {
//        }
//        try {
            classrooms.add(section.getClassroom());
//        } catch (NoScheduledMeetingException ignored) {
//        }
    }

    public Section getSection(Course course, String name) {
        Section temp = new Section(this, name, null, null, null, null, null);
        for (Section section : sections)
            if (section.equals(temp))
                return section;
        throw new NoSuchElementException();
    }

    public List<String> getSectionsForDisplay() {
        List<String> temp = new ArrayList<>();
        for (Section section : sections) {
            String a = "section!" + section.getSection() + "@"
                    + section.getActivity() + "@" +
                    section.getTerm() + "@";
//            try {
                a += section.getInstructor().getName();
//            } catch (InstructorTBAException e) {
//                a += "Instructor TBA";
//            }
            temp.add(a);
        }
        return temp;
    }

    public boolean sameCourse(Course course) {
        if (course.getDepartment() == this.department
                && course.getCourseNumber().equals(this.courseNumber))
            return true;
        return false;
    }

    public void changeToSameCourse(Course course) {
        this.credits = course.credits;
        this.description = course.description;
        this.courseName = course.courseName;

        for (Section section : course.sections)
            this.sections.add(section);
        for (Instructor instructor : course.instructorsWhoOfferThisCourse)
            this.instructorsWhoOfferThisCourse.add(instructor);
        for (Classroom classroom : course.classrooms)
            this.classrooms.add(classroom);
    }

    public void print() {
        System.out.println("!Course Name: " + department.getShortName() + courseNumber + courseName + "!");
        // TODO
        // when parse info from string, one way is string.split(" ")[i]
        // when i = 0, we get department name
        // when i = 1, we get number
        // ! is used to indicate the end of course name pair
        System.out.println("\t" + "@Credit: " + credits + "@");
        System.out.println("\t" + "#Description: " + description + "#");
        System.out.println("\t" + "$Reqs: " + reqs + "$");
        for (Section section : sections)
            section.print();
    }

    public Department getDepartment() {
        return department;
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

    public Set<Section> getSections() {
        return sections;
    }

    public Set<Instructor> getInstructorsWhoOfferThisCourse() {
        return instructorsWhoOfferThisCourse;
    }

    public Set<Classroom> getClassrooms() {
        return classrooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        Course course = (Course) o;

        if (department != null ? !department.equals(course.department) : course.department != null) return false;
        return courseNumber != null ? courseNumber.equals(course.courseNumber) : course.courseNumber == null;
    }

    @Override
    public int hashCode() {
        int result = department != null ? department.hashCode() : 0;
        result = 31 * result + (courseNumber != null ? courseNumber.hashCode() : 0);
        return result;
    }

    @Override
    public Iterator<Section> iterator() {
        return sections.iterator();
    }
}
