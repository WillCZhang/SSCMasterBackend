package com.CourseSchedule;

import com.CourseSchedule.CourseScheduleManager.CourseManager;
import com.CourseSchedule.DataParser.ReadObjects;
import com.CourseSchedule.JsonFileStoreAndRead.StoreJson;

/**
 * Created by Will on 2017/6/8.
 */
public class RunStoreJson {
    public static void main(String[] args) {
        new ReadObjects();
        String[] temp = CourseManager.getInstance().getFaculties();
        for (int i = 0; i < temp.length ; i++)
            System.out.println(temp[i]);

        new StoreJson();
    }
}
