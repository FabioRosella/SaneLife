package it.uniupo.reti2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.api.client.util.store.FileDataStoreFactory;
import it.uniupo.reti2.model.Activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FitbitClient {

    // variable definitions, for OAuth 2.0
    private static File DATA_STORE_DIR = new File(System.getProperty("user.dir"), "userToken");
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    // the "scope" of the authorization: we would like to access activities, heartrates, location, etc.
    private static final ArrayList<String> SCOPE =  new ArrayList<String>()
    {{
        add("activity"); add("heartrate"); add("location"); add("nutrition");
        add("profile"); add("settings"); add("sleep"); add("social"); add("weight");
    }};

    // JSON-related utilities, both based on Gson
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final Gson gson = new Gson();

    /**
     * Implements the Authorization Code Grant Flow of OAuth 2.0, for Fitbit.
     */
    private static Credential authorize() throws Exception {
        // build the authorization flow, set scope and datastore
        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl(OAuthCredentials.getTokenServerUrl()),
                new BasicAuthentication(OAuthCredentials.getClientId(), OAuthCredentials.getClientSecret()),
                OAuthCredentials.getClientId(),
                OAuthCredentials.getAuthorizationServerUrl()
        )
                .setScopes(SCOPE)
                .setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR)).build();

        // init the local server to handle the outcome of the authorization process
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(OAuthCredentials.getDomain())
                .setPort(OAuthCredentials.getPort()).build();

        // actually perform the authorization process. "user" is used as unique identifier.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Handles the request to the FitBit API. In this case, it requests all the user's activities in a given day.
     */
    private static void run(HttpRequestFactory requestFactory) throws IOException {
        // the URL to call
        GenericUrl url = new GenericUrl("https://api.fitbit.com/1/user/-/activities/date/2019-03-21.json");
        // perform a GET request
        HttpRequest request = requestFactory.buildGetRequest(url);

        // get the response as a JSON (and put it in a string)
        String jsonResponse = request.execute().parseAsString();

        // debug
        //System.out.println("[DEBUG] " + jsonResponse);

        // parse the JSON string in POJO thanks to gson
        Activities activities = gson.fromJson(jsonResponse, Activities.class);

        // print out the steps of the first activity (e.g., 1000 steps for a walk)
        System.out.println(activities.getActivities().get(0).getSteps());

    }


    public static void main(String[] args) {
        try {
            // authorization flow
            final Credential credential = authorize();
            // init a requestFactory
            HttpRequestFactory requestFactory =
                    HTTP_TRANSPORT.createRequestFactory((HttpRequest request) -> {
                        credential.initialize(request);
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    });
            // execute the API call, backed by OAuth 2.0
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


