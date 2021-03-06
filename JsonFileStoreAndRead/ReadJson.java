package com.CourseSchedule.JsonFileStoreAndRead;

import com.CourseSchedule.CourseScheduleManager.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Will on 2017/6/13.
 */
public class ReadJson {
    public static List<String> facultyReader(String facultyJson) {
        List<String> temp = new ArrayList<>();
        try {
            JSONObject pair = new JSONObject(facultyJson);
            JSONArray departments = pair.getJSONArray("departments");
            for (int i = 0; i < departments.length(); i++) {
                JSONObject object = departments.getJSONObject(i);
                temp.add(object.getString("department"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static void departmentReader(String departmentJson) {
        try {
            JSONObject departmentJsonObject = new JSONObject(departmentJson);
            Department department = new Department(departmentJsonObject.getString("shortName"));
            department.setFaculty(departmentJsonObject.getString("faculty"));
            department.setName(departmentJsonObject.getString("name"));
            JSONArray courses = departmentJsonObject.getJSONArray("courses");
            for (int i = 0; i < courses.length(); i++)
                department.addCourseNumber((String) courses.getJSONObject(i).get("course"));
            CourseManager.getInstance().addDepartmentForDownloadData(department);
        } catch (JSONException ignored) {

        }
    }

    public static void courseReader(Department department, String courseJsonString) {
        try {
            JSONObject courseJson = new JSONObject(courseJsonString);
            Course course = new Course(department, courseJson.getString("courseNumber"), courseJson.getString("courseName"));
            course.setCredits(courseJson.getString("credits"));
            course.setDescription(courseJson.getString("description"));
            course.setReqs(courseJson.getString("reqs"));
            JSONArray sections = courseJson.getJSONArray("sections");
            for (int i = 0; i < sections.length(); i ++)
                course.addSection((String) sections.getJSONObject(i).get("section"));
            CourseManager.getInstance().addCourse(department, course);
        } catch (JSONException ignored) {

        }
    }

    public static void sectionReader(Course course, String sectionJsonString) {
        try {
            JSONObject sectionJson = new JSONObject(sectionJsonString);

            Section section = new Section(course, sectionJson.getString("section"), sectionJson.getString("status"),
                    sectionJson.getString("activity"), null, null, sectionJson.getString("term"));

            try {
                String classroomName = sectionJson.getString("classroom");
                Building building = new Building(sectionJson.getString("building"));
                Classroom classroom = new Classroom(classroomName, building);
                section.setClassroom(classroom);

                JSONArray daysJsonArray = sectionJson.getJSONArray("days");
                for (int daysIndex = 0; daysIndex < daysJsonArray.length(); daysIndex++) {

                    JSONArray daysJson = daysJsonArray.getJSONArray(daysIndex);
                    for (int index = 0; index < daysJson.length() - 1; index++) {
                        JSONObject daysPair = daysJson.getJSONObject(index);
                        Iterator iterator = daysPair.keys();
                        String key = null;
                        while (iterator.hasNext())
                            key = (String) iterator.next();
                        Time start = (Time) daysPair.get(key);
                        Time end = (Time) daysJson.getJSONObject(index + 1).get(key);
                        section.addTime(key, start, end);
                    }
                }

            } catch (JSONException ignored) {
            }

            try {
                JSONObject instructorInfo = sectionJson.getJSONObject("instructor");
                Instructor instructor = new Instructor(instructorInfo.getString("name"));
                instructor.setWebsite(instructorInfo.getString("website"));
                section.setInstructor(instructor);
            } catch (JSONException ignored) {
            }
            course.addSection(section);
        } catch (JSONException ignored) {
        }
    }
}
