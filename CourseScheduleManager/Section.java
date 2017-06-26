package com.CourseSchedule.CourseScheduleManager;

import com.CourseSchedule.CourseScheduleManager.Exceptions.InstructorTBAException;
import com.CourseSchedule.CourseScheduleManager.Exceptions.NoScheduledMeetingException;

import java.io.Serializable;
import java.sql.Time;
import java.util.*;

/**
 * Created by Will on 2017/5/22.
 */
public class Section implements Serializable {
    private Course course;
    private String section, status, activity;
    private Set<String> days;
    private Instructor instructor;
    private Classroom classroom;
    private Time start, end;
    private String term;
    private Map<String, List<Time>> timeMap;
    private int totalSeats, currentRegistered, restrictSeats, generalSeats;
    private String restrictTo;
    private String lastWithdraw;

    public Section(Course course, String section, String status, String activity,
                   Instructor instructorName, Classroom classroom,
                   String term) {
        this.activity = activity;
        this.section = section;
        this.course = course;
        this.status = status;
        this.instructor = instructorName;
        this.classroom = classroom;
        this.term = term;
        this.restrictTo = "";
        timeMap = new HashMap<String, List<Time>>();
        days = new HashSet<>();

        course.addSection(this);
    }

    public void setInstructor(Instructor instructor) {
        instructor.addSection(this);
        Instructor instructor1 = InstructorManager.getInstance().getInstructor(instructor);
        this.instructor = instructor1;
        course.addSection(this);
    }

    public void setClassroom(Classroom classroom) {
        BuildingManager.getInstance().getBuilding(classroom.getBuildingThatThisClassroomAt());
        this.classroom = classroom;
    }

    public void addTime(String days, Time start, Time end) {
        this.days.add(days);

        List<Time> times = timeMap.get(days);
        if (times != null) {
            times.add(start);
            times.add(end);
        } else {
            List<Time> temp = new ArrayList<>();
            temp.add(start);
            temp.add(end);
            timeMap.put(days,temp);
        }
    }

    public void setSeatsInfo(int totalSeats, int currentRegistered, int restrictSeats, int generalSeats) {
        this.totalSeats = totalSeats;
        this.currentRegistered = currentRegistered;
        this.restrictSeats = restrictSeats;
        this.generalSeats = generalSeats;
    }

    public void setRestrictTo(String restrictTo) {
        this.restrictTo = restrictTo;
    }

    public void setLastWithdraw(String lastWithdraw) {
        this.lastWithdraw = lastWithdraw;
    }

    public void changeToSameSection(Section section) {
        this.status = section.getStatus();
        this.activity = section.getActivity();
        this.term = section.getTerm();
        this.totalSeats = section.totalSeats;
        this.currentRegistered = section.currentRegistered;
        this.restrictSeats = section.restrictSeats;
        this.generalSeats = section.generalSeats;
        this.restrictTo = section.restrictTo;
        this.lastWithdraw = section.lastWithdraw;
        try {
            this.days = section.getDays();
            this.classroom = section.getClassroom();
            this.start = section.getStart();
            this.end = section.getEnd();
            this.timeMap = section.getTimeMap();
        } catch (NoScheduledMeetingException ignored) {

        }

        try {
            this.instructor = section.getInstructor();
        } catch (InstructorTBAException ignored) {

        }
    }

    public String getSection() {
        return section;
    }

    public Course getCourse() {
        return course;
    }

    public String getStatus() {
        return status;
    }

    public String getActivity() {
        return activity;
    }

    public Set<String> getDays() throws NoScheduledMeetingException {
        if (timeMap.size() == 0)
            throw new NoScheduledMeetingException();
        return days;
    }

    public Time getStart() {
        return start;
    }

    public Time getEnd() {
        return end;
    }

    public String getTerm() {
        return term;
    }

    public Map<String, List<Time>> getTimeMap() {
        return timeMap;
    }

    public Classroom getClassroom() throws NoScheduledMeetingException {
        if (classroom == null)
            throw new NoScheduledMeetingException();
        return classroom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;

        Section section1 = (Section) o;

        if (course != null ? !course.equals(section1.course) : section1.course != null) return false;
        return section != null ? section.equals(section1.section) : section1.section == null;
    }

    @Override
    public int hashCode() {
        int result = course != null ? course.hashCode() : 0;
        result = 31 * result + (section != null ? section.hashCode() : 0);
        return result;
    }

    public Instructor getInstructor() throws InstructorTBAException {
        if (instructor == null)
            throw new InstructorTBAException();
        return instructor;
    }

    public void print() {
        System.out.println("\t ^Section: " + section + " " + status + " " + activity + " " + term + "^");
        if (instructor != null)
            System.out.println("\t %Instructor: " + instructor.getName() + "%");
        if (days != null) {
            for (Map.Entry<String, List<Time>> entry : timeMap.entrySet()) {
                System.out.println("\t *Days: " + entry.getKey() + "*");
                for (Time time : entry.getValue())
                    System.out.println("\t &Time: " + time.toString() + "&");
            }
        }
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getCurrentRegistered() {
        return currentRegistered;
    }

    public int getRestrictSeats() {
        return restrictSeats;
    }

    public int getGeneralSeats() {
        return generalSeats;
    }

    public String getRestrictTo() {
        return restrictTo;
    }

    public String getLastWithdraw() {
        return lastWithdraw;
    }

    public void setDays(Set<String> days) {
        this.days = days;
    }

    public void setTimeMap(Map<String, List<Time>> timeMap) {
        this.timeMap = timeMap;
    }
}

