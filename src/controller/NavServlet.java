package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Catalog;
import model.Messages;
import model.User;
import dao.CatalogDAO;
import dao.MessageDAO;

/**
 * Servlet implementation class NavServlet
 */
public class NavServlet extends HttpServlet {
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
		//If Logout button clicked
		if(request.getParameter("logout") != null)
		{
			HttpSession session = request.getSession();
			session.invalidate();
			request.setAttribute("msg", "You have logged out");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Login button clicked
		if(request.getParameter("login") != null)
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Catalog button clicked
		if(request.getParameter("catalog") != null)
		{
			HttpSession session = request.getSession();
			String msg = "";
			
			//Load products in Catalog
			Catalog catalog = new Catalog();
			CatalogDAO catalogData = new CatalogDAO();
			try {
				catalog = catalogData.getCatalog();
				catalogData.connection.DB_Close();
			} catch (SQLException e) {
				
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			session.setAttribute("catalog", catalog);
			session.setAttribute("departmentFilter", "");
			session.setAttribute("catalogTextFilter", "");
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/catalog.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If My Account button clicked
		if(request.getParameter("myAccount") != null)
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Messages button clicked
		if(request.getParameter("messages") != null)
		{
			//Load Messages
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			String msg = "";
			//Load user messages
			Messages messages = new Messages();
			MessageDAO messageData = new MessageDAO();
			try {
				messages = messageData.getMessageByUserId(user.getUserId());
				messageData.connection.DB_Close();
			} catch (SQLException e) {
				
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			session.setAttribute("messages", messages);
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/messages.jsp");
				dispatcher.forward(request,  response);
			}
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/messages.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Orders button clicked
		if(request.getParameter("orders") != null)
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Shopping Cart button clicked
		if(request.getParameter("shoppingCart") != null)
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/shoppingCart.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Sign Up button clicked
		if(request.getParameter("signUp") != null)
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/signUp.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Wish List button clicked
		if(request.getParameter("wishList") != null)
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/wishList.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Message Admin button clicked
		if(request.getParameter("messageAdmin") != null)
		{
			HttpSession session = request.getSession();
			session.setAttribute("receiverId", "1");
			session.setAttribute("receiverName", "Admin");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sendMessage.jsp");
			dispatcher.forward(request,  response);
		}

	}

}
