package it.uniupo.reti2.Models.HeartBeats;

import com.google.gson.annotations.SerializedName;

//----------------------------------------------------------------------------------------------------------------------
// Modello che serve per deserializzati dell'oggetto json che viene restituito effettuando la chiamata per i battiti
//----------------------------------------------------------------------------------------------------------------------

public class HeartBeats {

	//Viene utilizzata una serializzazione con il nome corretto del attributo json
    @SerializedName("activities-heart-intraday")
    private ActivitiesHeartIntraday activitiesHeartIntraday = null;

    public ActivitiesHeartIntraday getActivitiesHeartIntraday() { return activitiesHeartIntraday; }
}
