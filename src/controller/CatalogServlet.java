package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CatalogServlet
 */
public class CatalogServlet extends HttpServlet {
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
		//Check to see if Filter Catalog was clicked.
		if(request.getParameter("filterCatalog") != null){
			String departmentFilter = request.getParameter("departmentDropDown");
			String catalogFilter = request.getParameter("filterText");
			HttpSession session = request.getSession();
			session.setAttribute("departmentFilter", departmentFilter);
			session.setAttribute("catalogTextFilter", catalogFilter);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/catalog.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Check to see if Edit Product was clicked.
		if(request.getParameter("editProduct") != null){
			String productId = request.getParameter("productId");
			HttpSession session = request.getSession();
			session.setAttribute("productId", productId);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editProduct.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Check to see if Rate Product was clicked.
		if(request.getParameter("rateProduct") != null){
			String productId = request.getParameter("productId");
			HttpSession session = request.getSession();
			session.setAttribute("productId", productId);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/review.jsp");
			dispatcher.forward(request,  response);
		}
	}

}
