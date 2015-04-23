package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.List;
import model.User;
import dao.CatalogDAO;

/**
 * Servlet implementation class ShoppingCartServlet
 */
public class ShoppingCartServlet extends HttpServlet {
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
		//If remove from shopping cart clicked
		if(request.getParameter("removeFromShoppingcart") != null)
		{
			String msg = "";
			
			//get session with user object
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			
			//get listItemId
			int listItemId = Integer.parseInt(request.getParameter("listItemId"));
			
			//Create connection
			CatalogDAO catalogData = new CatalogDAO();
			
			//Update Data
			try {
				catalogData.removeProductShoppingCart(listItemId);
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/shoppingCart.jsp");
				dispatcher.forward(request,  response);
			}
			
			List shoppingCart = new List();
			
			//Update user Shopping Cart Object
			try {
				shoppingCart = catalogData.getShoppingCart(user.getUserId());
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/shoppingCart.jsp");
				dispatcher.forward(request,  response);
			}
			
			session.setAttribute("shoppingCart", shoppingCart);
			msg = "Product Removed Successfully!";
			request.setAttribute("msg", msg);

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/shoppingCart.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If remove from shopping cart clicked
		if(request.getParameter("submitShoppingCart") != null)
		{
			//forward to purchase page
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/purchase.jsp");
			dispatcher.forward(request,  response);
		}
	}

}
