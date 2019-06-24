package it.uniupo.reti2.model;

/**
 * Represents an activity, according to the Fitbit Web API
 */
public class Activity {

    // calories
    private int calories;
    // activity description
    private String description;
    // distance (km)
    private int distance;
    // steps
    private int steps;

    /* Getters */
    public int getCalories() {
        return calories;
    }

    public String getDescription() {
        return description;
    }

    public int getDistance() {
        return distance;
    }

    public int getSteps() {
        return steps;
    }

}
