<%@page import="com.merge.MainServlet"%>
<%@page import="com.util.DeploymentUtil"%>
<%@page import="com.zoho.saml.SAMLConstants"%>
<%@page import="com.user.UserHandlerImpl"%>
<%@page import="com.zoho.saml.SamlUtil"%>
<%@page import="com.zoho.saml.User"%>
<%@page import="com.user.UserHandler"%>
<%
User user= SamlUtil.getCurrentUser();
com.user.User usr;
if(user !=null){
   UserHandler handler = new UserHandlerImpl();
   usr =handler.getUserByEmail(user.getEmail());
  MainServlet mainServ = new MainServlet();
   if(usr == null){
  	usr= handler.addNewSamlUser(user);
   }
 if(usr !=null && (usr.getAuthToken()== null || usr.getAuthToken() =="")){
 %>
<form action="<%=request.getContextPath()%>/zoho/requestOAuth">
<input type="submit" style="background:green; margin-left: 44%; margin-top: 15%; width:200px; height:40px;" value="Authorize User" >
</form>
<%}else{
	response.sendRedirect(DeploymentUtil.getHomePage(request));
	}
}%>

