package it.uniupo.reti2.FitbitCredentials;

//----------------------------------------------------------------------------------------------------------------------
// Classe che salva tutte le credenziali e dati necessari per l'autenticazione con il Fitbit
//----------------------------------------------------------------------------------------------------------------------

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.util.ArrayList;

public final class OAuthCredentials {

    private static final String CLIENT_ID = "22DLXH";
    private static final String CLIENT_SECRET = "60333f59d244b6decc6dbd07c6c30951";
    private static final int PORT = 6789;
    private static final String DOMAIN = "localhost";

    // server URLs, as provided by Fitbit
    private static final String TOKEN_SERVER_URL = "https://api.fitbit.com/oauth2/token";
    private static final String AUTHORIZATION_SERVER_URL = "https://www.fitbit.com/oauth2/authorize";

    // Scopo dell'autorizzazione: a cosa vogliamo accedere activities, heartrates, location, etc.
    private static final ArrayList<String> SCOPE =  new ArrayList<String>()
    {{
        add("activity"); add("heartrate"); add("location"); add("nutrition");
        add("profile"); add("settings"); add("sleep"); add("social"); add("weight");
    }};

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static File DATA_STORE_DIR = new File(System.getProperty("user"), "userToken");

    public static JsonFactory getJsonFactory() { return JSON_FACTORY; }
    public static HttpTransport getHttpTransport() { return HTTP_TRANSPORT; }

    //------------------------------------------------------------------------------------------------------------------
    // Implementa l'autorizzazione Code Grant Flow of OAuth 2.0, per il  Fitbit.
    //------------------------------------------------------------------------------------------------------------------

    public static Credential authorize() throws Exception {
        // build the authorization flow, set scope and datastore
        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl(TOKEN_SERVER_URL),
                new BasicAuthentication(CLIENT_ID, CLIENT_SECRET),
                CLIENT_ID,
                AUTHORIZATION_SERVER_URL
        )
                .setScopes(SCOPE)
                .setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR)).build();

        // init the local server to handle the outcome of the authorization process
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(DOMAIN)
                .setPort(PORT).build();

        // actually perform the authorization process. "user" is used as unique identifier.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
