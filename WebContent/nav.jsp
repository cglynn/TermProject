<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link href="style.css" rel="stylesheet" type="text/css" />
<div id='nav'>
<%
ServletContext sc = getServletContext();
String path = sc.getContextPath();

%>
<table width="600">
	<tr>
		<td>
			<a href='<%=path %>/catalog.jsp'>Catalog</a>
		</td>
		<td>
			<a href='<%=path %>/login.jsp'>Login</a>
		</td>
		<td>
			<a href='<%=path %>/signup.jsp'>Sign up</a>
		</td>
		<td>
			<a href='<%=path %>/account.jsp'>My Account</a>
		</td>
		<td>
			<a href='<%=path %>/messages.jsp'>Messages</a>
		</td>	
		<td>
			<a href='<%=path %>/shoppingCart.jsp'>Shopping Cart</a>
		</td>		
		<td>
			<a href='<%=path %>/wishList.jsp'>Wish List</a>
		</td>	
		<td>
			<a href='<%=path %>/order.jsp'>Orders</a>
		</td>							
	</tr>
</table>
</div>