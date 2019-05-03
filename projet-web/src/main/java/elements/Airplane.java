package elements;



public class Airplane {
	//=================================attributes==============================
	/**
	 * the id of the plane
	 */
	private int id ;
	/**
	 * the model of the plane
	 */
	private String model;
	/**
	 * the seat number of the plane
	 */
	private int nbSeat;
	
	//=================================Constructors==============================
	public Airplane(int id, String model, int nbSeat) {
		super();
		this.id = id;
		this.model = model;
		this.nbSeat = nbSeat;
	}
	
	// =============================== the methods ================================
	
	//-------------------------------getters and setters---------------------------- 
	
	public Airplane() {
		this.id = 1;
		this.model = "test-model";
		this.nbSeat = 10;
	}

	public Airplane(String model) {
		this.id = 1;
		this.model = model;
		this.nbSeat = 1000;
	}
	public int getId() {
		return id;
	}
	
	public String getModel() {
		return model;
	}
	public int getNbSeat() {
		return nbSeat;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setNbSeat(int nbSeat) {
		this.nbSeat = nbSeat;
	}

	@Override
	public String toString() {
		return "Airplane [id=" + id + ", model=" + model + ", nbSeat=" + nbSeat + "]";
	}
	
	
	//-----------------------------------------------------------------------------------------
	
	

}
