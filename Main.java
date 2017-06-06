package com.CourseSchedule;

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

public class Main {
    public static final String JSON_PATH = "/Users/ZC/Documents/UBCCourseManager/app/src/main/assets/coursesJson.json";

    private static Set<Department> departments;
    private static InstructorManager instructorManager;
    private static BuildingManager buildingManager;

    public static void main(String[] args) throws IOException {
        new ReadObjects();

//        departments = CourseManager.getInstance().getDepartments();
//        instructorManager = InstructorManager.getInstance();
//        buildingManager = BuildingManager.getInstance();
//        Gson departmentsGson = new Gson();
//        Gson iM = new Gson();
//        Gson bM = new Gson();

        try {
            //JSONObject departmentsObjects = new JSONObject();
            //int departmentIndex = 0;
            JSONArray departmentsJsonArray = new JSONArray();
            for (Department department : CourseManager.getInstance().getDepartments()) {
                //String departmentKey = "department" + Integer.toString(departmentIndex++);

                JSONObject departmentObject = new JSONObject();
                departmentObject.put("shortName", department.getShortName());
                departmentObject.put("name", department.getName());
                departmentObject.put("faculty", department.getFaculty());

                JSONObject coursesObjects = new JSONObject();
                //int courseIndex = 0;
                JSONArray courseJsonArray = new JSONArray();
                for (Course course : department) {
                    //String courseKey = "course" + Integer.toString(courseIndex++);

                    JSONObject courseJson = new JSONObject();
                    courseJson.put("courseNumber", course.getCourseNumber());
                    courseJson.put("courseName", course.getCourseName());
                    courseJson.put("description", course.getDescription());
                    courseJson.put("credits", course.getCredits());
                    courseJson.put("reqs", course.getReqs());

                    JSONObject sectionObjects = new JSONObject();
                    //int sectionIndex = 0;
                    JSONArray sectionJsonArray = new JSONArray();
                    for (Section section : course) {
                        //String sectionKey = "section" + Integer.toString(sectionIndex++);

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
                                for (Time time : times) {
                                    JSONObject timePairs = new JSONObject();
                                    timePairs.put(day, time);
                                    daysJsonArray.put(timePairs);
                                }
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

                        sectionJsonArray.put(sectionJson);
                        //sectionObjects.put(sectionKey, sectionJson);
                    }
                    courseJson.put("sections", sectionJsonArray);

                    courseJsonArray.put(courseJson);
                    //coursesObjects.put(courseKey, courseJson);
                }
                departmentObject.put("courses", courseJsonArray);

                departmentsJsonArray.put(departmentObject);
                //departmentsObjects.put(departmentKey, departmentObject);

                FileWriter fileWriter = new FileWriter(JSON_PATH);
                fileWriter.write(departmentsJsonArray.toString());
                fileWriter.flush();
            }
        } catch (JSONException ignored) {}
    }
}
