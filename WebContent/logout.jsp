<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div id='logout'>
<%@ page import="model.User" %>
<% 
User logOutuser = (User)session.getAttribute("user");
String pathLogin = "";
String loginText = "";
boolean loggedIn1 = false;
if(logOutuser != null)
{
	loggedIn1 = logOutuser.isLoggedIn();
}

if (!loggedIn1)
		{
			ServletContext scLogout = getServletContext();
			pathLogin = scLogout.getContextPath();	
			out.print("<a href='" + pathLogin + "/login.jsp'>Click here to login.</a><br>");
		}
else{
	out.print("<form name='LogoutForm' action='LogoutServlet' method='post'><input type='submit' value='Logout' /></form>");
	
}
%>

</div>