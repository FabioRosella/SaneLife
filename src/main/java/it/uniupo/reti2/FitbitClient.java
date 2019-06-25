package it.uniupo.reti2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonObjectParser;
import com.google.gson.Gson;
import it.uniupo.reti2.FitbitCredentials.OAuthCredentials;
import it.uniupo.reti2.Models.Activities;

import java.io.IOException;

public class FitbitClient {

    private static final Gson gson = new Gson();

    //------------------------------------------------------------------------------------------------------------------
    // Effettua la richiesta al FitBit.
    // In questo caso, restituisce tutte le attività dell'utente per un giorno specifico.
    //------------------------------------------------------------------------------------------------------------------

    private static void run(HttpRequestFactory requestFactory) throws IOException {
        // Url da chiamare (API)
        GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/activities/date/2019-03-21.json");
        // Get request
        HttpRequest request = requestFactory.buildGetRequest(url);

        String jsonResponse = request.execute().parseAsString();

        // Serializza l'oggetto Json che arriva come risposta
        Activities activities = gson.fromJson(jsonResponse, Activities.class);

        // Stampa i passi della giornata ricevuti dall'oggetto JSON
        System.out.println(activities.getActivities().get(0).getSteps() + " passi");

    }

    //------------------------------------------------------------------------------------------------------------------
    // MAIN
    //------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        try {
            // Crea l'autorizzazione con un flusso
            /*final Credential credential = OAuthCredentials.authorize();
            // inizializza la richiesta
            HttpRequestFactory requestFactory =
                    OAuthCredentials.getHttpTransport().createRequestFactory((HttpRequest request) -> {
                        credential.initialize(request);
                        request.setParser(new JsonObjectParser(OAuthCredentials.getJsonFactory()));
                    });
            // Siamo Loggati
            run(requestFactory);*/
            // Success!
            test();
            return;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);

    }

    //------------------------------------------------------------------------------------------------------------------
    // Funzione di test per i thread
    //------------------------------------------------------------------------------------------------------------------
    private static void test(){

        System.out.print("Questa e' una funzione di test per i thread");

        Thread monitoringThread = new Thread(() -> {
            try{
                printSomething();
            }
            catch(Exception e){
                System.err.println(e.getMessage());
            }
        });

        monitoringThread.start();
    }

    private static void printSomething(){
        try {
            while (true) {
                System.out.println("TEST");
                Thread.sleep(1000);
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}


