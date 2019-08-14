package interfaces;

import java.io.IOException;
import java.util.List;

import elements.Reservation;



/**
 * functions that needed in ReservationDao
 * 
 *
 */
public interface ReservationDao extends GenericDao<Reservation>{

	
	
	/**
	 * all reservations that a pilot received
	 * @param pilot_id
	 * @return list of reservations with the same pilot
	 * @throws IOException 
	 */
	public List<Reservation> searchPilotReservation(String pilot_id) throws IOException;
	
	
	/**
	 * all reservations that a passenger booked
	 * @param passenger_id
	 * @return list of reservations with the same passenger
	 * @throws IOException 
	 */
	public List<Reservation> searchPassengerReservation(String passenger_id) throws IOException;

	
	/**
	 * add a reservation to the data base and give this reservation an id
	 * @param id the id of reservation for database
	 * @param e reservation that need to be added
	 * @return the result
	 * @throws Exception
	 */
	String addElement(String id, Reservation e) throws Exception;


	/**
	 * approver a reservation
	 * @param e reservation that needed to be approved
	 * @return resultat
	 * @throws Exception
	 */
	String agree(Reservation e) throws Exception;


	/**
	 * refuse a reservation
	 * @param e reservation that needed to be refused
	 * @return the result
	 * @throws Exception
	 */
	String refuse(Reservation e) throws Exception;

	/**
	 * search a reservation by its id
	 * @param id
	 * @return reservation
	 * @throws Exception
	 */
	Reservation searchReservation(String id) throws Exception;

}