package com.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.httpConnector.HTTPConnector;
//import com.leadsScore.LSException;

public class ZohoCrmUtil {
	private static String getAuthtokenHeadderFormat(String authToken)
	{
		if(authToken==null)
		{
			return null;
		}
		return "Zoho-oauthtoken ".concat(authToken);
	}
	/*
	 * To Fetch List of all modules in the users account
	 */
	public static HashMap<String, String> getAllModules(String authToken) throws Exception
	{
		if(authToken!=null)
		{
			try{
			HTTPConnector getAllModules = new HTTPConnector();
			String apiUrl = getModulesApi();

			getAllModules.setUrl(apiUrl);
			getAllModules.addHeadder("Authorization",getAuthtokenHeadderFormat(authToken));
			String response = getAllModules.get();
			JSONObject json =new JSONObject(response);
			JSONArray jsonArray = (JSONArray) json.get("modules");
			HashMap<String, String> modulesMap= new HashMap<String, String>();
			 for(int i=0 ;i< jsonArray.length();i++){
                      JSONObject temp=(JSONObject) jsonArray.get(i);
                      modulesMap.put(temp.get("module_name").toString(), temp.get("id").toString());
			 }
			return modulesMap;
			}catch(Exception e){
				throw new Exception("User AuthToken Expired");
			}
		}
		throw new Exception("User AuthToken Unavailable");
	}
	/*
	 * get All LeadScore Records from ZohoCRM
	 */
	public static String fetchAllRecords(String moduleName,String authToken) throws Exception
	{
		HTTPConnector getAllRecords = new HTTPConnector();
		String apiUrl = getRecordsAPI(moduleName);
		getAllRecords.setUrl(apiUrl);
		getAllRecords.addHeadder("Authorization",getAuthtokenHeadderFormat(authToken));
		String response = getAllRecords.get();
		return response;
	}
	/*
	 * getAll Fields under LEAD Module
	 */
	public static String getTabFields(String authToken, String tabName) throws Exception
	{
		HTTPConnector getAllLeadsField = new HTTPConnector();
		String apiUrl = getFieldsAPI(tabName);
		getAllLeadsField.setUrl(apiUrl);
		getAllLeadsField.addHeadder("Authorization",getAuthtokenHeadderFormat(authToken));
		String response = getAllLeadsField.get();
		return response;
	}
	/*
	 * Fetch the leadsScore TabName from an account
	 */
//	public static String getDupMergeCrtTabName(String module) throws Exception
//	{
////		String tabName = null; 
////			Element row = (Element) modulesList.item(i);
////			String singularName = row.getAttribute(Constants.XML_ATTR_SL);
////			if(Constants.TABLABLE_DUPLICATE_MERGE.equals(singularName))
////			{
////				tabName = row.getTextContent();
////				break;
////			}
////		}
//		return "tabName";
//	}
	/*
	 * verify user permission to rquired modules
	 */
//	public static HashMap<String,Boolean> getModuleVsPrivilegeMap(NodeList modulesList) throws Exception
//	{
//		HashMap<String,Boolean> moduleVsPrivilege = new HashMap<String,Boolean>();
//		
//		Boolean accessToLeads = false;
//		Boolean accessToLeadScore = false;
//		
//		
//		for(int i=0;i<modulesList.getLength();i++)
//		{
//			Element row = (Element) modulesList.item(i);
////			String singularName = row.getAttribute(Constants.XML_ATTR_SL);
////			if(Constants.TABLABLE_DUPLICATE_MERGE.equals(singularName))
//			{
//				accessToLeadScore = Boolean.TRUE;
//			}
//			else if(Constants.TABNAME_LEADS.equals(row.getTextContent()))
//			{
//				accessToLeads = Boolean.TRUE;
//			}
//		}
//		moduleVsPrivilege.put(Constants.COMPONENT_LEAD, accessToLeads);
//		moduleVsPrivilege.put(Constants.COMPONENT_DUPLICATE_MERGE, accessToLeadScore);
//		return moduleVsPrivilege;
//	}
	public static String insertRecords(String tabName,JSONArray data , String authToken) throws Exception
	{
		HTTPConnector insertRecord = new HTTPConnector();
		String apiUrl = getInsertRecordAPI(tabName);
		insertRecord.setUrl(apiUrl);
		insertRecord.addHeadder("Authorization",getAuthtokenHeadderFormat(authToken));
		String body="{\"data\""+":"+ data.toString()+"}";	
		insertRecord.setRequestBody(body);
		String response = insertRecord.post();
		
		return response;
	//	return parseXMLResponse(response);
	}
	public static String updateRecords(String tabName,JSONArray data,String authToken) throws Exception
	{
		HTTPConnector updateRecord = new HTTPConnector();
		String apiUrl = getUpdateRecordAPI(tabName);
		updateRecord.setUrl(apiUrl);
		updateRecord.addHeadder("Authorization",getAuthtokenHeadderFormat(authToken));
		String body="{\"data\""+":"+ data.toString()+"}";	
		updateRecord.setRequestBody(body);
		String response = updateRecord.put();
		return response;
	}
	public static String deleteRecords(String tabName,String criteriaId,String authToken) throws Exception
	{
		HTTPConnector deleteRecord = new HTTPConnector();
		String apiUrl = getDeleteRecordAPI(tabName, criteriaId);
		deleteRecord.setUrl(apiUrl);
		deleteRecord.addHeadder("Authorization",getAuthtokenHeadderFormat(authToken));
		String response = deleteRecord.delete();
		return response;
	}
	/*
	 * Url Construction methods
	 */
	private static String getRecordsAPI(String module){
		return "https://api.zoho.com".concat("/crm/v2/").concat(module);
	}
	private static String getFieldsAPI(String module){
		return "https://api.zoho.com".concat("/crm/v2/settings/fields?module=").concat(module);
	}
	private static String getInsertRecordAPI(String module){
		return "https://api.zoho.com".concat("/crm/v2/").concat(module);
	}
	private static String getUpdateRecordAPI(String module){
		return "https://api.zoho.com".concat("/crm/v2/").concat(module);
	}
	private static String getDeleteRecordAPI(String module, String criteriaId){
		return "https://api.zoho.com".concat("/crm/v2/").concat(module).concat("/"+criteriaId);
	}
	private static String getModulesApi(){
		return "https://api.zoho.com".concat("/crm/v2/settings/modules");
	}
}

