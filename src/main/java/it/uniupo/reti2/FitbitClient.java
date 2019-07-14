package it.uniupo.reti2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonObjectParser;
import com.google.gson.Gson;
import it.uniupo.reti2.FitbitCredentials.OAuthCredentials;
import it.uniupo.reti2.Mailer.Mailer;
import it.uniupo.reti2.Models.Devices.Device;
import it.uniupo.reti2.Models.HeartBeats.HeartBeats;
import it.uniupo.reti2.Models.Profile.Profile;
import it.uniupo.reti2.PhilipsHue.Colors;
import it.uniupo.reti2.PhilipsHue.PhilipsHue;

import java.io.IOException;
import java.time.LocalTime;

public class FitbitClient {

    private static final Gson gson = new Gson();
    private static String startTime = "";
    private static String endTime = "";

    //Contatore che indica dopo quando è possibile rimandare una mail così da non intasare la sua casella mail
    private static int TimeResendEmail = 0;

	//Inizializzazione della luce per monitorare i battiti ed effettuare una cromoterapia
    private static PhilipsHue light = new PhilipsHue(1);
	//Inizializzazione della luce per monitorare la batteria del dispositivo
    private static PhilipsHue lightBattery = new PhilipsHue(2);

    //Inizializzazione degli orari dove il paziente deve assumere le proprie medicine
    private static final String[] timemedicine = {"7:55-8:00","19:45-20:00"};

    //------------------------------------------------------------------------------------------------------------------
    // MAIN
    //------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        try {
            // Crea l'autorizzazione con un flusso
            final Credential credential = OAuthCredentials.authorize();
            // inizializza la richiesta
            HttpRequestFactory requestFactory =
                    OAuthCredentials.getHttpTransport().createRequestFactory((HttpRequest request) -> {
                        credential.initialize(request);
                        request.setParser(new JsonObjectParser(OAuthCredentials.getJsonFactory()));
                    });
            // Siamo Loggati
            light.turnOffLight();
            profile(requestFactory);
            startingActivities(requestFactory);
            // Success!
            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);

    }

    //------------------------------------------------------------------------------------------------------------------
    // Recupera i dati dell'account del paziente così da stampare nome peso ecc..
    //------------------------------------------------------------------------------------------------------------------

    public static void profile(HttpRequestFactory requestFactory) throws IOException {

		//Effettua una richiesta per recuperare i dati del profilo dell'utente
        GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/profile.json");
        // Get request
        HttpRequest request = requestFactory.buildGetRequest(url);

        String jsonResponse = request.execute().parseAsString();

        Profile profile = gson.fromJson(jsonResponse, Profile.class);
		
		//Scrive a video i dati dell'utente recuperati dalla richiesta
        System.out.println("\nNome del paziente : " + profile.getUser().getName() + " \n " +
                           "Eta' : " + profile.getUser().getAge() + " anni\n " +
                           "Peso : " + profile.getUser().getWeight() + " kg\n\n"
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    // Funzione che avvia tutte le attività che vengono eseguite per monitorare il paziente
    //------------------------------------------------------------------------------------------------------------------

    private static void startingActivities(HttpRequestFactory requestFactory) throws IOException {

        initAlarms(requestFactory);

        Thread monitoringThread = new Thread(() -> {
            try{
                monitoringHearthBeat(requestFactory);
            }
            catch(Exception e){
                System.err.println(e.getMessage());
            }
        });
		
		//Thread che monitora il battito dell'utente (paziente)
		//Fa partire il thread
        monitoringThread.start();
		
		//Processo principale che monitora la batteria del dispositivo
        monitoringBatteryDevice(requestFactory);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Monitora il battito cardiaco del paziente
    //------------------------------------------------------------------------------------------------------------------

    private static void monitoringHearthBeat(HttpRequestFactory requestFactory){
        HeartBeats heartbeats = null;
        int beats;

        try {
            while (true) {
				
				//Recupera l'intervallo di tempo per effettuare la richiesta e visualizzare i battiti
                startTime = getTime();
                Thread.sleep(5000);
				//L'intervallo è sempre di 5 secondi
                endTime = getTime();
				
				//Stampa a video l'intervallo di tempo per la richiesta
                System.out.println("Tempo di start " + startTime + " , tempo di end " + endTime);
				
				//Effettua la richiesta per recuperare i battiti del paziente passando l'intervallo precedentemente scelto
                heartbeats = getHeartBeats(requestFactory,startTime,endTime);

                heartbeats.getActivitiesHeartIntraday().printBeats();
                beats = heartbeats.getActivitiesHeartIntraday().getAvgBeats();
				
				//Stampa la media dei battiti recuperati all'interno dell'intervallo
                System.out.println("Media dei battiti : " + beats);

                if(beats >= Configurations.MaxHeartBeat){

                    //Se i battiti superano la soglia limite mandiamo anche una mail al dottore
                    if((beats >= Configurations.LimitHeartBeat) && (TimeResendEmail == 0))
                    {
                        //Inviamo una mail al dottore per informarlo
                        Mailer javaEmail = new Mailer();
                        javaEmail.setMailServerProperties();
                        javaEmail.createEmailMessage();
                        javaEmail.sendEmail();

                        //Setto il timeout per non continuare a reinviare mail al dottore
                        TimeResendEmail = Configurations.TimeResendMail;
                    }
                    //Facciamo partire la cromoterapia
                    System.out.println("\nBattiti alti --> Cromoterapia rilassante Avviata!");
                    light.turnColorloopOn();
					//Addormentiamo il thread per tutta la durata della cromoteria (variabile globale delle configurazioni)
                    Thread.sleep(Configurations.TimeCromo);
					//Spegnamo le luci
                    light.turnOffLight();

                    System.out.println("Cromoterapia completata!\n");

                    //Aspettiamo 30 secondi per verificare che la cromoterapia sia andata a buon fine
                    Thread.sleep(30000);
                }
                else{
					//Mostriamo dei messaggi se i battiti sono ok o troppo bassi
                    if(beats <= Configurations.MinHeartBeat){
                        System.out.println("Battiti bassi!\n");
                    }
                    else{
                        System.out.println("Battito nella norma!\n");
                    }
                }
				
				//Aspettiamo 5 secondi per effettuare la prossima monitorazione
                Thread.sleep(15000);

                //Se il timeout delle mail non è azzerato allora devo decrementarlo
                if(TimeResendEmail != 0)
                    TimeResendEmail--;
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Funzione che recupera l'ora corrente
    //------------------------------------------------------------------------------------------------------------------

    private static String getTime() {

        LocalTime time = LocalTime.now().minusMinutes(Configurations.Sync);
		
		//Splitta la risposta per scartare i millisecondi
        return time.toString().replace(".","-").split("-")[0];
    }

    //------------------------------------------------------------------------------------------------------------------
    // Effettua una chiamata alla API di fitbit per recuperare i battiti del paziente in un range orario
    //------------------------------------------------------------------------------------------------------------------

    private static HeartBeats getHeartBeats(HttpRequestFactory requestFactory, String start,String end) throws IOException {

        GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/activities/heart/date/today/1d/1sec/time/" + start + "/" + end + ".json");
        // Get request
        HttpRequest request = requestFactory.buildGetRequest(url);

        String jsonResponse = request.execute().parseAsString();

        HeartBeats heartbeats = gson.fromJson(jsonResponse, HeartBeats.class);

        return heartbeats;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Monitora lo status del device monitorando la sua batteria
    //------------------------------------------------------------------------------------------------------------------

    private static void monitoringBatteryDevice(HttpRequestFactory requestFactory) throws IOException {

        try{
            while(true) {
                GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/devices.json");
                // Get request
                HttpRequest request = requestFactory.buildGetRequest(url);

                String jsonResponse = request.execute().parseAsString();
                jsonResponse = jsonResponse.replace("[", "").replace("]","").replace(",\"features\":","");

                //System.out.println("DEBUG : " + jsonResponse);
                Device devs = gson.fromJson(jsonResponse, Device.class);

                System.out.println("Percentuale di batteria rimasta: " + devs.getBatteryLevel() + "%\nBatteria disponibile : " + devs.getBattery() + "\n");

				//A seconda della batteria rimanente accende una luce diversa
                if(devs.getBatteryLevel() >= 60){
                    lightBattery.turnOnLight(Colors.green.getValue());
                }
                else{
                    if(devs.getBatteryLevel() >= 15){
                        lightBattery.turnOnLight(Colors.yellow.getValue());
                    }
                    else{
                        lightBattery.turnOnLight(Colors.red.getValue());
                    }
                }
				
				//Effettua un controllo sulla batteria ogni 5 minuti
                Thread.sleep(Configurations.TimeMonitoringBattery * 60000);
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setta gli allarmi del fitbit per tutta la settimana, così da ricordare al paziente l'assunzione delle proprie
    // medicine
    //------------------------------------------------------------------------------------------------------------------

    private static void initAlarms(HttpRequestFactory requestFactory) throws IOException {

        try {

            /*for (int i = 0; i < timemedicine.length; i++) {
                //Inizializzo la stringa di connessione
                GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/devices/tracker/843203894/alarms.json?time=\"" + timemedicine[i] + "\"&enabled=\"true\"&recurring=\"false\"&weekDays=\"Monday\"");
                //Inizializzo la richiesta con la stringa di connessione
                HttpRequest request = requestFactory.buildGetRequest(url);

                //Effettuo una richiesta in post
                request.setRequestMethod("POST");

                String jsonResponse = request.execute().parseAsString();
            }*/

            //Mostro messaggio che conferma il setting degli allarmi
            System.out.println("Allarmi Settati\n");
        }
        catch(Error e){
            System.err.println(e.getMessage());
        }
    }
}


