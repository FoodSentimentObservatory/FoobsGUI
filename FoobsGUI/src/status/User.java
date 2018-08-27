package status;

public class User {
	
	private String username  = ""; 
	private String department  = ""; 
	private String organisation  = ""; 
	
	public User (String username, String department, String organisation ) {
		this.username = username;
		this.department = department;
		this.organisation= organisation;
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

}
