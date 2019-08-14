package elements;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * 
 * This class convert type 'json' to type 'object', and convert type 'object' to type 'json'
 *
 */
public class ConvertClass {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	
	/**
	 * convert object type to json type that needed by elastic search
	 * @param object 
	 * @return the json type that needed by elastic search
	 * @throws JsonProcessingException
	 */
	public static Map<String, Object> objectToJson(Object object) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> data = objectMapper.convertValue(object, Map.class);
		return data;
	}
	
	/**
	 * convert a json type to an object type that we need in java
	 * @param json 
	 * @param type the type of class that we have already defined 
	 * @return the object type we need in java
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static Object jsonToObject(Map<String, Object> json, String type)
			throws JsonParseException, JsonMappingException, IOException {
		switch (type) {
		case "Flight":
			return objectMapper.convertValue(json, Flight.class);
		case "Pilot":
			return objectMapper.convertValue(json, Pilot.class);
		case "Passenger":
			return objectMapper.convertValue(json, Passenger.class);
		case "Airport":
			return objectMapper.convertValue(json, Airport.class);
		case "Airplane":
			return objectMapper.convertValue(json, Airplane.class);
		case "Reservation":
			return objectMapper.convertValue(json, Reservation.class);
		default:
			return null;
		}
	}
	// main for test 
	public static void main(String[] args) throws IOException {
		Object[] table = new Object[5];
		Pilot test = new Pilot();
		Passenger p = new Passenger();
		Flight f = new Flight();
		Airplane plane = new Airplane();
		Airport airport = new Airport();
		table[0] = test;
		table[1] = p;
		table[2] = f;
		table[3] = plane;
		table[4] = airport;
		for ( int i = 0 ; i < 5 ; i++) {
			Map<String, Object> json=null;
			System.out.println("class object : "+table[i].getClass().getName().substring(9));
			try {
				json = ConvertClass.objectToJson(table[i]);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			System.out.println("As a JSON \n"+json.toString()+"\n");
			try {
				String type = table[i].getClass().getName().substring(9);
				System.out.println("As a java Object : \n"+ConvertClass.jsonToObject(json,type)+"\n");
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
