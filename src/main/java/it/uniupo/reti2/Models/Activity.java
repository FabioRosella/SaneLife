package it.uniupo.reti2.Models;

  //--------------------------------------------------------------------------------------------------------------------
  // Rappresenta un attività di una giornata
  //--------------------------------------------------------------------------------------------------------------------

public class Activity {

    // calorie
    private int calories;
    // descrizione attività
    private String description;
    // distanza (km)
    private int distance;
    // steps
    private int steps;

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
