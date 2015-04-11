package model;

public class User {
	
	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private boolean loggedIn;
	public String email;
	public String phoneNumber;
	public Integer userType ;
	public Integer routingNumber;
	public Integer accountNumber;
	public String url;
	public String companyName;
	public Integer isDeleted;
	public Address mailingAddress;

	
	//Default constructor
	public User()
	{
		userId = -1;
		userName = "";
		firstName = "";
		lastName = "";
		userType = 0;
		loggedIn = false;
		email = "";
		phoneNumber = "";
		routingNumber = -1;
		accountNumber = -1;
		url = "";
		companyName = "";
		isDeleted = 0;
		mailingAddress = new Address();
	}
	
	//Constructor setting properties
	public User(int userId, String userName, String firstName, String lastName, int userType, boolean loggedIn, String email, String phoneNumber, int routingNumber, int accountNumber, 
			String url, String companyName, int isDeleted, Address address)
	{
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
		this.loggedIn = loggedIn;
		
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.routingNumber = routingNumber;
		this.accountNumber = accountNumber;
		this.url = url;
		this.companyName = companyName;
		this.isDeleted = isDeleted;
		mailingAddress = address;
	}
	
	//Public Get/Set methods
	public int getUserId()
	{
		return userId;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn)
	{
		this.loggedIn = loggedIn;
	}
	
	  public String getEmail() {
		  return email;
	  }

	  public String getPhoneNumber() {
		  return phoneNumber;
	  }

	  public int getUserType() {
		  return userType;
	  }

	  public int getRoutingNumber() {
		  return routingNumber;
	  }

	  public int getAccountNumber() {
		  return routingNumber;
	  }

	  public String getUrl() {
		  return url;
	  }

	  public String getCompanyName() {
		  return companyName;
	  }

	  public int getIsDeleted() {
		  return isDeleted;
	  }
	  
	  public Address getMailingAddress() {
		  return mailingAddress;
	  }
	
}