package com.CourseSchedule.CourseScheduleManager;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Will on 2017/5/20.
 */
public class BuildingManager implements Serializable {
    private Set<Building> buildings;
    private static BuildingManager instance;

    private BuildingManager() {
        this.buildings = new HashSet<Building>();
    }

    public static void setInstance(BuildingManager instance) {
        BuildingManager.instance = instance;
    }

    public static BuildingManager getInstance() {
        // Do not modify the implementation of this method!
        if(instance == null) {
            instance = new BuildingManager();
        }
        return instance;
    }

    public Building getBuilding(Building building) {
        try {
            for (Building building1 : buildings) {
                if (building.equals(building1)) {
                    for (Classroom classroom : building) {
                        building1.addClassroom(classroom);
                        classroom.addBuilding(building1);
                        return building1;
                    }
                }
            }
            buildings.add(building);
        } catch (ConcurrentModificationException e) {
            getBuilding(building);
        }
        return building;
    }

    public void addClassroom(Classroom classroom) {

    }
}
