package it.uniupo.reti2.Models.HeartBeats;

import java.util.List;

public class ActivitiesHeartIntraday {

    private List<ValueIntraday> dataset = null;

    public List<ValueIntraday> getDataSetIntraday() { return dataset; }

    //------------------------------------------------------------------------------------------------------------------
    // Metodo che stampa a video tutti i battiti salvati nella lista dataset
    //------------------------------------------------------------------------------------------------------------------

    public void printBeats(){
        for (ValueIntraday beat : dataset) {
            System.out.println("Battito ricevuto : " + beat.getValue());
        }
    }
}
