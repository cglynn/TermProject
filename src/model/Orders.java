package model;

import java.util.ListIterator;
import java.util.Vector;

public class Orders {

    /**
   * 
   * @element-type Order
   */
  public Vector<Order>  orders;

  public void orders() {
  }
  
  public ListIterator<Order> getOrders()
  {
	  return orders.listIterator();
  }

}