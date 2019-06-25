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
        // the URL to call
        GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/activities/date/2019-03-21.json");
        // perform a GET request
        HttpRequest request = requestFactory.buildGetRequest(url);

        // get the response as a JSON (and put it in a string)
        String jsonResponse = request.execute().parseAsString();

        // parse the JSON string in POJO thanks to gson
        Activities activities = gson.fromJson(jsonResponse, Activities.class);

        // print out the steps of the first activity (e.g., 1000 steps for a walk)
        System.out.println(activities.getActivities().get(0).getSteps() + " passi");

    }

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
            run(requestFactory);
            // Success!
            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);

    }
}


