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
import dao.CatalogDAO;

/**
 * Servlet implementation class EditProductServlet
 */
public class EditProductServlet extends HttpServlet {
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
		//Check to see if Edit Product button was clicked
		if(request.getParameter("saveProduct") != null){

			//Verify Product Name contains text.
			String productName = request.getParameter("productName");
			if(productName == null) productName = "";
			if (productName.length() == 0)
			{
				msg = " Please enter Product Name.";
			}
			
			//Verify Product Description
			String productDescription = request.getParameter("productDescription");
			if(productDescription == null) productDescription = "";
			if (productDescription.length() == 0)
			{
				msg = msg + " Please enter Product Description.";
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editProduct.jsp");
				dispatcher.forward(request,  response);
			}
			//Update product information
			else{
				HttpSession session = request.getSession();
				
				//Update Data Base
				CatalogDAO catalogData = new CatalogDAO();
				try {
					catalogData.editProduct(Integer.parseInt((String) session.getAttribute("productId")), productName, productDescription, Integer.parseInt(request.getParameter("departmentDropDown")));
				} catch (NumberFormatException | SQLException e) {
					msg = "Number Format Exception " + e.toString();
					e.printStackTrace();
				}
				
				//Update product in Session, reload catalog to session.  In future may just update product in Session.
				Catalog catalog = null;
				catalogData = new CatalogDAO();
				try {
					catalog = catalogData.getCatalog();
					
				} catch (SQLException e) {
					msg = msg + " Sql Exception " + e.toString();
					e.printStackTrace();
				} catch (Throwable e) {
					
					msg = msg + " Sql Exception " + e.toString();
					e.printStackTrace();
				}
				try {
					catalogData.connection.DB_Close();
				} catch (Throwable e) {
					msg = msg + " Sql Exception " + e.toString();
					e.printStackTrace();
				}
				session.setAttribute("catalog", catalog);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editProduct.jsp");
				dispatcher.forward(request,  response);
			}
		}
		
		//If Saving Seller Information
		if(request.getParameter("saveSellingInformation") != null)
		{
			//Verify Product Price
			String productPrice = request.getParameter("productPrice");
			if(productPrice == null) productPrice = "";
			if (productPrice.length() == 0)
			{
				msg = msg + " Please enter Product Price.";
			}
			else{
				try {
					Double.parseDouble(productPrice);
				}
				catch (NumberFormatException nfe)
				{
					msg = msg + " Please enter product price as a decimal to two decimal places.";
				}
			}
			
			//Verify Product Shipping cost
			String productShippingCost = request.getParameter("productShippingCost");
			if(productShippingCost == null) productShippingCost = "";
			if (productShippingCost.length() == 0)
			{
				msg = msg + " Please enter Product Shipping Cost.";
			}
			else{
				try {
					Double.parseDouble(productShippingCost);
				}
				catch (NumberFormatException nfe)
				{
					msg = msg + " Please enter product shipping cost as a decimal to two decimal places.";
				}
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editProduct.jsp");
				dispatcher.forward(request,  response);
			}
			else{
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editProduct.jsp");
				dispatcher.forward(request,  response);
			}

		}
	}

}
