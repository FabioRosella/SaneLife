package it.uniupo.reti2.PhilipsHue;

import java.util.Map;

public class PhilipsHue {

	private final String URL  = "http://172.30.1.138:80";
	private final String user = "Ugb7HMqMlfrsN7m2gzvWIwzgoNefeFPrUVgt5TmE";
	//per i test
	/*
	String URL = "http://localhost:8000";
	private final String user = "newdeveloper";*/

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
		String body = "{\"on\": true, \"bri\" : 254, \"sat\":180, \"hue\" : " + color + "}";
		RestCall.put(call, body, "application/json");
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

	public Integer getId() { return id; }
	public boolean getColorloop() { return colorloop; }
	public void setColorloop(boolean colorloop) { this.colorloop = colorloop; }

	public Map<String, ?> getJsonElement(String element)
	{
		return (Map<String, ?>) allLights.get(element);
	}
}
