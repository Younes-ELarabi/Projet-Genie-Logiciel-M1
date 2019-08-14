package interfaces;

import java.util.List;

import elements.Airport;


/**
 * 
 * The functions that we may use in AirportDao
 *
 */
public interface AirportDao extends GenericDao<Airport>{
	
	/**
	 * Search a list of airport by its name
	 * @param name
	 * @return Airport List
	 * @throws Exception
	 */
	public List<Airport> searcheAirportByName(String name) throws Exception;
	
	/**
	 * Search a list of airport by its icao code
	 * @param code
	 * @return Airport list
	 * @throws Exception
	 */
	public List<Airport> searcheAirportByCode(String code) throws Exception;
	
	/**
	 * Search a list of airport by its location
	 * @param location
	 * @return Airport list
	 * @throws Exception
	 */
	public List<Airport> searcheAirportByLocation(String location) throws Exception;
	
}
