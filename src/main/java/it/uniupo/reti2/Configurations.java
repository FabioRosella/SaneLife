package it.uniupo.reti2;

public class Configurations {

    //Battito limite sopra al quale viene effettuata la cromoterapia
    public static final int MaxHeartBeat = 100;
	//Battiti limite minimi
    public static final int MinHeartBeat = 40;
	// Minuti di scarto per recuperare i dati dei battiti
    public static final int Sync = 1;
	// Durata in millisecondi della cromoterapia
    public static final int TimeCromo = 30000;
    //Battiti limite per l'inivio della mail
    public static final int LimitHeartBeat = 150;
    //Durata in minuti che identifica ogni quanto tempo viene effettuato un controllo sulla batteria
    public static final int TimeMonitoringBattery = 5;
    //Numero di misurazioni dopo il quale è possibile rispedire una mail al dottore
    public static final int TimeResendMail = 50;
}
