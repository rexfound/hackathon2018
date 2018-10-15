package com.ze.hackathon.Hackathon2018;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ze.hackathon.Hackathon2018.service.DataService;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Hackathon2018Application {

	private static Map<String, String> FIELD_MAP = new HashMap<String, String>();
	static {
		FIELD_MAP.put("name", "CITY_NAME");
		FIELD_MAP.put("lat", "LAT");
		FIELD_MAP.put("lon", "LON");
		FIELD_MAP.put("temp", "TEMP");
		FIELD_MAP.put("pressure", "PRESSURE");
		FIELD_MAP.put("humidity", "HUMIDITY");
		FIELD_MAP.put("temp_min", "TEMP_LO");
		FIELD_MAP.put("temp_max", "TEMP_HI");
		FIELD_MAP.put("speed", "WIND_SPD");
		FIELD_MAP.put("deg", "WIND_DIR");
		FIELD_MAP.put("gust", "GUST");
		FIELD_MAP.put("country", "COUNTRY");
		FIELD_MAP.put("rain", "RAIN");
		FIELD_MAP.put("snow", "SNOW");
		FIELD_MAP.put("clouds", "CLOUD");
		FIELD_MAP.put("description", "DESCRIPTION");

	}

	DataService dataService = new DataService();

	public Hackathon2018Application() {
		Runnable runnable = () -> {
			try {
				RestTemplate restTemplate = new RestTemplate();

				// Create the request body as a MultiValueMap
				MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();

				body.add("X-Auth-Token", "d1a78d030eac433ebf8f7a14c1eb6e73");

				// Note the body object as first parameter!
				HttpEntity<?> httpEntity = new HttpEntity<Object>(body);
				//https://api.openweathermap.org/data/2.5/find?lat=49.2&lon=-122.95&cnt=50&appid=17556070bfb60252ba8bcd48fc9408d5
				while (true) {
					ResponseEntity<String> result = restTemplate.exchange(
							"https://api.openweathermap.org/data/2.5/find?lat=49.2&lon=-122.95&cnt=50&appid={id}", HttpMethod.GET, httpEntity, String.class, "17556070bfb60252ba8bcd48fc9408d5");
					parseJSONData(result);
					TimeUnit.SECONDS.sleep(10);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		Thread thread = new Thread(runnable);
		thread.start();
	}

	public static void main(String[] args) {
		SpringApplication.run(Hackathon2018Application.class, args);
	}

	private void parseJSONData(ResponseEntity<String> result) {
		JsonFactory factory = new JsonFactory();
		Document document = new Document();
		Date date = Calendar.getInstance().getTime();
		MongoDatabase database =  dataService.initConnection();
		MongoCollection userCollection = database.getCollection("stations");

		try {
			JsonParser parser = factory.createParser(result.getBody());
			while(!parser.isClosed()){
				JsonToken jsonToken = parser.nextToken();
				if(JsonToken.FIELD_NAME.equals(jsonToken)){
					String fieldName = parser.getCurrentName();
					parser.nextToken();
					if (fieldName.equals("icon")) {
						document.put("DATE_UPDATED", date);
						//store
						userCollection.insertOne(document);
						document = new Document();
					}
					else if (FIELD_MAP.containsKey(fieldName)) {
						document.put(FIELD_MAP.get(fieldName), parser.getValueAsString());
					}
				}
			}
			System.out.println("Station data updated at: " + date);
		}
		catch (IOException ioe) {
			//do nothing
		}

	}
}
