package it.uniupo.reti2.Models.HeartBeats;

import java.util.List;

public class ActivitiesHeartIntraday {
	
	// Modello dataset che contiene i dati dei battiti (lista di battiti)
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
		
		//Se non sono registrati dei battiti il metodo ritorna 0
        if(dataset == null || dataset.size() == 0)
            return 0;

        int n = 0;
        int count=0;
		
		// Calcola il numero di battiti recuperati sommandoli tra loro
        for(ValueIntraday beat : dataset) {
            n = n + beat.getValue();
            count ++;
        }
		
		// Effettua la media e ritorna il valore
        return n/count;
    }
}
