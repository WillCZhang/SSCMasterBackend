package com.CourseSchedule;

import com.CourseSchedule.CourseScheduleManager.*;
import com.CourseSchedule.DataParser.ReadObjects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StoreObjectData {
    // TODO: write file to this path
    public static final String PATH = "";
    
    private JSONArray courseList;

    public StoreObjectData() {
        new ReadObjects();
        System.out.println("Finished Reading");

        Set<CourseObject> result = new HashSet<>();

        Set<Department> departments = CourseManager.getInstance().getDepartmentSet();
        for (Department department : departments) {
            parseCourseToCourseObject(result, department);
        }

        System.out.println("There are " + result.size() + " courses found on SSC");

        courseList = createCourseJson(result);
        storeCourseArray(courseList);
    }

    public JSONArray getCourseList() {
        return courseList;
    }

    private static void storeCourseArray(JSONArray courseList) {
        try {
            FileWriter fileWriter = new FileWriter(PATH + "courseList.json");
            fileWriter.write(courseList.toString().replaceAll("\n", ""));
            fileWriter.flush();
        } catch (Exception e) {
            System.out.println("wrong!!!");
        }
    }

    private static JSONArray createCourseJson(Set<CourseObject> result) {
        JSONArray jsonArray = new JSONArray();
        for (CourseObject courseObject : result) {
            try {
                JSONObject temp = new JSONObject();
                temp.put("faculty", courseObject.getFaculty());
                temp.put("departmentFullName", courseObject.getDepartmentFullName());
                temp.put("departmentShortName", courseObject.getDepartmentShortName());
                temp.put("courseNumber", courseObject.getCourseNumber());
                temp.put("courseName", courseObject.getCourseName());
                temp.put("description", courseObject.getDescription());
                temp.put("credits", courseObject.getCredits());
                temp.put("reqs", courseObject.getReqs());

                JSONArray sectionJson = creatSectionJson(courseObject.getSections());
                temp.put("sections", sectionJson);

                jsonArray.put(temp);
            } catch (JSONException e) {
                System.out.println("Caught JSON Exception in " + courseObject.getDepartmentShortName() + courseObject.getCourseNumber());
            }
        }

        return jsonArray;
    }

    private static JSONArray creatSectionJson(Set<SectionObject> sections) throws JSONException {
        JSONArray temp = new JSONArray();
        for (SectionObject sectionObject : sections) {
            JSONObject sectionJson = new JSONObject();
            sectionJson.put("section", sectionObject.getSection());
            sectionJson.put("status", sectionObject.getStatus());
            sectionJson.put("activity", sectionObject.getActivity());
            sectionJson.put("instructor", sectionObject.getInstructor());
            sectionJson.put("instructorWebsite", sectionObject.getInstructorWebsite());
            sectionJson.put("classroom", sectionObject.getClassroom());
            sectionJson.put("term", sectionObject.getTerm());
            sectionJson.put("restrictTo", sectionObject.getRestrictTo());
            sectionJson.put("lastWithdraw", sectionObject.getLastWithdraw());

            sectionJson.put("totalSeats", sectionObject.getTotalSeats());
            sectionJson.put("currentRegistered", sectionObject.getCurrentRegistered());
            sectionJson.put("restrictSeats", sectionObject.getRestrictSeats());
            sectionJson.put("generalSeats", sectionObject.getGeneralSeats());

            JSONArray timeMap = new JSONArray();
            if (sectionObject.getTimeMap() != null) {
                for (Map.Entry<String, String> entry : sectionObject.getTimeMap().entrySet()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(entry.getKey(), entry.getValue());
                    timeMap.put(jsonObject);
                }
            }
            sectionJson.put("timeMap", timeMap);

            temp.put(sectionJson);
        }

        return temp;
    }

    private static void parseCourseToCourseObject(Set<CourseObject> result, Department department) {
        String dsn = department.getShortName();
        String dfn = department.getName();
        String f = department.getFaculty();
        for (Course course: department.getCourses()) {
            String cn = course.getCourseNumber();
            CourseObject temp = new CourseObject(dsn, cn);
            temp.setDepartmentFullName(dfn);
            temp.setFaculty(f);
            temp.setCourseName(course.getCourseName());
            temp.setCredits(course.getCredits());
            temp.setDescription(course.getDescription());
            temp.setReqs(course.getReqs());

            if (course.getSections().size() != 0)
                for (Section section : course.getSections())
                    parseSectionToSectionObject(temp.getSections(), section);

            result.add(temp);
        }
    }

    private static void parseSectionToSectionObject(Set<SectionObject> sections, Section section) {
        SectionObject temp = new SectionObject(section.getSection(), section.getStatus(), section.getActivity(), section.getTerm());

        if (section.getInstructor() != null) {
            temp.setInstructor(section.getInstructor().getName());
            temp.setInstructorWebsite(section.getInstructor().getWebsite());
        }

        if (section.getClassroom() != null)
            temp.setClassroom(section.getClassroom().getName());

        temp.setLastWithdraw(section.getLastWithdraw());
        temp.setRestrictTo(section.getRestrictTo());
        temp.setSeatsInfo(section.getTotalSeats(), section.getCurrentRegistered(), section.getRestrictSeats(), section.getGeneralSeats());

        Map<String, List<Time>> timeMap = section.getTimeMap();
        for (Map.Entry<String, List<Time>> entry: timeMap.entrySet()) {
            String days = entry.getKey();
            String start = handleTime(entry.getValue().get(0));
            String end = handleTime(entry.getValue().get(1));
            temp.addTime(days, start, end);
        }

        sections.add(temp);
    }

    private static String handleTime(Time time) {
        String temp = time.toString();
        return temp.split(":")[0] + ":" + temp.split(":")[1];
    }
    
    public static void main(String[] args) {
        new StoreObjectData();
    }
}
