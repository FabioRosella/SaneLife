package it.uniupo.reti2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonObjectParser;
import com.google.gson.Gson;
import it.uniupo.reti2.FitbitCredentials.OAuthCredentials;
import it.uniupo.reti2.Models.Activities.Activities;
import it.uniupo.reti2.Models.HeartBeats.HeartBeats;
import it.uniupo.reti2.Models.Profile.Profile;
import it.uniupo.reti2.PhilipsHue.PhilipsHue;

import java.io.IOException;
import java.time.LocalTime;

public class FitbitClient {

    private static final Gson gson = new Gson();
    private static String startTime = "";
    private static String endTime = "";
    private static PhilipsHue light = new PhilipsHue(1);

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
            profile(requestFactory);
            startingActivities(requestFactory);
            //run(requestFactory);
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
    // Effettua la richiesta al FitBit.
    // In questo caso, restituisce tutte le attività dell'utente per un giorno specifico.
    //------------------------------------------------------------------------------------------------------------------

    private static void run(HttpRequestFactory requestFactory) throws IOException {
        // Url da chiamare (API)
        GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/activities/date/2018-05-24.json");
        // Get request
        HttpRequest request = requestFactory.buildGetRequest(url);

        String jsonResponse = request.execute().parseAsString();

        //System.out.println("DEBUG " + jsonResponse);
        // Serializza l'oggetto Json che arriva come risposta
        Activities activities = gson.fromJson(jsonResponse, Activities.class);

        // Stampa i passi della giornata ricevuti dall'oggetto JSON
        System.out.println(activities.getSummary().getSteps() + " passi");
    }

    //------------------------------------------------------------------------------------------------------------------
    // Recupera i dati dell'account del paziente così da stampare nome peso ecc..
    //------------------------------------------------------------------------------------------------------------------

    public static void profile(HttpRequestFactory requestFactory) throws IOException {

        GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/profile.json");
        // Get request
        HttpRequest request = requestFactory.buildGetRequest(url);

        String jsonResponse = request.execute().parseAsString();

        Profile profile = gson.fromJson(jsonResponse, Profile.class);

        System.out.println("\nNome del paziente : " + profile.getUser().getName() + " \n " +
                           "Eta' : " + profile.getUser().getAge() + " anni\n " +
                           "Peso : " + profile.getUser().getWeight() + " kg\n\n"
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    // Funzione che avvia tutte le attività che vengono eseguite per monitorare il paziente
    //------------------------------------------------------------------------------------------------------------------

    private static void startingActivities(HttpRequestFactory requestFactory){

        Thread monitoringThread = new Thread(() -> {
            try{
                monitoringHearthBeat(requestFactory);
            }
            catch(Exception e){
                System.err.println(e.getMessage());
            }
        });

        monitoringThread.start();
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

                startTime = getTime();
                Thread.sleep(5000);
                endTime = getTime();

                System.out.println("Tempo di start " + startTime + " , tempo di end " + endTime);

                heartbeats = getHeartBeats(requestFactory,startTime,endTime);

                heartbeats.getActivitiesHeartIntraday().printBeats();
                beats = heartbeats.getActivitiesHeartIntraday().getAvgBeats();

                System.out.println("Media dei battiti : " + beats);

                if(beats >= Configurations.MaxHertBeat){

                    //Facciamo partire la cromoterapia
                    System.out.println("\nBattiti alti --> Cromoterapia rilassante Avviata!");
                    light.turnColorloopOn();
                    Thread.sleep(Configurations.TimeCromo);
                    light.turnOffLight();
                    System.out.println("Cromoterapia completata!");
                }
                else{
                    System.out.print("Battito nella norma!");
                }

                Thread.sleep(5000);
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

    private static void monitoringBatteryDevice(HttpRequestFactory requestFactory){

    }
}


