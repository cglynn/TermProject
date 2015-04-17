package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import dao.AuthDAO;

/**
 * Servlet implementation class AccountServlet
 */
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//If remove user Clicked
		if(request.getParameter("removeUser") != null)
		{
			String msg = "";
			
			//get session with user object
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			
			//Create connection
			AuthDAO dataAuth = new AuthDAO();
			
			//Update Data
			try {
				dataAuth.closeAccount(user.getUserId());
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
				dispatcher.forward(request,  response);
			}
			
			msg = "Account Deleted Successfully!";
			request.setAttribute("msg", msg);

			//Logout
			session.invalidate();
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If update account Clicked
		if(request.getParameter("updateAccount") != null)
		{
			String msg = "";
			AuthDAO data = new AuthDAO();
			
			//Verify first name
			String firstName = request.getParameter("firstName");
			if(firstName == null) firstName = "";
			if (firstName.length() == 0)
			{
				msg = msg + " Please enter First Name.<br>";
			}
			else if(firstName.length() > 20)
			{
				msg = msg + " First name can only be 20 characters long.<br>";
			}
			
			//Verify last name
			String lastName = request.getParameter("lastName");
			if(lastName == null) lastName = "";
			if (lastName.length() == 0)
			{
				msg =msg + " Please enter Last Name.<br>";
			}
			else if(lastName.length() > 20)
			{
				msg = msg + " Last name can only be 20 characters long.<br>";
			}
			
			//Verify phoneNumber
			String phoneNumber = request.getParameter("phoneNumber");
			if(phoneNumber == null) phoneNumber = "";
			if (phoneNumber.length() == 0)
			{
				msg =msg + " Please enter Phone Number.<br>";
			}
			else if(phoneNumber.length() > 20)
			{
				msg = msg + " Phone number can only be 20 characters long.<br>";
			}
			
			//Verify email
			String email = request.getParameter("email");
			if(email == null) email = "";
			if (email.length() == 0)
			{
				msg =msg + " Please enter Email.<br>";
			}
			else if(email.length() > 20)
			{
				msg = msg + " Email can only be 20 characters long.<br>";
			}
			
			//Verify Street
			String street = request.getParameter("street");
			if(street == null) street = "";
			if (street.length() == 0)
			{
				msg =msg + " Please enter Street.<br>";
			}
			else if(street.length() > 20)
			{
				msg = msg + " Street can only be 20 characters long.<br>";
			}
			
			//Verify City
			String city = request.getParameter("city");
			if(city == null) city = "";
			if (city.length() == 0)
			{
				msg =msg + " Please enter City.<br>";
			}
			else if(city.length() > 20)
			{
				msg = msg + " City can only be 20 characters long.<br>";
			}
			
			//Verify State
			String state = request.getParameter("state");
			if(state == null) city = "";
			if (state.length() == 0)
			{
				msg =msg + " Please enter State.<br>";
			}
			else if(state.length() > 20)
			{
				msg = msg + " State can only be 20 characters long.<br>";
			}
			
			//Verify Zip
			String zip = request.getParameter("zip");
			if(zip == null) zip = "";
			if (zip.length() == 0)
			{
				msg =msg + " Please enter Zip.<br>";
			}
			else if(zip.length() > 20)
			{
				msg = msg + " Zip can only be 20 characters long.<br>";
			}
			
			//Verify Password
			String password = request.getParameter("password");
			if(password == null) password = "";
			if (password.length() == 0)
			{
				msg = msg + " Please enter Password.<br>";
			}
			else if(password.length() > 20)
			{
				msg = msg + " Password can only be 20 characters long.<br>";
			}
				
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
				dispatcher.forward(request,  response);
			}
			
			//If no issues with form, process Sign up.
			else{
				try {

						HttpSession session = request.getSession();
						User user = (User)session.getAttribute("user");
						int userId = user.getUserId();
						int result=data.AccountUpdate(firstName,lastName,phoneNumber,email,street,city,state,zip,password,userId);
						
						//If user account entered
						if(result == 1)
						{
								msg = "Account Updated Successfully";
								request.setAttribute("msg", msg);
								
								//Reload new user information.
								user =  data.getUserById(userId) ;
								user.setLoggedIn(true);
								session.setAttribute("user", user);
								
								RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
								dispatcher.forward(request,  response);
						}
						else{
							msg = "Account update Failed";
						}
					}
				 catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					msg = "Class Not Found Exception " + e.toString();
					e.printStackTrace();
				}
			
				//If issues with form display message
				if(!msg.equals("")){
					request.setAttribute("msg", msg);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
					dispatcher.forward(request,  response);
				}
				try {
					data.connection.DB_Close();
				} catch (Throwable e) {
					msg = "Class Not Found Exception " + e.toString();
					e.printStackTrace();
				}
			}
		}

	}

}
