<%@page import="com.user.User"%>
<%@page import="com.util.DeploymentUtil"%>
<%@page import="com.util.Util"%>
<%
	User user= Util.getCurrentUser();
	response.sendRedirect(DeploymentUtil.getHomePage());
%>
<html>	
	<head>
		<title>Duplicate Merge Made Easy</title><%--no i18n--%>
	</head>
	<body>
		<h1 style="text-align: center;"><strong>Duplicate Merge Made Easy</strong> </h1>
		<p>
		<strong><img style="display: block; margin-left: auto; margin-right: auto;" src="/images/lead_scoring.png"/>
		</strong>
		</p>
		<p>&nbsp;</p>
		<p style="text-align: center;">Click on the below link to SignIn using Zoho.</p>
		<p style="text-align: center;"><a href="/zoho/requestSAML">SIGN IN</a></p>
</body>
</html>

