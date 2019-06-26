package it.uniupo.reti2.Models.HeartBeats;

import java.util.List;

public class ActivitiesHeartIntraday {

    private List<ValueIntraday> dataset = null;

    public List<ValueIntraday> getDataSetIntraday() { return dataset; }

    //------------------------------------------------------------------------------------------------------------------
    // Metodo che stampa a video tutti i battiti salvati nella lista dataset
    //------------------------------------------------------------------------------------------------------------------

    public void printBeats(){

        if(dataset == null)
            return;

        for (ValueIntraday beat : dataset) {
            System.out.println("Battito ricevuto : " + beat.getValue());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Effettua una media aritmetica dei battiti recuperati nella fascia di tempo selezionata
    //------------------------------------------------------------------------------------------------------------------

    public int getAvgBeats(){

        if(dataset == null || dataset.size() == 0)
            return 0;

        int n = 0;
        int count=0;

        for(ValueIntraday beat : dataset) {
            n = n + beat.getValue();
            count ++;
        }

        return n/count;
    }
}
