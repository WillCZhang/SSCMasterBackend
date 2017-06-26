package com.CourseSchedule.JsonFileStoreAndRead;

import com.CourseSchedule.CourseScheduleManager.*;
import com.CourseSchedule.CourseScheduleManager.Exceptions.InstructorTBAException;
import com.CourseSchedule.CourseScheduleManager.Exceptions.NoScheduledMeetingException;
import com.CourseSchedule.DataParser.ReadObjects;
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
    public static final String tempPath = "/Users/ZC/Documents/UBCCourseManager/app/src/main/assets/";
    public static final String SECTION_JSON_PATH = tempPath;
            //"/Users/ZC/Documents/CourseScheduleBackground/src/com/CourseSchedule/JsonData/sections/";
    public static final String COURSES_JSON_PATH = tempPath;
            //"/Users/ZC/Documents/CourseScheduleBackground/src/com/CourseSchedule/JsonData/courses/";
    public static final String DEPARTMENT_JSON_PATH = tempPath;
                    //"/Users/ZC/Documents/CourseScheduleBackground/src/com/CourseSchedule/JsonData/departments/";
    public static final String FACULTY_JSON = tempPath;
                            //"/Users/ZC/Documents/CourseScheduleBackground/src/com/CourseSchedule/JsonData/faculties/";

    public StoreJson() {
        new ReadObjects();

        Storing();

        System.out.println("Done!");
    }

    private void Storing() {
        try {
            for (Department department : CourseManager.getInstance().getDepartments()) {

                storeDepartmentJson(department);

            }
        } catch (JSONException | IOException e) {
            System.out.println(e.getMessage());
        }


        storeFaculties();
    }

    private void storeFaculties() {
        try {
            for (String faculty : CourseManager.getInstance().getFaculties()) {
                JSONObject facultyObject = new JSONObject();
                String name = "name";
                String departments = "departments";
                String departmentShortName = "department";
                if (!faculty.equals("Regi")) {
                    facultyObject.put(name, faculty);
                    JSONArray departmentArray = new JSONArray();
                    for (Department department : CourseManager.getInstance().getDepartmentSetByFaculty(faculty)) {
                        JSONObject temp = new JSONObject();
                        temp.put(departmentShortName, department.getShortName());
                        departmentArray.put(temp);
                    }
                    facultyObject.put(departments, departmentArray);
                    FileWriter fileWriter = new FileWriter(FACULTY_JSON + faculty + JSON);
                    fileWriter.write(facultyObject.toString());
                    fileWriter.flush();
                }
            }
        } catch (JSONException | IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private void storeDepartmentJson(Department department) throws JSONException, IOException {
        JSONObject departmentObject = new JSONObject();
        departmentObject.put("shortName", department.getShortName());
        departmentObject.put("name", department.getName());
        departmentObject.put("faculty", department.getFaculty());

        JSONArray coursesList = new JSONArray();
        for (Course course : department) {
            storeCourseJson(course);
            JSONObject temp = new JSONObject();
            temp.put("course", course.getCourseNumber());
            coursesList.put(temp);
        }
        departmentObject.put("courses", coursesList);

        FileWriter fileWriter = new FileWriter(DEPARTMENT_JSON_PATH + department.getShortName() + JSON);
        fileWriter.write(departmentObject.toString());
        fileWriter.flush();
    }

    private void storeCourseJson(Course course) throws JSONException, IOException {
        JSONObject courseJson = new JSONObject();
        courseJson.put("courseNumber", course.getCourseNumber());
        courseJson.put("courseName", course.getCourseName());
        courseJson.put("description", course.getDescription());
        courseJson.put("credits", course.getCredits());
        courseJson.put("reqs", course.getReqs());

        JSONArray sectionsList = new JSONArray();
        for (Section section : course) {
            storeSectionJson(section);
            JSONObject temp = new JSONObject();
            temp.put("section", section.getSection());
            sectionsList.put(temp);
        }
        courseJson.put("sections", sectionsList);

        FileWriter fileWriter = new FileWriter(COURSES_JSON_PATH +
                course.getDepartment().getShortName() + course.getCourseNumber() + JSON);
        fileWriter.write(courseJson.toString());
        fileWriter.flush();
    }

    private void storeSectionJson(Section section) throws JSONException, IOException {
        JSONObject sectionJson = new JSONObject();
        sectionJson.put("section", section.getSection());
        sectionJson.put("status", section.getStatus());
        sectionJson.put("activity", section.getActivity());
        sectionJson.put("term", section.getTerm());
        // TODO: new part, do refactor accordingly
        sectionJson.put("total", section.getTotalSeats());
        sectionJson.put("current", section.getCurrentRegistered());
        sectionJson.put("general", section.getGeneralSeats());
        sectionJson.put("restrict", section.getRestrictSeats());
        sectionJson.put("restrictedTo", section.getRestrictTo());
        sectionJson.put("withrawDay", section.getLastWithdraw());

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
