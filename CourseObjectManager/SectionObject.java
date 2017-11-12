package com.CourseSchedule.CourseScheduleManager;

public class SectionObject {
    private String course;
    private String section, status, activity;
    private String instructor;
    private String instructorWebsite;
    private String classroom;
    private String term;
    private String restrictTo;
    private String lastWithdraw;

    private Set<String> days;
    private Map<String, String> timeMap;
    private int totalSeats, currentRegistered, restrictSeats, generalSeats;

    public SectionObject(String course, String section, String status, String activity, String term) {
        this.course = course;
        this.activity = activity;
        this.section = section;
        this.status = status;
        this.instructor = "TBA";
        this.instructorWebsite = "";
        this.classroom = "";
        this.term = term;
        this.restrictTo = "";
        timeMap = new HashMap<String, String>();
        days = new HashSet<>();
    }

    public void setSeatsInfo(int totalSeats, int currentRegistered, int restrictSeats, int generalSeats) {
        this.totalSeats = totalSeats;
        this.currentRegistered = currentRegistered;
        this.restrictSeats = restrictSeats;
        this.generalSeats = generalSeats;
    }

    public void addTime(String days, String start, String end) {
        this.days.add(days);

        String times = timeMap.get(days);
        if (times != null) {
            times += ";" + start + " - " + end;
        } else {
            String temp = start + " - " + end;
            timeMap.put(days,temp);
        }
    }

    public void setRestrictTo(String restrictTo) {
        this.restrictTo = restrictTo;
    }

    public void setLastWithdraw(String lastWithdraw) {
        this.lastWithdraw = lastWithdraw;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setInstructorWebsite(String instructorWebsite) {
        this.instructorWebsite = instructorWebsite;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSection() {
        return section;
    }

    public String getStatus() {
        return status;
    }

    public String getActivity() {
        return activity;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getTerm() {
        return term;
    }

    public Set<String> getDays() {
        return days;
    }

    public Map<String, String> getTimeMap() {
        return timeMap;
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
    
    public String getCourse() {
        return course;
    }

    public String getRestrictTo() {
        return restrictTo;
    }

    public String getLastWithdraw() {
        return lastWithdraw;
    }

    public String getInstructorWebsite() {
        return instructorWebsite;
    }
}
