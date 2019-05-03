package elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Flight {

	//=================================attributes==============================
	/**
	 * the flight id
	 */
	private String id; 
	
	/**
	 * the departure date of the flight
	 */
	private String departureDate;
	
	/**
	 * the departure time of the flight
	 */
	private String departureTime;
	
	/**
	 * the airplane of the flight
	 */
	private Airplane plane;
	
	/**
	 * the departure airport of the flight
	 */
	private Airport departureAirport;
	
	/**
	 * the arrival airport of the flight
	 */
	private Airport arrivalAirport;
	
	/**
	 * the pilot of the flight
	 */
	private Pilot pilot;
	
	/**
	 * the passengers of the flight
	 */
	private List<Passenger> passengers;
	
	/**
	 * the available seats of the flight which is initialized by the seat number of the plane
	 */
	private int availableSeat;
	
	/**
	 * the price of each seat 
	 */
	private double price;
	/**
	 * how much time does this flight take
	 */
	private String duree;
	
	private String from;
	//=================================Constructors================================
	public Flight(String id, String departureDate, String departureTime, Airplane plane, Airport departureAirport,
			Airport arrivalAirport, Pilot pilot, List<Passenger> passenger, int availableSeat, double price,
			String duree) {
		super();
		this.id = id;
		this.departureDate = departureDate;
		this.departureTime = departureTime;
		this.plane = plane;
		this.departureAirport = departureAirport;
		this.arrivalAirport=arrivalAirport;
		this.pilot = pilot;
		this.passengers = passenger;
		this.availableSeat = availableSeat;
		this.price = price;
		this.duree = duree;
		this.setFrom(departureAirport.getLocation());
	}
	
	
	// =============================== the methods ================================
	
	public Flight() {
		// TODO Auto-generated constructor stub
		this.id = "1520";
		this.departureDate = "test-departure";
		this.departureTime = "test-time";
		this.plane = new Airplane();
		this.departureAirport = new Airport();
		this.arrivalAirport=new Airport();
		this.pilot = new Pilot();
		this.passengers = new ArrayList<Passenger>();
		this.availableSeat = 10;
		this.price = 10;
		this.duree = "duration";
		
	}

	//-------------------------------getters and setters---------------------------- 
	public String getId() {
		return id;
	}
	
	public String getDepartureDate() {
		return departureDate;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	
	public String getDuree() {
		return duree;
	}
	public void setDuree(String duree) {
		this.duree = duree;
	}
	public Airplane getPlane() {
		return plane;
	}
	public Airport getDepartureAirport() {
		return departureAirport;
	}
	
	public Pilot getPilot() {
		return pilot;
	}
	public List<Passenger> getPassengers() {
		return passengers;
	}
	public int getAvailableSeat() {
		return availableSeat;
	}
	public double getPrice() {
		return price;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	
	public void setPlane(Airplane plane) {
		this.plane = plane;
	}
	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}
	
	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}
	public void setPassengers(List<Passenger> passenger) {
		this.passengers = passenger;
	}
	
	public void setPassenger(Passenger passenger) {
		this.passengers.add(passenger);
	}
	public void setAvailableSeat(int availableSeat) {
		this.availableSeat = availableSeat;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Flight [id=" + id + ", departureDate=" + departureDate + ", departureTime=" + departureTime + ", plane="
				+ plane + ", departureAirport=" + departureAirport + ", arrivalAirport=" + arrivalAirport + ", pilot=" + pilot + ", passengers=" + passengers
				+ ", availableSeat=" + availableSeat + ", price=" + price + ", duree=" + duree + "]";
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Airport getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(Airport arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}
	
	
	
	
	//--------------------------------------------------------------------
	
	
	

}
