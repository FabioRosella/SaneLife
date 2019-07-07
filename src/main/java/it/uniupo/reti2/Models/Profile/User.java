package it.uniupo.reti2.Models.Profile;

//----------------------------------------------------------------------------------------------------------------------
// Modello utente che viene utilizzato per deserializzare l'oggetto json per i suoi dati
//----------------------------------------------------------------------------------------------------------------------

public class User {

	//Nome e cognome
    private String fullName;
	//Età
    private String age;
	//Data di nascita
    private String dateofBirth;
	//Peso
    private String weight;

	// Metodi per recuperare i campi privati
    public String getName() { return fullName; }
    public String getAge() { return age; }
    public String getDateofBirth() { return dateofBirth; }
    public String getWeight() { return weight; }
}
