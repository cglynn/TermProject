package model;


public class ProductSeller {

  public int sellerId;

  public double price;

  public double shippingCost;

  public int productSellerId;

  public int productId;


  public int getSellerId() {
	  return sellerId;
  }

  public double getPrice() {
	  return price;
  }

  public double getShippingCost() {
	  return shippingCost;
  }

  public  ProductSeller(int productSellerId, int sellerId, double price, double shippingCost, int productId) {
	  this.sellerId = sellerId;
	  this.price  = price;
	  this.shippingCost = shippingCost;
	  this.productSellerId = productSellerId;
	  this.productId = productId;
  }
  
  public  ProductSeller() {
	  this.sellerId = -1;
	  this.price  = 0;
	  this.shippingCost = 0;
	  this.productSellerId = -1;
	  this.productId = -1;
  }

  public int getProductSellerId() {
	  return productSellerId;
  }

  public int getProductId() {
	  return productId;
  }

}