package it.uniupo.reti2.Models.HeartBeats;

import com.google.gson.annotations.SerializedName;

public class HeartBeats {

    @SerializedName("activities-heart-intraday")
    private ActivitiesHeartIntraday activitiesHeartIntraday = null;

    public ActivitiesHeartIntraday getActivitiesHeartIntraday() { return activitiesHeartIntraday; }
}
