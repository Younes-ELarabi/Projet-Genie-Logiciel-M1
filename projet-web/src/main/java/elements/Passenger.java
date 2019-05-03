package elements;

public class Passenger extends User {

	public Passenger(String id,String userName, String passWord, String name, String birthDate, String mail) {
		super(id,userName, passWord, name, birthDate, mail);
		// TODO Auto-generated constructor stub
	}

	public Passenger() {
		// TODO Auto-generated constructor stub
		super("id","test", "test","test","test","test");
	}
	
	@Override
	public String toString() {
		return "Passenger [userName=" + this.getUserName() + ", passWord=" + this.getPassWord() + ", name=" + this.getName()
		+ ", birthDate=" + this.getBirthDate() +"]";
	}

}
