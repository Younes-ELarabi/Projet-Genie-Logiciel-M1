package interfaces;

import java.util.List;

import elements.Airplane;
import elements.Airport;
import elements.Flight;
import elements.Passenger;


/**
 * 
 * The functions that we may use in FlightDao
 *
 */
public interface FlightDao extends GenericDao<Flight> {
	
	//====================================== different ways of searching flights =======================================================
	/**
	 * Search a fight by its id
	 * @param id
	 * @return Flight
	 * @throws Exception
	 */
	public Flight searchFlight(String id) throws Exception;
	
	/**
	 * Search a list of flight by its airport name
	 * @param airport_name
	 * @return Flight list
	 */
	public List<Flight> searchFlightAirport(String airport_name);
	
	/**
	 * Search a list of flight by its airport name and its date
	 * @param airport_name
	 * @param date
	 * @return Flight liste
	 */
	public List<Flight> searchFlightAirportAndDate(String airport_name,String date);
	
	
	public List<Flight> searchFlight(String departureAirport, String duree, String departureDate) throws Exception;
	public List<Flight> searchFlightDuree(String duree) throws Exception;
	
	/**
	 * Search a list of flight by its departure date
	 * @param departureDate
	 * @return Flight list
	 * @throws Exception
	 */
	public List<Flight> searchFlightDeparDate(String departureDate) throws Exception;
	public List<Flight> searchFlightPlane(Airplane plane) throws Exception;
	public List<Flight> searchFlightArrivAirport(Airport arrivAirport) throws Exception;
	
	/**
	 * Search a list of flight by its pilot
	 * @param p
	 * @return Flight list
	 * @throws Exception
	 */
	public List<Flight> searchFlightPliot(String p) throws Exception;
	
	/**
	 * Search a list of flight by its passenger
	 * @param p
	 * @return Flight list
	 * @throws Exception
	 */
	public List<Flight> searchFlightPassenger(String p) throws Exception;
	public List<Flight> searchFlightNbSeat(int nb) throws Exception;
	public List<Flight> searchFlightPrice(double price) throws Exception;
	
	/**
	 * Add a passenger to a flight
	 * @param p passenger
	 * @param f flight
	 * @return true if success, false if fail
	 */
	public boolean addPassengerToFlight(Passenger p,Flight f);
	
	/**
	 * Search a list of flight by its location
	 * @param location
	 * @return Flight list
	 * @throws Exception
	 */
	public List<Flight> searchFlightLocation(String location) throws Exception;
	
	/**
	 * Add a flight to the data base and give this flight an id
	 * @param id the id of this flight in data base
	 * @param e flight that need to be added
	 * @return the result
	 * @throws Exception
	 */
	String addElement(String id, Flight e) throws Exception;
	
	
}