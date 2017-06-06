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
    public static final String JSON_PATH = "/Users/ZC/Documents/CourseScheduleBackground/src/com/CourseSchedule/coursesJson.json";

    public StoreJson() {
        new ReadObjects();
        for (int i = 0; i < CourseManager.getInstance().getFaculties().length; i++)
            System.out.println(CourseManager.getInstance().getFaculties()[i]);

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
                            //int daysIndex = 0;
                            JSONArray daysJsonArray = new JSONArray();
                            for (String day : days) {
                                //String daysKey = "days" + Integer.toString(daysIndex++);

                                List<Time> times = section.getTimeMap().get(day);
                                JSONObject timePairs = new JSONObject();
                                for (Time time : times)
                                    timePairs.put(day, time);
                                //sectionJson.put(daysKey, timePairs);
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
        catch (IOException ignored) {}
    }
}
