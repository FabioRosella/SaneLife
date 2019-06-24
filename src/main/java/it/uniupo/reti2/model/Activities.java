package it.uniupo.reti2.model;

import java.util.ArrayList;

/**
 * To parse the JSON content of the activities coming from the Fitbit Web APIs
 */
public class Activities {

    // one of the objects in the "activities" JSON
    private ArrayList<Activity> activities;

    // another example, not yet implemented
    // private Summary summary;

    /* Getters */
    public ArrayList<Activity> getActivities() {
        return activities;
    }

}
