import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;

import static com.CourseSchedule.StoreObjectData.PATH;

public class ClassifyObjects {
    private final static String SCIENCE = "Faculty of Science";
    private final static String ARTS = "Faculty of Arts";
    private final static String SAUDER = "Sauder School of Business";
    private final static String ENGEERING = "Faculty of Applied Science";
    private final static String EDUCATION = "Faculty of Education";
    private final static String FORESTRY = "Faculty of Forestry";
    private final static String LFS = "Faculty of Land and Food System";
    private final static String DENTISTRY = "Faculty of Dentistry";
    private final static String MUSIC = "School of Music";
    private final static String LAW = "Peter A. Allard School of Law";
    private final static String OTHERS = "Others";

    private JSONArray courseList;
    private JSONArray sectionList;
    private JSONObject facultyDepartmentPair;
    private JSONObject departmentCoursePair;

    public ClassifyObjects() {
        StoreObjectData storeObjectData = new StoreObjectData();
        courseList = storeObjectData.getCourseList();
        sectionList = storeObjectData.getSectionList();


        try {
            storeFacultyDepartmentPair();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            storeDepartmentCoursePair();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeDepartmentCoursePair() throws JSONException {
        departmentCoursePair = new JSONObject();
        for (int i = 0; i < courseList.length(); i++) {
            JSONObject course = courseList.getJSONObject(i);
            String departmentSN = course.getString("departmentShortName");
            JSONArray courseNumberList;
            try {
                courseNumberList = departmentCoursePair.getJSONArray(departmentSN);
            } catch (JSONException e) {
                courseNumberList = new JSONArray();
                departmentCoursePair.put(departmentSN, courseNumberList);
            }
            String courseNumber = course.getString("courseNumber");
            String courseName = course.getString("courseName");
            courseNumberList.put(courseNumber + "," + courseName);
        }
        writeToDisk("DepartmentCoursePair.json", departmentCoursePair);
    }

    private void storeFacultyDepartmentPair() throws JSONException {
        facultyDepartmentPair = new JSONObject();
        for (int i = 0; i < courseList.length(); i++) {
            JSONObject course = courseList.getJSONObject(i);
            String faculty = course.getString("faculty");
            String departmentSN = course.getString("departmentShortName");
            String departmentFN = course.getString("departmentFullName");
            String departmentSNFNPair = departmentSN + "," + departmentFN;
            switch (faculty) {
                case SCIENCE:
                    addToPair(departmentSNFNPair, SCIENCE);
                    break;
                case ARTS:
                    addToPair(departmentSNFNPair, ARTS);
                    break;
                case DENTISTRY:
                    addToPair(departmentSNFNPair, DENTISTRY);
                    break;
                case ENGEERING:
                    addToPair(departmentSNFNPair, ENGEERING);
                    break;
                case EDUCATION:
                    addToPair(departmentSNFNPair, EDUCATION);
                    break;
                case "Faculty of Comm and Bus Admin":
                    addToPair(departmentSNFNPair, SAUDER);
                    break;
                case FORESTRY:
                    addToPair(departmentSNFNPair, FORESTRY);
                    break;
                case LFS:
                    addToPair(departmentSNFNPair, LFS);
                    break;
                case MUSIC:
                    addToPair(departmentSNFNPair, MUSIC);
                    break;
                case LAW:
                    addToPair(departmentSNFNPair, LAW);
                    break;
                default:
                    addToPair(departmentSNFNPair, OTHERS);
                    break;
            }
        }
        writeToDisk("FacultyDepartmentPair.json", facultyDepartmentPair);
    }

    private void writeToDisk(String name, JSONObject sourse) {
        try {
            FileWriter fileWriter = new FileWriter(PATH + name);
            fileWriter.write(sourse.toString());
            fileWriter.flush();
        } catch (Exception e) {
            System.out.println("Wrong!!!");
        }
    }

    private void addToPair(String departmentSNFNPair, String faculty) throws JSONException {
        JSONArray departmentList;
        try {
            departmentList = facultyDepartmentPair.getJSONArray(faculty);
            putIntoDepartmentList(departmentList, departmentSNFNPair);
        } catch (JSONException e) {
            departmentList = new JSONArray();
            departmentList.put(departmentSNFNPair);
            facultyDepartmentPair.put(faculty, departmentList);
        }
    }

    private void putIntoDepartmentList(JSONArray departmentList, String departmentShortName) throws JSONException {
        for (int i = 0; i < departmentList.length(); i++) {
            String tempName = departmentList.getString(i);
            if (tempName.equals(departmentShortName))
                return;
        }
        departmentList.put(departmentShortName);
    }

    private static void storeJsonArray(JSONArray jsonArray, String path, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(path + fileName);
            fileWriter.write(jsonArray.toString().replaceAll("\n", ""));
            fileWriter.flush();
        } catch (Exception e) {
            System.out.println("wrong!!!");
        }
    }

    public static void main(String[] args) {
        new ClassifyObjects();
    }
}
