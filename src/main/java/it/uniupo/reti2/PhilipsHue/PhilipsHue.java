package it.uniupo.reti2.PhilipsHue;

import java.util.Map;

public class PhilipsHue {
	 private final String URL  = "http://127.0.0.1:8000";
	    private final String user = "newdeveloper";

	/*
	    private final String URL  = "http://172.30.1.138";
	    private final String user = "C0vPwqjJZo5Jt9Oe5HgO6sBFFMxgoR532IxFoGmx";
	*/

	    private String lightURL = URL + "/api/" + user + "/lights/";
	    private Map<String, ?> allLights;
	    public boolean state, colorloop;
	    private int id;

	    public PhilipsHue(int id){
	        this.id = id;
	        this.colorloop = false;
	        lightURL += id;
	        allLights = RestCall.get(lightURL);

	        turnOffLight();
	    }

	    public void turnOnLight(int color) throws InterruptedException {
	        colorloop = false;

	        String call = lightURL + "/state";
	        String body = "{\"on\": true, \"bri\" : 254, \"sat\":254, \"hue\" : " + color + "}";
	        RestCall.put(call, body, "application/json");
	        turnOffLight();
	    }

	    public void turnOffLight() {
	        colorloop = false;
	        String call = lightURL + "/state";
	        String body = "{\"on\": false}";
	        RestCall.put(call, body, "application/json");
	    }

	    public void turnColorloopOn() {
	        colorloop = true;
	        String call = lightURL  + "/state";
	        String body = "{\"on\": true, \"effect\" : \"colorloop\" }";
	        RestCall.put(call, body, "application/json");
	    }

	 /* cromoterapia da eliminare o modifica? 
	  *  public void waitLoop(int durata_cromoterapia) {
	        for (int i = 0; i < durata_cromoterapia; i++) {
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            System.out.println(durata_cromoterapia - i);
	        }

	        colorloop = false;
	        turnOffLight();
	    }
*/
	    public Integer getId() { return id; }
	    public boolean getColorloop() { return colorloop; }
	    public void setColorloop(boolean colorloop) { this.colorloop = colorloop; }

	    public Map<String, ?> getJsonElement(String element)
	    {
	        return (Map<String, ?>) allLights.get(element);
	    }
}
