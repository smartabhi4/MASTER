//$Id$
package com.merge;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.user.User;
import com.user.UserHandler;
import com.user.UserHandlerImpl;
import com.util.Constants;
import com.util.CriteriaPojo;
import com.util.ZohoCrmUtil;
import com.zoho.oauth.OAuthParams;
import com.zoho.oauth.OAuthUtil;
import com.zoho.saml.SamlUtil;

public class MainServlet extends HttpServlet{
	private User currentUser;
	private HashMap<String, String> modulesList;
	private LinkedList<JSONObject>fldList;
	private String CriteriaName;
	private String CriteriaId;
	private LinkedList<CriteriaPojo> allCriteria;
	private String CriteriaList;
	private String selActionFld;
	private String selActionVal;
	private String selActionOption;
	private String selectedModName;
    private String mergeField1;
    private String mergeField2;
    private String mergeField3;
    private String fldtype;
    private JSONArray fldValsArr;
	  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {
		 try{
		UserHandler handler = new UserHandlerImpl();
		com.zoho.saml.User samlUser = SamlUtil.getCurrentUser();
		HttpSession session=request.getSession();
		currentUser =handler.getUserByEmail(samlUser.getEmail());
		session.setAttribute("currentUser", currentUser);
		fldtype =(String) request.getParameter("fldtype");
		 OAuthParams params = new OAuthParams();
		 params.setAccessToken(currentUser.getAuthToken());
		 params.setExpiryTime(currentUser.getExpirytime());
		 params.setRefreshToken(currentUser.getRefreshToken());
		  if(currentUser != null && (fldtype == null)){
			  if(OAuthUtil.isTokenValid(params)){
					currentUser=	updateExpriedAccessToken();
			  }
			     
				  request.setAttribute("module", getSelectedModName());
				  getAllDetials(request);
				  request.setAttribute("fldList", getFldList());
				  request.setAttribute("moduleList", getModulesList());
				  request.setAttribute("selectedModule", getSelectedModName());
				  if(getCriteriaId() != null && !"".equals(getCriteriaName())){
					  request.setAttribute(Constants.CriteriaName, getCriteriaName());
					  request.setAttribute(Constants.CriteriaID, getCriteriaId());
					  request.setAttribute(Constants.AllCriteria, getAllCriteria());
					  request.setAttribute("SelActionOption", getSelActionOption());
					  request.setAttribute("SelActionFld", getSelActionFld());
					  request.setAttribute("SelActionVal", getSelActionVal());
				  }else{
					  request.setAttribute(Constants.CriteriaName, "");
					  request.setAttribute(Constants.CriteriaID, "");
					  request.setAttribute("SelActionVal", getSelActionVal());
					  request.setAttribute("SelActionFld","");
				  }
				  request.setAttribute("selMFld1", mergeField1);
				  request.setAttribute("selMFld2", mergeField2);
				  request.setAttribute("selMFld3", mergeField3);
				  request.setAttribute("fldValsArray", getFldValsArr());
					  RequestDispatcher rd = request.getRequestDispatcher("/jsp/OAuth.jsp");
					  response.setBufferSize(128 * 1024);
					  rd.forward(request, response);
			   }else{
				   if(OAuthUtil.isTokenValid(params)){
						currentUser=	updateExpriedAccessToken();
				   }
				   String module=(String) request.getParameter("module");
				   criteriaMetaDataImpl metaData = new criteriaMetaDataImpl();
				   metaData.setUser(currentUser);
				   String fld_api = (String )request.getParameter("fld_api");
				   JSONArray picklistFldValues= metaData.populateAllPicklistValues(module, fld_api);
//				   response.setContentType("application/json");
				   response.getWriter().write(picklistFldValues.toString());
			   }
		  }catch(Exception e){
			    e.printStackTrace();
		   }
	  }
	  public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	  {
		  UserHandler handler = new UserHandlerImpl();
			com.zoho.saml.User samlUser = SamlUtil.getCurrentUser();
			HttpSession session=request.getSession();
			currentUser =handler.getUserByEmail(samlUser.getEmail());
//		  currentUser = Util.getCurrentUser();
		  OAuthParams oauthParams = new OAuthParams();
		  if(currentUser != null){
			  oauthParams.setAccessToken(currentUser.getAuthToken());
			  oauthParams.setExpiryTime(Long.valueOf(currentUser.getExpirytime()));
			  oauthParams.setRefreshToken(currentUser.getRefreshToken());
			  	if(OAuthUtil.isTokenValid(oauthParams)){
			  	try{
			  		updateExpriedAccessToken();
			  	}catch(Exception e){
			  		}
				}
		  Object strCriteria= request.getParameter("criteriaList");
		  String strCriteriaList= strCriteria.toString();
		  strCriteriaList = strCriteriaList.replaceAll("\\]", "");
		  strCriteriaList = strCriteriaList.replaceAll("\\[", "");
		  strCriteriaList = strCriteriaList.replaceAll("(},)", "},,");
		  String []arr =strCriteriaList.split("(,,)");
		  JSONArray criteriaArray = new JSONArray();
		  try{
		  for(int i=0;i<arr.length ;i++){
			  JSONObject criteriaJson = new JSONObject(arr[i]);
			  criteriaArray.put(criteriaJson);
		  }
		  }catch(Exception e){
		  }
		  String result= saveCriteria(criteriaArray);   
		  response.setContentType("application/json");
		  response.getWriter().write(result);
		  }
	  }
	  public void getAllDetials(HttpServletRequest request)throws ServletException,Exception{
			getAllModuleDetails();
			String module= request.getParameter("module");
				if( module != null){
					getModuleFields(module);
				}else{
					getModuleFields("Leads");
				}
		}
	  private HashMap<String, String> getAllModuleDetails() throws SecurityException, Exception{
			if (currentUser != null) 
			{
				/*
				 * Check if authToken is configured
				 */
					try{
					HashMap<String, String> modulesList = ZohoCrmUtil.getAllModules(currentUser.getAuthToken());
					setModulesList(modulesList);
					}catch(Exception e){
						
					}
				}
			return getModulesList();
		}
	  public void getModuleFields(String moduleName)throws Exception{
			criteriaMetaDataImpl metadata = new criteriaMetaDataImpl();
			metadata.setUser(currentUser);
			setFldValsArr(metadata.populateAllFields(moduleName));
			setFldList(metadata.getAllLeadsFields());
			setSelectedModName(moduleName);
			metadata.populateAllRecords("recordsdupmerge_DupMergeCRTModule",moduleName);
			
			if(metadata.getCriteriaId()!= null && !"".equals(metadata.getCriteriaId())){
				setCriteriaName(metadata.getCriteriaName());
				setCriteriaId(metadata.getCriteriaId());
				setAllCriteria(metadata.getAllCriteria());
				if(metadata.getAction() != "null" && metadata.getAction() != null && !"".equals(metadata.getAction())){
					String []tempActionArr = metadata.getAction().split("(#\\W+)");
					setSelActionOption(tempActionArr[0]);
					setSelActionFld(tempActionArr[1]);
					setSelActionVal(tempActionArr[2]);
				}else{
					setSelActionVal("");
				}
			}else{
				setCriteriaName("");
				setSelActionVal("");
				setSelActionFld("None");
			}
			mergeField1 = metadata.getMergeField1();
			mergeField2 = metadata.getMergeField2();
			mergeField3 = metadata.getMergeField3();
		}
	  
	  public String saveCriteria(JSONArray criteriaList) {
			JSONObject responseJson = new JSONObject();
			try 
			{
				String response="";
				UserHandler handler = new UserHandlerImpl();
				com.zoho.saml.User samlUser = SamlUtil.getCurrentUser();
				currentUser =handler.getUserByEmail(samlUser.getEmail());
				OAuthParams oauthParams = new OAuthParams();
				oauthParams.setAccessToken(currentUser.getAuthToken());
				oauthParams.setExpiryTime(currentUser.getExpirytime());
				oauthParams.setRefreshToken(currentUser.getRefreshToken());
				if (currentUser != null) 
				{
					if(OAuthUtil.isTokenValid(oauthParams)){
						currentUser= updateExpriedAccessToken();
					}
					criteriaMetaDataImpl objImpl= new criteriaMetaDataImpl();
					objImpl.setUser(currentUser);
					 response = objImpl.insertOrUpdateRecords(criteriaList);
				}
				if(response == "success"){
					responseJson.put("result","success");
				}
					/*
					 * To Create CRM DuplicateExtension Criteria Records from the Criteria Obtained
					 */
			} catch (Exception e) {
				e.printStackTrace();
			}
			return responseJson.toString();
		}
	  public void doDelete(HttpServletRequest request , HttpServletResponse response) throws ServletException,IOException{
		  
		  	UserHandler handler = new UserHandlerImpl();
			com.zoho.saml.User samlUser = SamlUtil.getCurrentUser();
			currentUser =handler.getUserByEmail(samlUser.getEmail());
		  if(currentUser != null){
			  	OAuthParams oauthParams = new OAuthParams();
				oauthParams.setAccessToken(currentUser.getAuthToken());
				oauthParams.setExpiryTime(Long.valueOf(currentUser.getExpirytime()));
				oauthParams.setRefreshToken(currentUser.getRefreshToken());
				if(OAuthUtil.isTokenValid(oauthParams))
				{
					try{
					currentUser= updateExpriedAccessToken();
					}catch(Exception e){
					}
				}
		  String criteriaID =(String) request.getParameter("CriteriaId");
		  try{
		  String delResponse= ZohoCrmUtil.deleteRecords("recordsdupmerge_DupMergeCRTModule", criteriaID, currentUser.getAuthToken());
		  JSONObject jobj = new JSONObject(delResponse);
		  JSONArray jsonarr= (JSONArray) jobj.get("data");
		  jobj = (JSONObject) jsonarr.get(0);
		  JSONObject result= new JSONObject();
		  if(jobj.has("status") && jobj.get("status").toString().equals("success")){
			  result.put("result", "success");
		  }else{
			  result.put("result", "SESSION_UNAVAILABLE");
		  }
		  response.setContentType("application/json");
		  response.getWriter().write(result.toString());
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  }
	  }
	  public User updateExpriedAccessToken() throws Exception{
		  User latestUser = null;
		  try{
		  OAuthParams generateAccessToken = OAuthUtil.refreshAccessToken(currentUser.getRefreshToken());
		  UserHandler usrHandler = new UserHandlerImpl();
		  currentUser.setAuthToken(generateAccessToken.getAccessToken());
		  currentUser.setExpirytime(generateAccessToken.getExpiryTime());
		  latestUser= usrHandler.updateUser(currentUser);
		  }catch(Exception e){
			  throw  new Exception("Error while creating Access Token");
		  }
		return latestUser;
	  }
	public HashMap<String, String> getModulesList() {
		return modulesList;
	}
	public void setModulesList(HashMap<String, String> modulesList) {
		this.modulesList = modulesList;
	}
	public LinkedList<JSONObject> getFldList() {
		return fldList;
	}
	public void setFldList(LinkedList<JSONObject> fldList) {
		this.fldList = fldList;
	}
	public String getSelectedModName() {
		return selectedModName;
	}
	public void setSelectedModName(String selectedModName) {
		this.selectedModName = selectedModName;
	}
	public LinkedList<CriteriaPojo> getAllCriteria() {
		return allCriteria;
	}
	public void setAllCriteria(LinkedList<CriteriaPojo> allCriteria) {
		this.allCriteria = allCriteria;
	}
	public String getCriteriaId() {
		return CriteriaId;
	}
	public void setCriteriaId(String criteriaId) {
		CriteriaId = criteriaId;
	}
	public String getCriteriaName() {
		return CriteriaName;
	}
	public void setCriteriaName(String criteriaName) {
		CriteriaName = criteriaName;
	}
	public String getSelActionFld() {
		return selActionFld;
	}
	public void setSelActionFld(String selActionFld) {
		this.selActionFld = selActionFld;
	}
	public String getSelActionVal() {
		return selActionVal;
	}
	public void setSelActionVal(String selActionVal) {
		this.selActionVal = selActionVal;
	}
	public String getSelActionOption() {
		return selActionOption;
	}
	public void setSelActionOption(String selActionOption) {
		this.selActionOption = selActionOption;
	}
	public String getCriteriaList() {
		return CriteriaList;
	}
	public void setCriteriaList(String criteriaList) {
		CriteriaList = criteriaList;
	}
	public JSONArray getFldValsArr() {
		return fldValsArr;
	}
	public void setFldValsArr(JSONArray fldValsArr) {
		this.fldValsArr = fldValsArr;
	}
}
