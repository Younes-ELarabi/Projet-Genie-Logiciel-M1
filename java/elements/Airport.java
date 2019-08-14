package elements;


public class Airport {

	//=================================attributes==============================
	/**
	 * the name of the airport
	 */
	private String name;
	/**
	 * the ICAO code of the airport
	 */
	private String icaoCode; 
	/**
	 * the location of the airport
	 */
	private String location;
	//=================================Constructors==============================
	public Airport(String name, String icaoCode, String location) {
		super();
		this.name = name;
		this.icaoCode = icaoCode;
		this.location = location;
	}
	
	// =============================== the methods ================================
	
	//-------------------------------getters and setters---------------------------- 
	
	public Airport() {
		// TODO Auto-generated constructor stub
		super();
		this.name = "airport";
		this.icaoCode = "icaoCode";
		this.location = "location";
	}

	public String getName() {
		return name;
	}
	
	public String getIcaoCode() {
		return icaoCode;
	}
	public String getLocation() {
		return location;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIcaoCode(String icaoCode) {
		this.icaoCode = icaoCode;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Airport [name=" + name + ", icaoCode=" + icaoCode + ", location=" + location + "]";
	}
	
	
	// -----------------------------------------------------------------------------------------
	 
}
