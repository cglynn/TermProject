package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AuthDAO;

/**
 * Servlet implementation class SignupServlet
 */
public class SignupServlet extends HttpServlet {
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
		String msg = "";
		AuthDAO data = new AuthDAO();
		String username = request.getParameter("username");
		if(username == null) username = "";
		if (username.length() == 0)
		{
			msg = " Please enter username.";
		}
		
		//If user name is blank.  return with message.
		if(!msg.equals("")){
			request.setAttribute("msg", msg);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/signup.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Check user name button is clicked
		else if(request.getParameter("checkUsername") != null){
			
			try {
				if(data.isUserNameAvailable(username))
				{
					msg = "Username Available";
				}
				else
				{
					msg = "Username not Available";
				}
			} catch (ClassNotFoundException e) {
				msg = "Failed to retrieve Available username. Error: " + e.toString();
				e.printStackTrace();
			} catch (SQLException e) {
				msg = "Failed to retrieve Available username. Error: " + e.toString();
				e.printStackTrace();
			}
			//Return message stating if user name is available
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/signup.jsp");
				dispatcher.forward(request,  response);
			}
		}
		
		
		//Else Sign up button was clicked.  Verify fields
		else{
			//Verify first name
			String firstName = request.getParameter("firstName");
			if(firstName == null) firstName = "";
			if (firstName.length() == 0)
			{
				msg = msg + " Please enter First Name.<br>";
			}
			
			//Verify last name
			String lastName = request.getParameter("lastName");
			if(lastName == null) lastName = "";
			if (lastName.length() == 0)
			{
				msg =msg + " Please enter Last Name.<br>";
			}
			
			//Verify phoneNumber
			String phoneNumber = request.getParameter("phoneNumber");
			if(phoneNumber == null) phoneNumber = "";
			if (phoneNumber.length() == 0)
			{
				msg =msg + " Please enter Phone Number.<br>";
			}
			
			//Verify email
			String email = request.getParameter("email");
			if(email == null) email = "";
			if (email.length() == 0)
			{
				msg =msg + " Please enter Email.<br>";
			}
			
			//Verify Street
			String street = request.getParameter("street");
			if(street == null) street = "";
			if (street.length() == 0)
			{
				msg =msg + " Please enter Street.<br>";
			}
			
			//Verify City
			String city = request.getParameter("city");
			if(city == null) city = "";
			if (city.length() == 0)
			{
				msg =msg + " Please enter City.<br>";
			}
			
			//Verify State
			String state = request.getParameter("state");
			if(state == null) city = "";
			if (state.length() == 0)
			{
				msg =msg + " Please enter State.<br>";
			}
			
			//Verify State
			String zip = request.getParameter("zip");
			if(zip == null) zip = "";
			if (zip.length() == 0)
			{
				msg =msg + " Please enter Zip.<br>";
			}
			
			//Verify Password
			String password = request.getParameter("password");
			if(password == null) password = "";
			if (password.length() == 0)
			{
				msg = msg + " Please enter Password.<br>";
			}
			
			//Verify last name
			String confirmPassword = request.getParameter("confirmPassword");
			if(confirmPassword == null) confirmPassword = "";
			if (confirmPassword.length() == 0)
			{
				msg = msg + " Please enter Confirm Password.<br>";
			}
			
			//Verify Password and Confirm Password match
			if (confirmPassword.compareTo(password) != 0)
			{
				msg = msg + " Password and Confirm Password do not match.";
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/signup.jsp");
				dispatcher.forward(request,  response);
			}
			
			//If no issues with form, process Sign up.
			else{
				try {
					//If user name is available
					if(data.isUserNameAvailable(username))
					{
						int userId = -1;
						userId = data.enterNewUser(username, password, firstName, lastName, email, phoneNumber, street, city, state, zip);
						
						//If user account entered
						if(userId > 0)
						{
								msg = "Account Created Successfully";
								request.setAttribute("msg", msg);
								RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
								dispatcher.forward(request,  response);
						}
						else{
							msg = "Account creation Failed";
						}
					}
					else{
						msg = "Username Not Available";
					}
				} catch (ClassNotFoundException e) {
					msg = "Class Not Found Exception " + e.toString();
					e.printStackTrace();
				} catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				}
				
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/signup.jsp");
				dispatcher.forward(request,  response);
			}
		}
		
		try {
			data.connection.DB_Close();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		
	}

}