package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.user.User;
import com.zoho.saml.SAMLConstants;
public class Util {

	private static Properties prop =null;

	public static String getProperty(String propName) {
		if (getProp() != null) {
			return getProp().getProperty(propName);
		} else {
			return null;
		}
	}
	public static String getFileAsString(File file)	throws FileNotFoundException, IOException 
	{
		StringWriter content = new StringWriter();
		int readCount = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // No I18N
			char[] buf = new char[8192];
			while ((readCount = reader.read(buf)) > 0) {
				content.write(buf, 0, readCount);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (content != null) {
				content.close();
			}
		}
		return content.toString();
	}
	public static  byte[] getFileAsByte(String filePath) 
	{
		File file = new File(filePath);
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fis.read(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer;
	}
	public static User getCurrentUser()
	{
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		
		User userinfo = getCurrentUser(request);
		return userinfo;
	}
	private static User getCurrentUser(HttpServletRequest request)
	{
		User userInfo = (User) request.getSession().getAttribute(SAMLConstants.USER_IN_SESSION);
		return userInfo;
	}
//	public static ZohoCrmMeta getCurrentUserCrmMeta()throws Exception
//	{
//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//		
//		HttpSession session=request.getSession();
//		User currentUser = new User();
//		currentUser.setAuthToken("1000.ef9b674557c9292f162fdef5623fee68.aececebc9a1df1db39a627f05208f257");
//		if(currentUser==null)
//		{
//			throw new Exception("User Not set");
//		}
//		ZohoCrmMeta crmMeta = null;
//		/*
//		 * Fetch CRM META from session if cacheMeta flag is set 
//		 * 
//		 * Meta in session will be dropped at every LeadScore Update done to ZOHOCRM
//		 */
//		if(DeploymentUtil.toCacheCrmMeta())
//		{
//			crmMeta = (ZohoCrmMeta)session.getAttribute(Constants.CRM_META_IN_SESSION);
//		}
//		/*
//		 * if meta is not available in cache OR cacheMeta flag is false
//		 * create meta
//		 */
//		if(crmMeta==null)
//		{
//			/*
//			 * populate CrmMeta using ZOHOCRM api if unavailable in currentSession
//			 */
//			crmMeta = new ZohoCrmMeta(currentUser);
//			if(DeploymentUtil.toCacheCrmMeta())
//			{
//				session.setAttribute(Constants.CRM_META_IN_SESSION,crmMeta);
//			}
//		}

////		return crmMeta;
////	}
//	private static String getLeadScoreAppURL()
//	{
//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//		String scheme = request.getScheme();
//		String serverName = request.getServerName();
//		Integer serverPort = request.getServerPort();
//		
//		String url = scheme.concat("://").concat(serverName).concat(":").concat(serverPort.toString());
//		return url;
//	}

	public static Properties getProp() {
		return prop;
	}

	public static void setProp(Properties prop) {
		Util.prop = prop;
	}
}

