package it.uniupo.reti2;

/**
 * Class to store all the credentials/URLs needed for OAuth 2.0 at Fitbit
 */
public final class OAuthCredentials {

    // sample client id, as provided by Fitbit
    private static final String CLIENT_ID = "22DLSD";
    // sample client secret, as provided by Fitbit
    private static final String CLIENT_SECRET = "d0ea470d22bdddb1d3b9e87a4c885f17";
    // domain and port of the local server to complete the OAuth 2.0 authorization flow
    private static final int PORT = 6789;
    private static final String DOMAIN = "localhost";

    // server URLs, as provided by Fitbit
    private static final String TOKEN_SERVER_URL = "https://api.fitbit.com/oauth2/token";
    private static final String AUTHORIZATION_SERVER_URL = "https://www.fitbit.com/oauth2/authorize";

    /* Getters */
    public static String getClientId() { return CLIENT_ID; }
    public static String getClientSecret() { return CLIENT_SECRET; }
    public static int getPort() { return PORT; }
    public static String getDomain() { return DOMAIN; }

    public static String getTokenServerUrl() {
        return TOKEN_SERVER_URL;
    }
    public static String getAuthorizationServerUrl() {
        return AUTHORIZATION_SERVER_URL;
    }

}
