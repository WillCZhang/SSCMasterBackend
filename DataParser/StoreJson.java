package com.CourseSchedule.DataParser;

import com.CourseSchedule.CourseScheduleManager.*;
import com.CourseSchedule.CourseScheduleManager.Exceptions.InstructorTBAException;
import com.CourseSchedule.CourseScheduleManager.Exceptions.NoScheduledMeetingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.Set;

/**
 * Created by Will on 2017/6/1.
 */
public class StoreJson {
    public static final String JSON = ".json";
    public static final String SECTION_JSON_PATH = "/Users/ZC/Documents/CourseScheduleBackground/src/com/CourseSchedule/sections/";
    public static final String COURSES_JSON_PATH = "/Users/ZC/Documents/CourseScheduleBackground/src/com/CourseSchedule/courses/";
    public static final String DEPARTMENT_JSON_PATH = "/Users/ZC/Documents/CourseScheduleBackground/src/com/CourseSchedule/departments/";

    public StoreJson() {
        new ReadObjects();
        for (int i = 0; i < CourseManager.getInstance().getFaculties().length; i++)
            System.out.println(CourseManager.getInstance().getFaculties()[i]);

        Storing();
    }

    private void Storing() {
        try {
            JSONArray departmentsJsonArray = new JSONArray();
            for (Department department : CourseManager.getInstance().getDepartments()) {

                storeDepartmentJson(departmentsJsonArray, department);

            }
        } catch (JSONException ignored) {}
        catch (IOException ignored) {}
    }


    private void storeDepartmentJson(JSONArray departmentsJsonArray, Department department) throws JSONException, IOException {
        JSONObject departmentObject = new JSONObject();
        departmentObject.put("shortName", department.getShortName());
        departmentObject.put("name", department.getName());
        departmentObject.put("faculty", department.getFaculty());

        JSONObject coursesObjects = new JSONObject();
        JSONArray courseJsonArray = new JSONArray();
        for (Course course : department) {

            storeCourseJson(courseJsonArray, course);

        }
        //departmentObject.put("courses", courseJsonArray);

        FileWriter fileWriter = new FileWriter(DEPARTMENT_JSON_PATH + department.getShortName() + JSON);
        fileWriter.write(departmentsJsonArray.toString());
        fileWriter.flush();
    }

    private void storeCourseJson(JSONArray courseJsonArray, Course course) throws JSONException, IOException {
        JSONObject courseJson = new JSONObject();
        courseJson.put("courseNumber", course.getCourseNumber());
        courseJson.put("courseName", course.getCourseName());
        courseJson.put("description", course.getDescription());
        courseJson.put("credits", course.getCredits());
        courseJson.put("reqs", course.getReqs());

        JSONObject sectionObjects = new JSONObject();
        JSONArray sectionJsonArray = new JSONArray();
        for (Section section : course) {

            storeSectionJson(sectionJsonArray, section);

        }

        FileWriter fileWriter = new FileWriter(COURSES_JSON_PATH +
                course.getDepartment().getShortName() + course.getCourseNumber() + JSON);
        fileWriter.write(courseJson.toString());
        fileWriter.flush();
    }

    private void storeSectionJson(JSONArray sectionJsonArray, Section section) throws JSONException, IOException {
        JSONObject sectionJson = new JSONObject();
        sectionJson.put("section", section.getSection());
        sectionJson.put("status", section.getStatus());
        sectionJson.put("activity", section.getActivity());
        sectionJson.put("term", section.getTerm());
        try {
            Classroom classroom = section.getClassroom();
            sectionJson.put("classroom", classroom.getName());
            sectionJson.put("building", classroom.getBuildingThatThisClassroomAt().getName());

            Set<String> days = section.getDays();
            JSONArray daysJsonArray = new JSONArray();
            for (String day : days) {
                List<Time> times = section.getTimeMap().get(day);
                JSONObject timePairs = new JSONObject();
                for (Time time : times)
                    timePairs.put(day, time);
                daysJsonArray.put(timePairs);
            }
            if (daysJsonArray.length() != 0)
                sectionJson.put("days", daysJsonArray);
        } catch (NoScheduledMeetingException ignored) {
        }

        try {
            Instructor instructor = section.getInstructor();
            JSONObject instructorInfo = new JSONObject();
            instructorInfo.put("name", instructor.getName());
            instructorInfo.put("website", instructor.getWebsite());
            sectionJson.put("instructor", instructorInfo);
        } catch (InstructorTBAException ignored) {
        }

        Course course = section.getCourse();
        FileWriter fileWriter = new FileWriter(SECTION_JSON_PATH +
                course.getDepartment().getShortName() + course.getCourseNumber() + section.getSection() + JSON);
        fileWriter.write(sectionJson.toString());
        fileWriter.flush();
    }
}
