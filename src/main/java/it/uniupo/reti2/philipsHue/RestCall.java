package it.uniupo.reti2.philipsHue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class RestCall {
	private static final Gson gson = new Gson();

    public static Map<String, ?> get(String URL){
    	Map<String, ?> response = new HashMap<>();
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	HttpGet request = new HttpGet(URL);

    	CloseableHttpResponse result = null;
    	try {
    		result = httpClient.execute(request);
    		String json = EntityUtils.toString(result.getEntity());
    		response = gson.fromJson(json, Map.class);
    		result.close();
    		httpClient.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    return response;
    }

    public static void put(String URL, String contentBody, String contentType) {

    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	HttpPut request = new HttpPut(URL);
    	StringEntity params = null;
    	try {
    		params = new StringEntity(contentBody);
    		request.addHeader("content-type", contentType);
    		request.setEntity(params);
    		HttpResponse result = httpclient.execute(request);
    		httpclient.close();
    	} 
    	catch (Exception e) {
    		e.printStackTrace();
    	}
	}
}
