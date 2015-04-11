<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Index Page</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ include file="logout.jsp" %>
<%@ page import="model.User" %>
<%@ page import="enums.userType" %>
<% 

User user = (User)session.getAttribute("user");
boolean loggedIn = false;
if(user != null)
{
	loggedIn = user.isLoggedIn();
}

String msg = (String)request.getAttribute("msg");

if (msg == null)
{
	msg = "";
}
if(msg != "")
{
	out.print("<div id='message'>");
	out.print(msg);
	out.print("</div>");
}


if (!loggedIn)
		{
			
	      out.print("You are not logged in.  Please go to login.jsp to log in.");
		}
else{
	out.print("<p>Hello " + user.getFirstName() + " " + user.getLastName() + "</p>");
	out.print("<p> User Type is " + user.getUserType());
	if(user.getUserType() == userType.admin.value)
	{
		out.print("<p> User Type is an Admin");
	}
	if(user.getUserType() == userType.seller.value)
	{
		out.print("<p> User Type is an Seller");
	}
	if(user.getUserType() == userType.buyer.value)
	{
		out.print("<p> User Type is an buyer");
	}
}


%>
</body>
</html>