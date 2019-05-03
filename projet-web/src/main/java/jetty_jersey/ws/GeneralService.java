package jetty_jersey.ws;

import javax.inject.Singleton;


import dao.AirplaneDaoImpl;
import dao.AirportDaoImpl;
import dao.FlightDaoImpl;
import dao.PassengerDaoImpl;
import dao.PilotDaoImpl;
import dao.ReservationDaoImpl;
import jwt.JwtToken;

/**
 * 
 * define and initialize dao implementations and security
 *
 */
@Singleton
public abstract class GeneralService {
	
	
	//DAO
	protected PilotDaoImpl pilotDao = new PilotDaoImpl();
	protected PassengerDaoImpl passangerDao = new PassengerDaoImpl();
	protected AirplaneDaoImpl airplaneDao = new AirplaneDaoImpl();
	protected AirportDaoImpl airportDao = new AirportDaoImpl();
	protected FlightDaoImpl flightDao = new FlightDaoImpl();
	protected ReservationDaoImpl reservationDao = new ReservationDaoImpl();
	//Token  
	protected JwtToken jwtModule = new JwtToken();
	
}
