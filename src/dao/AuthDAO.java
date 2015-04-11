package dao;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;
import model.Address;


/**
 * @author Chris Glynn
 *
 */
public class AuthDAO {
	
	public ConnectionInfo connection;
		
	public AuthDAO()
	{
		
	}
	
	/**
	 * query database to get password for the referencing user name check against
	 * password passed as a parameter
	 * @param userName
	 * @param password
	 * @return If match userId, else -1
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public int checkUserPass(String userName, String password) throws ClassNotFoundException, SQLException  {
		
			int userId = -1;	

			//create query
			String sql = "SELECT userId FROM user WHERE userName = ? and password = ? AND isDeleted = 0 ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql);
			
			//set variable in prepared statement
			connection.ps.setString(1, userName);
			connection.ps.setString(2, password);
			
			connection.executeQuery();

			try {
				if(connection.result.next()) userId = connection.result.getInt(1); //check if next to make sure next is not NULL!
				}
				catch (SQLException ex) {
				Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
				}
				
			return userId;
		}
	
	/**
	 * query database with userId and return User object
	 * @param userId
	 * @return User
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public User getUserById(int userId) throws ClassNotFoundException, SQLException {
		User user = null;
		Address address = null;
		
		//create query
		String sql = "SELECT userId, userName, firstName, lastName, userType, email, phoneNumber, routingNumber, accountNumber, url, companyName, isDeleted, city, state, street, zip  FROM user WHERE userId = ?";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				address = new Address(connection.result.getString(13),connection.result.getString(14),connection.result.getString(15),connection.result.getInt(16));
			    user = new User(connection.result.getInt(1), connection.result.getString(2), connection.result.getString(3), connection.result.getString(4), connection.result.getInt(5), false, connection.result.getString(6), connection.result.getString(7), connection.result.getInt(8), connection.result.getInt(9), connection.result.getString(10), connection.result.getString(11), connection.result.getInt(12), address);
			}
			else
			{
				user = new User();
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			return user;
		}
		
	/**
	 * Insert buyer information into user table
	 * @param userId
	 * @return userId if successful, else -1
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public int enterNewUser(String userName, String password, String firstName, String lastName, String email, String phoneNumber, String street, String city, String state, String zip) throws ClassNotFoundException, SQLException {

		int userId = -1;
		
		//create query
		String sql = "Insert Into user (userName, password, firstName, lastName, email, phoneNumber, street, city, state, zip) Values (?,?,?,?,?,?,?,?,?,?) ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
		
		//set variable in prepared statement
		connection.ps.setString(1, userName);
		connection.ps.setString(2, password);
		connection.ps.setString(3, firstName);
		connection.ps.setString(4, lastName);
		connection.ps.setString(5, email);
		connection.ps.setString(6, phoneNumber);
		connection.ps.setString(7, street);
		connection.ps.setString(8, city);
		connection.ps.setString(9, state);
		connection.ps.setString(10, zip);
		
        int affectedRows = connection.ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = connection.ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

		return userId;
			
		}
		
		
		/**
		 * Checks if user name is available
		 * @param userId
		 * @return true if available, false otherwise.
		 * @throws SQLException 
		 * @throws ClassNotFoundException 
		 */
		public boolean isUserNameAvailable(String userName) throws ClassNotFoundException, SQLException {
				boolean success = true;
				
				//create query
				String sql = "SELECT userName FROM user WHERE userName = ? ";
				
				//create connection
				connection = new ConnectionInfo();
				//getConnection();
				
				//create prepared statement
				connection.ps = connection.conn.prepareStatement(sql);
				
				//set variable in prepared statement
				connection.ps.setString(1, userName);
				
				connection.executeQuery();

				try {
					if(connection.result.next()) success = false; //If result returned, user name is not available.
					}
					catch (SQLException ex) {
					Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
					}
					
					return success;
				}
		
		  public void approveSeller() {
		  }

		  public void denySeller() {
		  }

		  public void closeAccount() {
		  }

}