package interfaces;

import elements.Pilot;


/**
 * 
 * functions that needed in PilotDao
 *
 */
public interface PilotDao extends GenericDao<Pilot>{
	/**
	 * search a pilot by his id 
	 * @param id the id of the pilot
	 * @return Pilot
	 * @throws Exception if we can't find such a pilot
	 */
	 public Pilot searchPilot(String id) throws Exception;
	 
	 /**
	  * search a pilot by his user name and his password 
	  * @param username
	  * @param password
	  * @return Pilot
	  * @throws Exception if we can't find such a pilot
	  */
	 public Pilot searchPilot(String username,String password) throws Exception;

	Pilot searchPilotByUsername(String username) throws Exception;
}
