package jetty_jersey.ws;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import elements.Flight;
import elements.Passenger;
import elements.Pilot;
import elements.Reservation;
import elements.User;


/**
 * 
 * Web services
 *
 */
@Provider
@javax.inject.Singleton 
@Path("service")
public class Service extends GeneralService implements ContainerResponseFilter{
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		responseContext.getHeaders().add(
	            "Access-Control-Allow-Origin", "*");
	    responseContext.getHeaders().add(
	            "Access-Control-Allow-Credentials", "true");
	    responseContext.getHeaders().add(
	           "Access-Control-Allow-Headers",
	           "origin, content-type, accept, authorization");
	    responseContext.getHeaders().add(
	            "Access-Control-Allow-Methods", 
	            "GET, POST, PUT, DELETE, OPTIONS, HEAD");
	}
		
	
	public String[] welcomeMessage(String name) {
		String[] s = {"Welcome in our flight sharing website !","Hello "+name+",\n" + 
				"You have just created your account on ourflight sharing site and we thank you for it.\n" + 
				"We hope that you will find in our service the answer to your expectations, which we know that it is important and you should know that we are constantly working to improve your planepooling experience.\n" + 
				"Thank you."+
				"Groupe 2 of 'Génie Logiciel'"};
		return s;
	}
	
	public String[] reservatonApprovedMessage(String name) {
		String[] s = {"Congratuations, one of your reservations has been approved !","Hello "+name+",\n" + 
				"one of your reservation has been approved .\n You can find more information in your account.\n" + 
				"Thank you."+
				"Groupe 2 of 'Génie Logiciel'"};
		return s;
	}
	public String[] reservatonDisapprovedMessage(String name) {
		String[] s = {"Sorry, one of your reservation has been disapproved!","Hello "+name+",\n" + 
				"one of your reservation has been canceled or refused.\n You can find more information in your account.\n" + 
				"Sorry."+
				"Groupe 2 of 'Génie Logiciel'"};
		return s;
	}
	public String[] addReservationMessage(String name) {
		String[] s = {"A new Reservation Request of your flight! ","Hello "+name+",\n" + 
				"There's a new request of your flightn please check your account for more information.\n" + 
				"Best,"+
				"Groupe 2 of 'Génie Logiciel'"};
		return s;
	}
	public String[] flightReminder() {
		String[] s = {"Next day flight reminder","We want to remind you that you have booked a flight for tomorrow.\n"
				+ "You can read more about it in your own account.\n"
				+ "Reply us at this mail adress if you have any question."
				+ "Thank you."};
		return s;
	}

	
	/**
	 * web service for login with username and password
	 * @param username
	 * @param password
	 * @return Reponse
	 * @throws Exception
	 */
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/x-www-form-urlencoded")
	public Response login(@FormParam("usr") String username, @FormParam("pwd") String password) throws Exception {
		User user = null;
		String msg = "{\"result\":\"ok\"}";
		int life = 1000000;
		if ( (user = pilotDao.searchPilot(username,password)) != null) {
			if ( user.getPassWord().equals(password)) {
				String jwt = jwtModule.createJWT(user.getId(),username, "pilot", life);
				NewCookie cookie = new NewCookie("cookie",jwt,"/","","",life,false);
				System.out.println("Found user  :"+user);
				return Response.status(200).entity(msg).cookie(cookie).build();
			}
		}else if ( (user = passangerDao.searchPassenger(username,password)) != null) {
			if ( user.getPassWord().equals(password)) {
				String jwt = jwtModule.createJWT(user.getId(),username, "passenger", life);
				NewCookie cookie = new NewCookie("cookie",jwt,"/","","",life,false);
				System.out.println("Found user  :"+user);
				return Response.status(200).entity(msg).cookie(cookie).build();
			}
		}
		return Response.status(404).entity("{\"result\":\"no\"}").build();
	}
	
	
	/**
	 * web service for account creation
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/createAccount")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAccount(Map<String,String> profile) throws Exception {
		//check if userName exists
		if( !pilotDao.consultElement("userName", profile.get("userName")).isEmpty() || !passangerDao.consultElement("userName", profile.get("userName")).isEmpty() ) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		Set<String> keys =  profile.keySet();
		User user = null;
		if (keys.contains("Pilot")) {
			if( profile.get("Pilot").equals("on")) {
				String id = UUID.randomUUID().toString().substring(0, 7);
				user = new Pilot(
						id,
						profile.get("userName"),
						profile.get("passWord"),
						profile.get("userName"),
						profile.get("birthDate")
						,null,null,
						profile.get("email")
						);
				System.out.println(pilotDao.addElement((Pilot)user));
				new EmailSender(user.getEmail(),welcomeMessage(user.getName())[0],welcomeMessage(user.getName())[1]);
				//return (Pilot)user;
				return Response.ok("OK").build();
			}
		}else {
				String id = UUID.randomUUID().toString().substring(0, 7);
			    user = new Passenger(
			    		id,
			    		profile.get("userName"),
			    		profile.get("passWord"),
			    		profile.get("userName"), 
			    		profile.get("birthDate"), 
			    		profile.get("email"));
			    System.out.println(passangerDao.addElement((Passenger)user));
			    new EmailSender(user.getEmail(),welcomeMessage(user.getName())[0],welcomeMessage(user.getName())[1]);
			    //return (Passenger)user;
			    return Response.ok("OK").build();
		}
		//return user;
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	/**
	 * Edit user account 
	 * @param newProfile
	 * @throws Exception 
	 */
	@POST
	@Path("/editAccount")
	@Consumes(MediaType.APPLICATION_JSON)
	public void editAccount(Map<String,String> profile,@CookieParam("cookie") String cookie) throws Exception {
		if(cookie != null) {
			Map<String,Object> user_details = jwtModule.parseJWT(cookie);
			String role = (String ) user_details.get("role");
			if ( role.equals("pilot") ) {
				Pilot user = new Pilot(
						(String) user_details.get("id"),
						profile.get("userName"),
						profile.get("passWord"),
						profile.get("name"),
						profile.get("birthDate"),
						profile.get("qualification"),
						profile.get("experience"),
						profile.get("email")
						);
				pilotDao.modifyElement((String) user_details.get("id"), user);
			}//else it's a passenger for sure
			Passenger newPassenger =  new Passenger(
		    		(String) user_details.get("id"),
		    		profile.get("userName"),
		    		profile.get("passWord"),
		    		profile.get("name"), 
		    		profile.get("birthDate"), 
		    		profile.get("email"));
			passangerDao.modifyElement((String) user_details.get("id"), newPassenger);
		}
	}
	
	
	
	/**
	 * web service for searching flights by its airport
	 * @param airport
	 * @return
	 */
	@GET
	@Path("getFlightAiport/{airport}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Flight> getFlightsByAirport(@PathParam(value = "airport") String airport){
		try {
			return flightDao.consultElement("location",airport);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * web service for getting a list of flights with a certain departure date
	 * @param date departure date of flight
	 * @return Flight list
	 */
	@GET
	@Path("getFlightDate/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Flight> getFlightsByDate(@PathParam(value = "date") String date){
		try {
			return flightDao.consultElement("departureDate",date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * web service for getting a list of flights with a certain departure date and a certain departure location
	 * @param date departure date
	 * @param location departure location
	 * @return Flight list
	 * @throws Exception
	 */
	@GET
	@Path("getFlight/{date}/{location}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Flight> getFlights(@PathParam(value ="date") String date,@PathParam(value = "location") String location) throws Exception{
		List<Flight> results = null;
	
		results = flightDao.consultElement(date,location);
		if( results.isEmpty()) {	
			results=flightDao.searchFlightDeparDate(date);
			if (results.isEmpty()) {
				results=flightDao.searchFlightLocation(location);
			}	
		}
		return results;
		
	}
	
	
	/**
	 * web service for getting a flight by its flight id
	 * @param flight_id
	 * @return Flight
	 * @throws Exception
	 */
	@GET
	@Path("getFlight/{flight_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Flight getFlight(@PathParam(value ="flight_id") String flight_id) throws Exception{
		Flight result = null;
	
		result= flightDao.searchFlight(flight_id);
		
		return result;
		
	}
	
	
	/**
	 * web service for getting a list of flights with a certain pilot username
	 * @param pilot_username
	 * @return Flight list
	 * @throws Exception
	 */
	@GET
	@Path("getFlightPilot/{pilot_username}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Flight> getFlightPilot(@PathParam(value ="pilot_username") String pilot_username) throws Exception{
		List<Flight>result = null;
		result= flightDao.searchFlightPliot(pilot_username);
		return result;
		
	}
	
	/**
	 * web service for getting a list of flights with a certain passenger username
	 * @param passenger_username
	 * @return Flight list
	 * @throws Exception
	 */
	@GET
	@Path("getFlightPassenger/{passenger_username}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Flight> getFlightPassenger(@PathParam(value ="passenger_username") String passenger_username) throws Exception{
		List<Flight>result = null;
		result= flightDao.searchFlightPassenger(passenger_username);
		return result;
		
	}
	
	/**
	 * web service for booking flight with a flight id
	 * @param fid flight id
	 * @param cookie
	 * @return true if success, false if fail
	 * @throws Exception
	 */
	@POST
	@Path("bookFlight/{flight_id}")
	public boolean bookFlight(@PathParam(value="flight_id")String fid,@CookieParam("cookie") String cookie) throws Exception {
		Map<String,Object> user_details = jwtModule.parseJWT(cookie);
		Passenger passenger = passangerDao.searchPassenger((String)user_details.get("id"));
		Flight flight = flightDao.searchFlight(fid);
		return flightDao.addPassengerToFlight(passenger, flight);
	}
	
	
	/**
	 * web service for getting the pilot with a pilot username
	 * @param pilot_username
	 * @return Pilot
	 * @throws Exception
	 */
	@GET
	@Path("getPilot/{pilot_username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Pilot getPilot(@PathParam(value ="pilot_username") String pilot_username) throws Exception{
		Pilot result = null;
	
		result= pilotDao.searchPilotByUsername(pilot_username);
		
		return result;
		
	}
	
	/**
	 * web service for getting account information of a user
	 * @param cookie
	 * @return User
	 * @throws Exception
	 */
	@GET
	@Path("getAccountInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public User getAccountInfo(@CookieParam("cookie") String cookie) throws Exception{
		
		if(cookie != null) {
			Map<String,Object> user_details = jwtModule.parseJWT(cookie);
			if(user_details.get("role").equals("passenger")) {
				Passenger passenger = passangerDao.searchPassenger((String)user_details.get("id"));
				return passenger;
			}
			if(user_details.get("role").equals("pilot")) {
				Pilot pilot = pilotDao.searchPilot((String)user_details.get("id"));
				return pilot;
			}
		}
		return null;
		
	}
	
	/**
	 * web service for getting a passenger account information
	 * @param cookie
	 * @return Passenger
	 * @throws Exception
	 */
	@GET
	@Path("getPassengerAccountInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public Passenger getPassengerAccountInfo(@CookieParam("cookie") String cookie) throws Exception{
		
		if(cookie != null) {
			Map<String,Object> user_details = jwtModule.parseJWT(cookie);
			if(user_details.get("role").equals("passenger")) {
				Passenger passenger = passangerDao.searchPassenger((String)user_details.get("id"));
				return passenger;
			}
			
		}
		return null;
		
	}
	
	/**
	 * web service for distinguishing the type of user: pilot or passenger
	 * @param cookie
	 * @return String
	 * @throws Exception
	 */
	@GET
	@Path("usertype")
	@Produces(MediaType.APPLICATION_JSON)
	public String type(@CookieParam("cookie") String cookie) throws Exception {
		if(cookie != null) {
			String r= (String)jwtModule.parseJWT(cookie).get("role");
			System.out.println(r);
			return "{\"role\" :\" "+ r+"\"}";
			//return r;
		}
		return null;
	}

	
	/**
	 * web service by getting a flight by its id
	 * @param id
	 * @return Flight
	 */
	@GET
	@Path("consultFlight/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Flight consultFlight(@PathParam(value ="id") String id) {
		Flight flight = null;
		try {
			 flight = flightDao.searchFlight(id);
			 flightDao.consultElement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flight;
		//return new Flight();
	}
	
	
	/**
	 * web service for adding a flight in the data base
	 * @param flight
	 * @return String
	 * @throws Exception
	 */
	@PUT
	@Path("/addFlight")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addFlight(Flight flight) throws Exception {
		System.out.println(flight);
		List<Passenger> pas=new LinkedList<Passenger>();
		flight.setPassengers(pas);
		flightDao.addElement(flight);
		return "{\"result\" :\" ok\"}";
	}
	
	/**
	 * web service for editing a flight information which add a new passenger to this flight
	 * @param pid passenger id 
	 * @param fid flight id
	 * @return Flight
	 * @throws Exception
	 */
	@PUT
	@Path("editFlight/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Flight editFlight(@PathParam(value="id")String pid,String fid) throws Exception {
		Flight f = flightDao.searchFlight(fid);
		Passenger p = passangerDao.searchPassenger(pid);
		flightDao.addPassengerToFlight(p,f);
		return flightDao.searchFlight(fid);
	}
	
	/**
	 * web service for getting all the passengers of one flight 
	 * @param id_flight
	 * @return Passenger list
	 */
	@GET
	@Path("getPassengers/{id_flight}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Passenger> getPassengers(@PathParam(value ="id_flight") String id_flight){
		List<Passenger> passengers = null;
		try {
			passengers = passangerDao.consultElement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return flight.getPassenger();
		return passengers;
	}
	
	
	/**
	 * web service for deleting a flight
	 * @param id flight id
	 * @return boolean
	 */
	@DELETE
	@Path("deleteFlight/{id}")
	public boolean deleteFlight(@PathParam(value="id") String id) {
		return flightDao.deleteElement(id);
	}
	
	
	/**
	 * web service for adding a new reservation 
	 * @param r reservation that need to be added
	 * @return String 
	 * @throws Exception
	 */
	@PUT
	@Path("addReservation")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addReservation(Reservation r) throws Exception {
		if(r!=null) {
			new EmailSender(r.getFlight().getPilot().getEmail(),addReservationMessage(r.getFlight().getPilot().getName())[0],addReservationMessage(r.getFlight().getPilot().getName())[1]);

			reservationDao.addElement(r);
			return "{\"result\" :\" succes\"}";
		}
		return "{\"result\" :\" fail\"}";
	}
	
	
	/**
	 * web services for getting all the reservations of a passenger
	 * @param username_passenger
	 * @return Reservation list
	 */
	@GET
	@Path("getReservation_Passenger/{username_passenger}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Reservation> getReservation_Passenger(@PathParam(value ="username_passenger") String username_passenger){
		List<Reservation> reservations = null;
		try {
			reservations = reservationDao.searchPassengerReservation(username_passenger);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return flight.getPassenger();
		return reservations;
	}
	
	
	/**
	 * web service for getting all reservations of a pilot
	 * @param username_pilot
	 * @return Reservation list
	 */
	@GET
	@Path("getReservation_Pilot/{username_pilot}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Reservation> getReservation_Pilot(@PathParam(value ="username_pilot") String username_pilot){
		List<Reservation> reservations = null;
		try {
			reservations = reservationDao.searchPilotReservation(username_pilot);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reservations;
	}
	
	
	/**
	 * web service for approving a reservation
	 * @param e Reservation that need to be approved
	 * @throws Exception
	 */
	@POST
	@Path("agreeReservation/")
	public void agreeReservation(Reservation e) throws Exception {
		new EmailSender(e.getpassenger().getEmail(),reservatonApprovedMessage(e.getpassenger().getName())[0],reservatonApprovedMessage(e.getpassenger().getName())[1]);
		reservationDao.agree(e);
	}
	
	/**
	 * web service for refusing a reservation
	 * @param e reservation that needed to be refused
	 * @throws Exception
	 */
	@POST
	@Path("disagreeReservation/")
	public void disagreeReservation(Reservation e) throws Exception {
		new EmailSender(e.getpassenger().getEmail(),reservatonDisapprovedMessage(e.getpassenger().getName())[0],reservatonDisapprovedMessage(e.getpassenger().getName())[1]);
		reservationDao.refuse(e);
	}
	
	/**
	 * web service for getting a reservation by its id
	 * @param id resevation id
	 * @return Reservation
	 * @throws Exception
	 */
	@GET
	@Path("getReservation/{id}")
	public Reservation getReservation(@PathParam(value="id") String id)  throws Exception {
		return reservationDao.searchReservation(id);
		
	}
	
	
}