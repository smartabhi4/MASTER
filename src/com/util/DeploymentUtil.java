package com.util;

import javax.servlet.http.HttpServletRequest;

public class DeploymentUtil {
	public static String getLoginPage(){
		return getLoginPage(null);
	}
	public static String getLoginPage(HttpServletRequest request)
	{
		String contextPath="";
		if(request != null){
			contextPath = request.getContextPath();
		}
		if(DeploymentUtil.isStandAloneDeployment())
		{
			return contextPath+"/merge/login.do";
		}
		else
		{
			return contextPath+"/zoho/requestSAML";
		}
	}
	public static String getHomePage(){
		return getHomePage(null);
	}
	public static String getHomePage(HttpServletRequest request)
	{
		String contextPath = "";
		if(request!=null){
			contextPath = request.getContextPath();
		}
		if(DeploymentUtil.isStandAloneDeployment())
		{
			return contextPath+"/merge/login.do";
		}
		else
		{
			return contextPath+"/merge/getAllDetails.do";
		}
	}	
	public static Boolean isDevMode()
	{
		return Boolean.parseBoolean(Util.getProperty(Constants.DEV_MODE));
	}
	/*
	 * To show only the CriteriaPage
	 * 
	 * or Show the complete Product
	 */
	public static Boolean isStandAloneDeployment()
	{
		String deploymentType=Util.getProperty(Constants.PLUGIN_TYPE);
		if(Constants.PLUGIN_TYPE_STAND_ALONE.equals(deploymentType))
		{
			return true;
		}
		return false;
	}
//	public static Boolean toCacheCrmMeta()
//	{
//		return Boolean.parseBoolean(Util.getProperty(Constants.CACHE_CRM_META));
//	}
}

