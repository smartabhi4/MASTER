	//$Id$
package com.handler;

import com.user.User;
import com.user.UserHandler;
import com.user.UserHandlerImpl;
import com.util.Util;
import com.util.ZohoCrmUtil;
import com.zoho.oauth.OAuthParams;
import com.zoho.oauth.PersistenceHandler;
import com.zoho.saml.SamlUtil;

public class DBHandler implements PersistenceHandler{
	
	@Override
	public void deleteOAuthData(String arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OAuthParams getOAuthData(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOAuthData(OAuthParams params) throws Exception {
		
		UserHandler handler = new UserHandlerImpl();
		com.zoho.saml.User currentUser = SamlUtil.getCurrentUser();
		User usr= handler.getUserByEmail(currentUser.getEmail());
		if(usr != null && (usr.getAuthToken() == null || usr.getAuthToken() != "")){
			usr.setAuthToken(params.getAccessToken());
			usr.setExpirytime(params.getExpiryTime());
			usr.setRefreshToken(params.getRefreshToken());
			handler.updateUser(usr);
		}
	}

	@Override
	public void updateOAuthData(OAuthParams params) throws Exception 
	{
		// TODO Auto-generated method stub
	}

}

