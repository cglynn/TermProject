package dao;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.List;
import model.ListItem;
import model.Product;
import model.Catalog;
import model.ReviewsRanking;
import model.ProductSeller;
import model.Image;

import java.util.Vector;
import java.util.ListIterator;

import enums.ListType;

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
  
  //returns listItemId if exists otherwise -1
  public int doesItemExistInList( int listId, ProductSeller productSeller) throws SQLException
  {
	  int listItemId = -1;
	  
		//create query
		String sql = "SELECT listItemId FROM listitem l join productSeller p on l.sellerId = p.sellerId and l.productId = p.productId WHERE listId = ? AND l.sellerId = ? AND l.productId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, listId);
		connection.ps.setInt(2, productSeller.getSellerId());
		connection.ps.setInt(3, productSeller.getProductId());
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				listItemId = connection.result.getInt(1);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
			  
	  return listItemId;
  }

//Add Item to object list  
public void addItemToList(List list, ListItem item) throws SQLException
{
	//load items
	list.listItem = new Vector<ListItem>();
	
	//create query
	String sql = "SELECT listItemId, listId, productId, price quantity FROM listItem WHERE listId = ?";
	
	//create connection
	connection = new ConnectionInfo();
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, list.getListId());
	
	connection.executeQuery();
	
	try {
		while(connection.result.next()) 
		{
			ListItem itemx = new ListItem(connection.result.getInt(1),connection.result.getInt(2),connection.result.getInt(3), connection.result.getDouble(4),connection.result.getInt(5));
			list.listItem.add(itemx);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
}
  
  //Add Product to Shopping Cart.  If item already exists update quantity.
  public void addProductShoppingCart(int listId, int productSellerId) throws SQLException {
		//create connection
		connection = new ConnectionInfo();

		ProductSeller productSeller = getProductSeller(productSellerId);
		
		int listItemId = doesItemExistInList(listId, productSeller);
		
		//If Entry already exists, update entry.
		if(listItemId != -1)
		{
		
			//create query
			String sql = "UPDATE listItem SET quantity=quantity+1 WHERE listItemId = ?";
		
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql);
			
			//set variable in prepared statement
			connection.ps.setInt(1, listItemId);
			
			int affectedRows = connection.ps.executeUpdate();
			
	        if (affectedRows == 0) {
	            throw new SQLException("Updating Shopping Cart Failed, no rows affected.");
	        }
		}
		//Else need to insert new row.
		else
		{
			//create query
			String sql = "Insert Into listItem (quantity, sellerId, productId, listId) Values (?,?,?,?) ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			
			//set variable in prepared statement
			connection.ps.setInt(1, 1);
			connection.ps.setInt(2, productSeller.getProductSellerId());
			connection.ps.setInt(3, productSeller.getProductId());
			connection.ps.setInt(4, listId);
			
	        int affectedRows = connection.ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Updating shopping Cart failed..");
	        }
		}
  }
  
  //returns productSeller object from productSellerID.
  public ProductSeller getProductSeller(int productSellerId) throws SQLException
  {
	  ProductSeller productSeller = null;
	  
		//create query
		String sql = "SELECT sellerId, price, shippingCost, productId, companyName FROM productSeller p JOIN user u on p.sellerId = u.userId WHERE productSellerId = ?  ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, productSellerId);
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				 productSeller= new ProductSeller( productSellerId, connection.result.getInt(1), connection.result.getDouble(2), connection.result.getDouble(3), connection.result.getInt(4),connection.result.getString(5));
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
	  
	  return productSeller;
  }
  
  //returns wish list, else null.
  public List getWishList(int userId) throws SQLException 
  {
	  List wishList = null;

		//create query
		String sql = "SELECT listId FROM list WHERE ownerId = ? AND listType = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);
		connection.ps.setInt(2, ListType.wish.value);
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				wishList = new List(connection.result.getInt(1), ListType.wish.value,userId, -1);
				
				//Load items of List
				ListIterator<ListItem> listIterator = wishList.getListItems();
				if(listIterator != null){
					while(listIterator.hasNext())
					{
						ListItem item = listIterator.next();
						addItemToList(wishList, item);
					}
				}
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
	  
	  
	  return wishList;
  }

  
  //returns shopping Cart, else null.
  public List getShoppingCart(int userId) throws SQLException
  {
	  List shoppingList = null;

		//create query
		String sql = "SELECT listId FROM list WHERE ownerId = ? AND listType = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);
		connection.ps.setInt(2, ListType.shoppingCart.value);
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				shoppingList = new List(connection.result.getInt(1), ListType.shoppingCart.value,userId, -1);
				
				//Load items of List
				ListIterator<ListItem> listIterator = shoppingList.getListItems();
				if(listIterator != null)
				{
					while(listIterator.hasNext())
					{
						ListItem item = listIterator.next();
						addItemToList(shoppingList, item);
					}
				}
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
	  
	  
	  return shoppingList;
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