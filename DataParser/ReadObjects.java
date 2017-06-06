package com.CourseSchedule.DataParser;

import com.CourseSchedule.CourseScheduleManager.BuildingManager;
import com.CourseSchedule.CourseScheduleManager.CourseManager;
import com.CourseSchedule.CourseScheduleManager.InstructorManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static com.CourseSchedule.DataParser.ModifyMultithreadedRequestData.OBJECT_PATH;

/**
 * Created by Will on 2017/5/24.
 */
public class ReadObjects {
    public ReadObjects() {
        try {
            FileInputStream fin1 = new FileInputStream(OBJECT_PATH + "BuildingManager.ser");
            ObjectInputStream ois1 = new ObjectInputStream(fin1);
            BuildingManager.setInstance((BuildingManager) ois1.readObject());
            ois1.close();

            FileInputStream fin2 = new FileInputStream(OBJECT_PATH + "CourseManager.ser");
            ObjectInputStream ois2 = new ObjectInputStream(fin2);
            CourseManager.setInstance((CourseManager) ois2.readObject());
            ois2.close();

            FileInputStream fin3 = new FileInputStream(OBJECT_PATH + "InstructorManager.ser");
            ObjectInputStream ois3 = new ObjectInputStream(fin3);
            InstructorManager.setInstance((InstructorManager) ois3.readObject());
            ois3.close();
        } catch(IOException i) {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c) {
            System.out.println("Required class not found");
            c.printStackTrace();
            return;
        }
    }



//    public static void main(String[] args) {
//        try {
//            String fileName1 = "BuildingManager.ser";
//            FileInputStream fin1 = new FileInputStream(fileName1);
//            ObjectInputStream ois1 = new ObjectInputStream(fin1);
//            BuildingManager.setInstance((BuildingManager) ois1.readObject());
//            ois1.close();
//
//            String fileName2 = "CourseManager.ser";
//            FileInputStream fin2 = new FileInputStream(fileName2);
//            ObjectInputStream ois2 = new ObjectInputStream(fin2);
//            CourseManager.setInstance((CourseManager) ois2.readObject());
//            ois2.close();
//
//            String fileName3 = "InstructorManager.ser";
//            FileInputStream fin3 = new FileInputStream(fileName3);
//            ObjectInputStream ois3 = new ObjectInputStream(fin3);
//            InstructorManager.setInstance((InstructorManager) ois3.readObject());
//            ois3.close();
//        } catch(IOException i) {
//            i.printStackTrace();
//            return;
//        }catch(ClassNotFoundException c) {
//            System.out.println("Required class not found");
//            c.printStackTrace();
//            return;
//        }
//        CourseManager.getInstance().print();
//    }
}
