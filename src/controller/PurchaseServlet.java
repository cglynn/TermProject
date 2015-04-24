package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Catalog;
import model.List;
import model.ListItem;
import model.Orders;
import model.Product;
import model.ProductSeller;
import model.User;
import dao.CatalogDAO;
import dao.AuthDAO;
import enums.ListType;

/**
 * Servlet implementation class PurchaseServlet
 */
public class PurchaseServlet extends HttpServlet {
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
		if(request.getParameter("purchase") != null)
		{
			//Verify fields have been filled in
			String msg = "";
			
			//Verify first name
			String receiverName = request.getParameter("receiverName");
			if(receiverName == null) receiverName = "";
			if (receiverName.length() == 0)
			{
				msg = msg + " Please enter Receiver Name.<br>";
			}
			else if(receiverName.length() > 20)
			{
				msg = msg + " Receiver name can only be 20 characters long.<br>";
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
			
			//Verify Pay Pal Account
			String payPalAccount = request.getParameter("payPalAccount");
			if(payPalAccount == null) payPalAccount = "";
			if (payPalAccount.length() == 0)
			{
				msg = msg + " Please enter Pay Pal Account Number.<br>";
			}
			else if(payPalAccount.length() > 20)
			{
				msg = msg + " Pay Pal Account number can only be 20 characters long.<br>";
			}
				
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/purchase.jsp");
				dispatcher.forward(request,  response);
			}
			//Submit Order
			else{
				CatalogDAO data = new CatalogDAO();
				try {

						HttpSession session = request.getSession();
						double tax = (double)session.getAttribute("tax");
						double subTotal = (double)session.getAttribute("subTotal");
						User user = (User)session.getAttribute("user");
						Catalog catalog = (Catalog)session.getAttribute("catalog");
						int userId = user.getUserId();
						int orderId=data.createOrder(userId, tax, subTotal, receiverName, payPalAccount, street, city, state, zip);
						
						//Order Entered
						if(orderId > 0)
						{
								List shoppingList = (List)session.getAttribute("shoppingCart");
								
								//Update ListItem with ShippingCost and Price.
								ListIterator<ListItem> items = shoppingList.getListItems();
								ListItem item = new ListItem();
								Product product = new Product();
								ProductSeller seller = new ProductSeller();
								
								while(items.hasNext())
								{
									item = items.next();
									product = catalog.getProductById(item.getProductId());
									seller = product.getProductSellerById(item.sellerId);
									item.price = seller.getPrice();
									item.shippingPrice = seller.getShippingCost();
									data.updateListItemPrices(item);
								}
								
								//Switch Shopping list to Order list
								data.switchShopCartToOrderList(shoppingList.getListId(), orderId);

								msg = "Order Created Successfully";
								request.setAttribute("msg", msg);
								
								//Load Orders
								Orders orders = new Orders();
								orders = data.getBuyerOrder(user.getUserId() );
								
								//Create and load new empty shopping Cart.
								AuthDAO dataAuth = new AuthDAO();
								dataAuth.createList(userId, ListType.shoppingCart.value, null);
								List shoppingCart = data.getShoppingCart(userId);
								session.setAttribute("shoppingCart", shoppingCart);
								session.setAttribute("orders", orders);
								dataAuth.connection.DB_Close();
								data.connection.DB_Close();
								
								RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
								dispatcher.forward(request,  response);
						}
						else{
							msg = "Order Creation Failed.";
						}
					}
				 catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				//If issues with form display message
				if(!msg.equals("")){
					request.setAttribute("msg", msg);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
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
