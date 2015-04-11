package model;



public class Address {


	
  public String street;

  public String city;

  public String state;

  public int zip;

  public Address() {
		city = "";
		state = "";
		street = "";
		zip = -1;
  }
  
  public Address(String city, String state, String street, int zip){
	  this.city = city;
	  this.state = state;
	  this.street = street;
	  this.zip = zip;  
  }


  public String getCity() {
	  return city;
  }

  public String getState() {
	  return state;
  }

  public int getZip() {
	  return zip;
  }

}