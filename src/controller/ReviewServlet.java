package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CatalogDAO;

/**
 * Servlet implementation class ReviewServlet
 */
public class ReviewServlet extends HttpServlet {
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
		//If Submit Review button clicked
		if(request.getParameter("submitReview") != null)
		{
			String msg = "";
			HttpSession session = request.getSession();
			String productIdString = (String)session.getAttribute("productId");
			int productId = Integer.parseInt(productIdString);
			String rankingString = (String)request.getParameter("ratingDropDown");
			int ranking = Integer.parseInt(rankingString);
			String review = (String)request.getParameter("reviewText");
			if(review == null)
			{
				review = "";
			}
			if(review.length() ==0)
			{
				msg = "Please enter Review Text.";
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/review.jsp");
				dispatcher.forward(request,  response);
			}
			CatalogDAO dataCatalog = new CatalogDAO();
			try {
				dataCatalog.reviewProduct(productId, ranking, review);
				dataCatalog.connection.DB_Close();
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			
				msg = "Rating saved successfully!";
			
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/review.jsp");
				dispatcher.forward(request,  response);
		}
		
	}

}
