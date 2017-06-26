package com.CourseSchedule;

/**
 * Created by Will on 2017/6/17.
 */
public class Test {
    public static void main(String[] args) {
        String a = "123";
        String[] temp = a.split("[a-zA-Z]");
        System.out.println(temp.length);

        String b = "123B";
        String[] temp2 = b.split("[a-zA-Z]");
        System.out.println(temp2.length);
    }
}
