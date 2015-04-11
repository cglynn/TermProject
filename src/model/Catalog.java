package model;

import java.util.ListIterator;
import java.util.Vector;

public class Catalog {

    public Vector<Product>  products;


  public void catalog() {
	  //this.products = new Vector<Product>();
  }
  
  public ListIterator<Product> getProducts()
  {
	  return products.listIterator();
  }

}