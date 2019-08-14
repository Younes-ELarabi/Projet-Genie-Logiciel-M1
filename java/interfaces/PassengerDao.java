package interfaces;

import elements.Passenger;

/**
 * functions that needed in PassengerDao
 * 
 *
 */
public interface PassengerDao extends GenericDao<Passenger> {
	/**
	 * search a passenger by his id 
	 * @param id the id of the passenger
	 * @return Passenger
	 * @throws Exception if we can't find such a passenger
	 */
	 public Passenger searchPassenger(String id) throws Exception;
	 
	 /**
	  * search a passenger by his user name and his password 
	  * @param username
	  * @param password
	  * @return Passenger
	  * @throws Exception if we can't find such a passenger
	  */
	 public Passenger searchPassenger(String username,String password) throws Exception;
}
