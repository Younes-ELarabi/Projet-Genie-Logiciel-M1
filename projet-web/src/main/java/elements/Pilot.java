package elements;


public class Pilot extends User{

	/**
	 * (written in the project article,) the experiences of the pilot
	 */
	private String experience;
	
	/**
	 * (written in the project article,) the qualifications of the pilot
	 */
	private String qualification;
	
	public Pilot(String id,String userName, String passWord, String name, String birthDate, String experience,String qualification,String mail) {
		super(id,userName, passWord, name, birthDate,mail);
		this.experience = experience;
		this.qualification = qualification;
	}
	
	public Pilot() {
		super("id","test", "test","test", "test","test");
	}

	public String getExperience() {
		return experience;
	}

	public String getQualification() {
		return qualification;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	@Override
	public String toString() {
		return "Pilot [userName=" + this.getUserName() + ", passWord=" + this.getPassWord() + ", name=" + this.getName()
		+ ", birthDate=" + this.getBirthDate() + " , experience=" + this.getExperience() + " , qualification=" + this.getQualification() +"]";
	}
	
}
