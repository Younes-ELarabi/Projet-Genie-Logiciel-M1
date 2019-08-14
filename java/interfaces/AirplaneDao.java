package interfaces;

import java.util.List;

import elements.*;

/**
 * 
 * The functions that we may use in AirplaneDao
 *
 */
public interface AirplaneDao extends GenericDao<Airplane>{

	/**
	 * search a specific airplane via its id
	 * @param id the id of the airplane
	 * @return the airplane that we want
	 * @throws Exception if we can't find such a plane
	 */
	public Airplane searchePlane(int id) throws Exception;
	
	
	/**
	 * search a list of airplanes via a certain model
	 * @param model the model of the airplane
	 * @return the list of the airplane that we want
	 * @throws Exception if we can't find such a list
	 */
	public List<Airplane> searchePlane(String model) throws Exception;


}
