package dao;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Product;
import model.Catalog;
import model.ReviewsRanking;
import model.ProductSeller;
import model.Image;

import java.util.Vector;
import java.util.ListIterator;

public class CatalogDAO {

    public ConnectionInfo connection;

  public CatalogDAO()  {
	  		  
  }
  
  public Catalog getCatalog() throws SQLException
  {
	  	Catalog catalog = new Catalog();
		//create connection
		connection = new ConnectionInfo();
		
	  	catalog.products = new Vector<Product>();
		//create query
		String sql = "SELECT productID, name, description, department, isDeleted FROM product WHERE isDeleted = 0";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		connection.executeQuery();

		try {
			while(connection.result.next()) 
			{
				Product product = new Product(connection.result.getInt(1),connection.result.getString(2),connection.result.getString(3),connection.result.getInt(4), connection.result.getInt(5));
				catalog.products.add(product);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
		
		//Load images/sellers/Reviews Rankings for items
		ListIterator<Product> productsIterator = catalog.products.listIterator();
		while(productsIterator.hasNext())
		{
			
			Product product = productsIterator.next();
			
			addImagesToProduct(product);
			
			addSellersToProduct(product);
			
			addRankingsReviewsToProduct(product);
			
		}
		
		
		return catalog;
  }
	
  
private void addImagesToProduct(Product product) throws SQLException
{
	//load images
	product.images = new Vector<Image>();
	
	//create query
	String sql = "SELECT imageId, productId, location FROM image WHERE productID = ?";
	
	//create connection
	connection = new ConnectionInfo();
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, product.productId);
	
	connection.executeQuery();

	try {
		while(connection.result.next()) 
		{
			Image image = new Image(connection.result.getInt(1),connection.result.getInt(2),connection.result.getString(3));
			product.images.add(image);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
}

private void addSellersToProduct(Product product) throws SQLException
{
	//load Sellers
	product.sellers = new Vector<ProductSeller>();
	
	//create query
	String sql = "SELECT productSellerId, sellerId, price, shippingCost, productId, companyName FROM productSeller p JOIN user u on p.sellerId = u.userId WHERE productID = ? ";
	
	//create connection
	connection = new ConnectionInfo();
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, product.productId);
	
	connection.executeQuery();

	try {
		while(connection.result.next()) 
		{
			ProductSeller seller = new ProductSeller(connection.result.getInt(1),connection.result.getInt(2),connection.result.getDouble(3), connection.result.getDouble(4), connection.result.getInt(5), connection.result.getString(6) );
			product.sellers.add(seller);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
}

private void addRankingsReviewsToProduct(Product product) throws SQLException
{
	//load Rankings and Reviews
	product.reviewsRankings = new Vector<ReviewsRanking>();
	
	//create query
	String sql = "SELECT reviewsRankingId, ranking, review FROM reviewsRanking WHERE productID = ? ";
	
	//create connection
	connection = new ConnectionInfo();
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, product.productId);
	
	connection.executeQuery();

	try {
		while(connection.result.next()) 
		{
			ReviewsRanking reviewRankings = new ReviewsRanking(connection.result.getInt(1),connection.result.getInt(2),connection.result.getString(3), product.productId );
			product.reviewsRankings.add(reviewRankings);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
}

//Edit Product Information.  Name/Description/Department.
public void editProduct(int productId, String name, String description, int department) throws SQLException {
	//create connection
	connection = new ConnectionInfo();

	//create query
	String sql = "UPDATE product SET name=?, description=?, department=? WHERE productId = ?";

	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setString(1, name);
	connection.ps.setString(2, description);
	connection.ps.setInt(3, department);
	connection.ps.setInt(4, productId);
	
	int affectedRows = connection.ps.executeUpdate();
	
    if (affectedRows == 0) {
        throw new SQLException("Creating product seller info failed, no rows affected.");
    }
  }

//Edit Product Seller Information. Price/Shipping Cost.
public void editProductSeller(int productId, int sellerId, double price, double shippingCost) throws SQLException, ClassNotFoundException {
	//create connection
	connection = new ConnectionInfo();

	int productSellerId = doesProductSellerExist(productId, sellerId);
	
	//If Entry already exists, update entry.
	if(productSellerId != -1)
	{
	
		//create query
		String sql = "UPDATE productSeller SET price=?, shippingCost=? WHERE productSellerId = ?";
	
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setDouble(1, price);
		connection.ps.setDouble(2, shippingCost);
		connection.ps.setInt(3, productId);
		
		int affectedRows = connection.ps.executeUpdate();
		
        if (affectedRows == 0) {
            throw new SQLException("Creating product seller info failed, no rows affected.");
        }
	}
	//Else need to insert new row.
	else
	{
		//create query
		String sql = "Insert Into productSeller (sellerId, price, shippingCost, productId) Values (?,?,?,?) ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
		
		//set variable in prepared statement
		connection.ps.setInt(1, sellerId);
		connection.ps.setDouble(2, price);
		connection.ps.setDouble(3, shippingCost);
		connection.ps.setInt(4, productId);
		
        int affectedRows = connection.ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating product seller info failed, no rows affected.");
        }
	}
}

//Returns -1 if entry does not exist, else productSellerId
public int doesProductSellerExist(int productId, int sellerId) throws ClassNotFoundException, SQLException {
	int productSellerId = -1;
	
	//create query
	String sql = "SELECT productSellerId FROM productSeller WHERE sellerId = ? AND productId = ? ";
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, sellerId);
	connection.ps.setInt(2, productId);
	
	connection.executeQuery();

	try {
		if(connection.result.next()) 
		{
			productSellerId = connection.result.getInt(1);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return productSellerId;
	}

  public void removeProduct() {
  }

  public void reviewProduct(int productId, int ranking, String review) throws SQLException {
		//create connection
		connection = new ConnectionInfo();

		

			//create query
			String sql = "Insert Into reviewsRanking (productId, ranking, review) Values (?,?,?) ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			
			//set variable in prepared statement
			connection.ps.setInt(1, productId);
			connection.ps.setInt(2, ranking);
			connection.ps.setString(3, review);
			
	        int affectedRows = connection.ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating product review failed, no rows affected.");
	        }
	  
  }

  public void addProductShoppingCart() {
  }

  public void addProductWishList() {
  }

  public void createOrder() {
  }

  public void removeProductShoppingCart() {
  }

  public void removeItemWishList() {
  }

  public void deleteOrder() {
  }

  public void removeProductFromOrder() {
  }
  
  public void deleteProduct(int productId) throws SQLException {
		//create connection
		connection = new ConnectionInfo();

			//create query
			String sql = "UPDATE product set isDeleted = 1 where productId = ? ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql);
			
			//set variable in prepared statement
			connection.ps.setInt(1, productId);
			
	        int affectedRows = connection.ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Deleting Product failed.");
	        }
  }


}