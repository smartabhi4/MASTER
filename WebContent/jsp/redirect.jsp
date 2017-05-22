
<%@page import="com.util.DeploymentUtil"%>
<%-- <%=request.getAttribute("OAUTH_PARAMS")%> --%>
<%
	out.println();
	response.sendRedirect(DeploymentUtil.getHomePage(request));
%>
