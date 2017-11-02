package com.CourseSchedule;

import com.CourseSchedule.DataParser.ReadObjects;
import com.CourseSchedule.JsonFileStoreAndRead.StoreJson;

/**
 * Created by Will on 2017/6/8.
 */
public class RunStoreJson {
    public static void main(String[] args) {
        new ReadObjects();

        System.out.println("Finished Reading");

        new StoreJson();
    }
}
