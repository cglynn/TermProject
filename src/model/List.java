package model;

import java.util.Vector;
import java.util.ListIterator;
import model.ListItem;

public class List {

  public Integer listType;

  public Integer ownerId;

  public Integer listId;

  public Integer orderId;

    /**
   * 
   * @element-type ListItem
   */
  public Vector<ListItem>  listItem;

  public List() {
	  this.listType = -1;
	  this.ownerId = -1;
	  this.listId = -1;
	  this.orderId = -1;
	  this.listItem = new Vector<ListItem>();
  }
  
  public List(int listId, int listType, int ownerId, int orderId) {
	  this.listType = listType;
	  this.ownerId = ownerId;
	  this.listId = listId;
	  this.orderId = orderId;
	  this.listItem = new Vector<ListItem>();
  }

  public int getListType() {
	  return listType;
  }

  public int getOwnerId() {
	  return ownerId;
  }

  public int getListId() {
	  return listId;
  }

  public int getOrderId() {
	  return orderId;
  }
  
  public ListIterator<ListItem> getListItems(){
  	return listItem.listIterator();
  }

}