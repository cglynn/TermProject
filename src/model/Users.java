package model;

import java.util.ListIterator;
import java.util.Vector;

public class Users {

    /**
   * 
   * @element-type Message
   */
  public Vector<User>  users;

  public void messages() {
  }
  
  public ListIterator<User> getUsers()
  {
	  return users.listIterator();
  }

}