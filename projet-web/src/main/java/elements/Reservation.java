package elements;

public class Reservation {

	/**
	 * reservation id
	 */
	private String id;
	
	
	/**
	 * reservation flight
	 */
	private Flight flight;
	
	
	/**
	 * the passenger who wants to book a flight
	 */
	private Passenger passenger;
	
	
	/**
	 * the number of seats the user wants to book
	 */
	private int nbseat;

	
	public Reservation() {
		this.id=null;
		this.flight=null;
		this.passenger=null;
		this.nbseat=0;
	}


	public Reservation(String id, Flight flight, Passenger passenger, int nbseat) {
		super();
		this.id = id;
		this.flight = flight;
		this.passenger = passenger;
		this.nbseat = nbseat;
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Flight getFlight() {
		return flight;
	}


	public void setFlight(Flight flight) {
		this.flight = flight;
	}


	public Passenger getpassenger() {
		return passenger;
	}


	public void setpassenger(Passenger passenger) {
		this.passenger = passenger;
	}


	public int getNbseat() {
		return nbseat;
	}


	public void setNbseat(int nbseat) {
		this.nbseat = nbseat;
	}

	
}