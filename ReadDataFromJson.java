package com.CourseSchedule;

import com.CourseSchedule.CourseScheduleManager.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Iterator;

/**
 * Created by Will on 2017/6/6.
 */
public class ReadDataFromJson {
    private CourseManager courseManager;
    private InstructorManager instructorManager;
    private BuildingManager buildingManager;

    public ReadDataFromJson(String json) {
        try {
            JSONArray departmentsJsonArray = new JSONArray(json);
            for (int departmentIndex = 0; departmentIndex < departmentsJsonArray.length(); departmentIndex++ ) {

                JSONObject departmentJsonObject = departmentsJsonArray.getJSONObject(departmentIndex);
                Department department = new Department(departmentJsonObject.getString("shortName"));
                department.setFaculty(departmentJsonObject.getString("faculty"));
                department.setName(departmentJsonObject.getString("name"));

                JSONArray coursesArray = departmentJsonObject.getJSONArray("courses");
                for (int courseIndex = 0; courseIndex < coursesArray.length(); courseIndex++) {

                    JSONObject courseJson = coursesArray.getJSONObject(courseIndex);
                    Course course = new Course(department, courseJson.getString("courseNumber"), courseJson.getString("courseName"));
                    course.setCredits(courseJson.getString("credits"));
                    course.setDescription(courseJson.getString("description"));
                    course.setReqs(courseJson.getString("reqs"));

                    JSONArray sectionJsonArray = courseJson.getJSONArray("sections");
                    for (int sectionIndex = 0; sectionIndex < sectionJsonArray.length(); sectionIndex++) {

                        JSONObject sectionJson = sectionJsonArray.getJSONObject(sectionIndex);
                        Section section = new Section(course, sectionJson.getString("section"), sectionJson.getString("status"),
                                sectionJson.getString("activity"), null, null, sectionJson.getString("term"));

                        try {
                            String classroomName = sectionJson.getString("classroom");
                            Building building = new Building(sectionJson.getString("building"));
                            Classroom classroom = new Classroom(classroomName, building);
                            section.setClassroom(classroom);

                            JSONArray daysJsonArray =  sectionJson.getJSONArray("days");
                            for (int daysIndex = 0; daysIndex <daysJsonArray.length(); daysIndex++) {

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
                    }
                }
            }
        } catch (JSONException ignored) {
        }

        courseManager = CourseManager.getInstance();
        instructorManager = InstructorManager.getInstance();
        buildingManager = BuildingManager.getInstance();
    }

    public CourseManager getCourseManager() {
        return courseManager;
    }

    public InstructorManager getInstructorManager() {
        return instructorManager;
    }

    public BuildingManager getBuildingManager() {
        return buildingManager;
    }
}
