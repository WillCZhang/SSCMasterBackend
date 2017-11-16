import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;

import static com.CourseSchedule.StoreObjectData.PATH;

public class ClassifyObjects {
    public static final String DEPARTMENT_PATH = "";
    public static final String COURSE_PATH = "";
    public static final String SECTION_PATH = "";
    public static final String BUILDING_PATH = "";
    public static final String INSTRUCTOR_PATH = "";

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

    public ClassifyObjects() {
        StoreObjectData storeObjectData = new StoreObjectData();
        courseList = storeObjectData.getCourseList();
        sectionList = storeObjectData.getSectionList();


        try {
            storeFacultyDepartmentPair();
        } catch (JSONException ignored) {

        }

//        sortByDepartment();
//        sortByCourse();
//        sortBySection();
//        sortByBuilding();
//        sortByInstructor();
    }
    
    
/*
  By just looking at the code, storeFacultyDepartmentPair() method has a pretty low running time, 
  but it actually runs pretty fast... So just leave it there... 
  This classify method should not be using often as this is doing in the backend...
*/
    private void storeFacultyDepartmentPair() throws JSONException {
        facultyDepartmentPair = new JSONObject();
        for (int i = 0; i < courseList.length(); i++) {
            JSONObject course = courseList.getJSONObject(i);
            String faculty = course.getString("faculty");
            String departmentSN = course.getString("departmentShortName");
            switch (faculty) {
                case SCIENCE:
                    addToPair(departmentSN, SCIENCE);
                    break;
                case ARTS:
                    addToPair(departmentSN, ARTS);
                    break;
                case DENTISTRY:
                    addToPair(departmentSN, DENTISTRY);
                    break;
                case ENGEERING:
                    addToPair(departmentSN, ENGEERING);
                    break;
                case EDUCATION:
                    addToPair(departmentSN, EDUCATION);
                    break;
                case "Faculty of Comm and Bus Admin":
                    addToPair(departmentSN, SAUDER);
                    break;
                case FORESTRY:
                    addToPair(departmentSN, FORESTRY);
                    break;
                case LFS:
                    addToPair(departmentSN, LFS);
                    break;
                case MUSIC:
                    addToPair(departmentSN, MUSIC);
                    break;
                case LAW:
                    addToPair(departmentSN, LAW);
                    break;
                default:
                    addToPair(departmentSN, OTHERS);
                    break;
            }
        }
        writeToDisk();
    }

    private void writeToDisk() {
        try {
            FileWriter fileWriter = new FileWriter(PATH + "FacultyDepartmentPair.json");
            fileWriter.write(facultyDepartmentPair.toString());
            fileWriter.flush();
        } catch (Exception e) {
            System.out.println("Wrong!!!");
        }
    }

    private void addToPair(String departmentShortName, String faculty) throws JSONException {
        JSONArray departmentList;
        try {
            departmentList = facultyDepartmentPair.getJSONArray(faculty);
            putIntoDepartmentList(departmentList, departmentShortName);
        } catch (JSONException e) {
            departmentList = new JSONArray();
            departmentList.put(departmentShortName);
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

    private void sortByDepartment() throws JSONException {

    }

    private void sortByCourse() {

    }

    private void sortBySection() {

    }

    private void sortByBuilding() {

    }

    private void sortByInstructor() {

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
