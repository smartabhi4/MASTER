<%@page import="com.util.DeploymentUtil"%>
<%
response.sendRedirect(DeploymentUtil.getLoginPage(request));
%>
