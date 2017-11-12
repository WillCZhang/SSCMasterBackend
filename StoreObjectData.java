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
    private JSONArray sectionList;

    public StoreObjectData() {
        new ReadObjects();
        System.out.println("Finished Reading");

        Set<CourseObject> result = new HashSet<>();

        Set<Department> departments = CourseManager.getInstance().getDepartmentSet();
        for (Department department : departments) {
            parseCourseToCourseObject(result, department);
        }

        System.out.println("There are " + result.size() + " courses found on SSC");

        sectionList = new JSONArray();
        courseList = createCourseJson(result);

        storeCourseArray();
        storeSectionArray();
    }

    public JSONArray getCourseList() {
        return courseList;
    }

    public JSONArray getSectionList() {
        return sectionList;
    }

    private void storeCourseArray() {
        try {
            FileWriter fileWriter = new FileWriter(PATH + "courseList.json");
            fileWriter.write(courseList.toString().replaceAll("\n", ""));
            fileWriter.flush();
        } catch (Exception e) {
            System.out.println("Wrong!!!");
        }
    }

    private void storeSectionArray() {
        try {
            for (int i = 0; i < sectionList.length(); i++) {
                JSONObject section = sectionList.getJSONObject(i);
                FileWriter fileWriter = new FileWriter(PATH + section.getString("course") + section.getString("section"));
                fileWriter.write(section.toString().replaceAll("\n", ""));
                fileWriter.flush();
            }
        } catch (Exception e) {
            System.out.println("Wrong!!!");
        }
    }

    private JSONArray createCourseJson(Set<CourseObject> result) {
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

                StringBuilder sections = new StringBuilder();
                for (SectionObject sectionObject : courseObject.getSections()) {
                    JSONObject sectionObj = creatSectionJson(sectionObject);
                    sectionList.put(sectionObj);
                    sections.append(sectionObj.getString("section") + ";");
                }
                temp.put("sections", sections.toString());

                jsonArray.put(temp);
            } catch (JSONException e) {
                System.out.println("Caught JSON Exception in " + courseObject.getDepartmentShortName() + courseObject.getCourseNumber());
            }
        }

        return jsonArray;
    }

    private JSONObject creatSectionJson(SectionObject section) throws JSONException {
        JSONObject sectionJson = new JSONObject();
        sectionJson.put("course", section.getCourse());
        sectionJson.put("section", section.getSection());
        sectionJson.put("status", section.getStatus());
        sectionJson.put("activity", section.getActivity());
        sectionJson.put("instructor", section.getInstructor());
        sectionJson.put("instructorWebsite", section.getInstructorWebsite());
        sectionJson.put("classroom", section.getClassroom());
        sectionJson.put("term", section.getTerm());
        sectionJson.put("restrictTo", section.getRestrictTo());
        sectionJson.put("lastWithdraw", section.getLastWithdraw());

        sectionJson.put("totalSeats", section.getTotalSeats());
        sectionJson.put("currentRegistered", section.getCurrentRegistered());
        sectionJson.put("restrictSeats", section.getRestrictSeats());
        sectionJson.put("generalSeats", section.getGeneralSeats());

        JSONArray timeMap = new JSONArray();
        if (section.getTimeMap() != null) {
            for (Map.Entry<String, String> entry : section.getTimeMap().entrySet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(entry.getKey(), entry.getValue());
                timeMap.put(jsonObject);
            }
        }
        sectionJson.put("timeMap", timeMap);
        return sectionJson;
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
        String course = section.getCourse().getDepartment().getShortName() + section.getCourse().getCourseNumber();
        SectionObject temp = new SectionObject(course, section.getSection(), section.getStatus(), section.getActivity(), section.getTerm());

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
