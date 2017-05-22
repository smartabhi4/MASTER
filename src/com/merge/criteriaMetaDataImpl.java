//$Id$
package com.merge;

import java.util.HashMap;
import java.util.LinkedList;


import org.json.JSONArray;
import org.json.JSONObject;

import com.user.User;
import com.util.Constants;
import com.util.CriteriaPojo;
import com.util.ZohoCrmUtil;
import com.zoho.oauth.HTTPConnector;

public class criteriaMetaDataImpl {
	private User user;
	
	private Boolean metaPopulated = false;
	private Boolean validAuthToken = true;
	private String criteriaId;
	private String criteriaName;
	private LinkedList<CriteriaPojo> allCriteria;
	private LinkedList<JSONObject> allLeadsFields;
	private String action;
	private String mergeField1;
	private String mergeField2;
	private String mergeField3;
	public JSONArray populateAllFields(String module) throws Exception
	{
//		setUser(new User());
//		user.setAuthToken("1000.ea09a07b42512bffca4b24d1cc1acd2c.932ab3e4661ab121c4cfca9e454e910b");
		String allFields = ZohoCrmUtil.getTabFields(this.getUser().getAuthToken(), module);
		JSONObject json = new JSONObject(allFields);
		JSONArray jsonArr = (JSONArray)json.get("fields");
		JSONObject fieldInfo = new JSONObject();
		LinkedList<JSONObject> jsonFields= new LinkedList<JSONObject>();
		for(int fldIndex=0;fldIndex<jsonArr.length();fldIndex++)
		{
			JSONObject fieldList = new JSONObject();
			fieldInfo =(JSONObject) jsonArr.get(fldIndex);
			String label = fieldInfo.get(Constants.JSON_FIELD_ATTR_LABEL).toString();
			fieldList.put(Constants.JSON_FIELD_ATTR_LABEL, label);

			label = fieldInfo.get(Constants.JSON_FIELD_ATTR_TYPE).toString();
			fieldList.put(Constants.JSON_FIELD_ATTR_TYPE, label);
			
			label= fieldInfo.get(Constants.JSON_FIELD_ATTR_MAXLENGTH).toString();
			fieldList.put(Constants.JSON_FIELD_ATTR_MAXLENGTH, label);
			
			label= fieldInfo.get(Constants.JSON_FIELD_API_NAME).toString();
			fieldList.put(Constants.JSON_FIELD_API_NAME, label);
			jsonFields.add(fieldList);
		}
		setAllLeadsFields(jsonFields);
		return jsonArr;
}
	public String insertOrUpdateRecords(JSONArray criteriaList)throws Exception{
		int crtSize = criteriaList.length();
		JSONArray recordFormatData = new JSONArray(); 
		JSONObject makeSingleCrt = new JSONObject();
		StringBuilder strbldPattern = new StringBuilder(200).append("");
//		StringBuilder strbldPattern1 = new StringBuilder(200);
		int crtNumber=1;
		String seperator = "##+##";
		for(int i=0 ;i<crtSize-1;i++){
			JSONObject tempString = (JSONObject)criteriaList.get(i); 
			if(tempString != null && tempString.has("toDelete") != true){
			String checkval = tempString.get(Constants.FIELD_FIELD_CHECK_VALUE).toString();
			if(checkval == null){
				checkval="";
			}
			String crtComperator = tempString.get(Constants.FIELD_FIELD_COMPARATOR).toString();
			String crtFldName= tempString.get(Constants.FIELD_FIELD_NAME).toString();
			StringBuilder strbld = new StringBuilder(1000);
			strbld.append(crtFldName).append(seperator).append(crtComperator).append(seperator).append(checkval);
			makeSingleCrt.put("recordsdupmerge.Criteria_"+(crtNumber)+"", strbld.toString());
			if(i>0){
				if(tempString.has("operator")){
					strbldPattern.append(tempString.get("operator").toString());
				}
			}
			crtNumber = crtNumber+1;
			}
			else if(i>0 && crtNumber < crtSize-1)
			{
				makeSingleCrt.put("recordsdupmerge.Criteria_"+(i+1)+"", "null");
			}
//			}else if(i>0 && crtNumber == crtSize-1){
//				makeSingleCrt.put("recordsdupmerge.Criteria_"+(i+1)+"", "null");
//			}
			 if(i>0 && i< crtSize-2 && !tempString.has("toDelete")){
					 JSONObject tempCheckSperator = criteriaList.getJSONObject(i+1);
					 if(!tempCheckSperator.has("toDelete")){
						 	strbldPattern.append(seperator);
					 }
				 }
		}
		JSONObject tempJson = new JSONObject();
		tempJson = (JSONObject) criteriaList.get(crtSize-1);
		makeSingleCrt.put("recordsdupmerge.CustomModule1_Name", "DUPLICATECRITERIA");
		makeSingleCrt.put("recordsdupmerge.Module_Name", tempJson.get("Module Name").toString());
		makeSingleCrt.put("recordsdupmerge.Criteria_Name", tempJson.get(Constants.FIELD_CRITERIA_NAME).toString());
		if("".equals(strbldPattern.toString())){
			makeSingleCrt.put("recordsdupmerge.Criteria_Pattern", "null");
		}else{
		makeSingleCrt.put("recordsdupmerge.Criteria_Pattern", strbldPattern.toString());
		}
//		makeSingleCrt.put("recordsdupmerge.Criteria_Pattern1", strbldPattern1.toString());
		String updateValueCrt = tempJson.get("updatedValue").toString();
		if(updateValueCrt != null && !"".equals(updateValueCrt)){
			StringBuilder temp = new StringBuilder(200);
			temp.append(tempJson.get("UpdateOptions").toString()).append(seperator).append(tempJson.get("updateFieldValue").toString()).append(seperator).append(updateValueCrt);
			makeSingleCrt.put("recordsdupmerge.Action",temp.toString());
		}
		if( !tempJson.get("selFieldMerge1").toString().equals("None") ){
			makeSingleCrt.put("recordsdupmerge.MergeField_1",tempJson.get("selFieldMerge1").toString());
		}
		if( !tempJson.get("selFieldMerge2").toString().equals("None") ){
			makeSingleCrt.put("recordsdupmerge.MergeField_2",tempJson.get("selFieldMerge2").toString());
		}
		if( !tempJson.get("selFieldMerge3").toString().equals("None") ){
			makeSingleCrt.put("recordsdupmerge.MergeField_3",tempJson.get("selFieldMerge3").toString());
		}
		String isNew = tempJson.get("isNew").toString();
		String resopnse="";
		
		if(isNew == "true"){
			recordFormatData.put(makeSingleCrt);
			resopnse = ZohoCrmUtil.insertRecords("recordsdupmerge_DupMergeCRTModule", recordFormatData, user.getAuthToken());
		}else{
			String criteriaID = tempJson.get("FIELD_ID").toString(); 
			makeSingleCrt.put("id", criteriaID);
			recordFormatData.put(makeSingleCrt);
			resopnse = ZohoCrmUtil.updateRecords("recordsdupmerge_DupMergeCRTModule", recordFormatData, user.getAuthToken());
		}
		//makeSingleCrt.put("isNew",isNew);
//		recordFormatData.put(makeSingleCrt);
		return "success";
	}
	public void populateAllRecords(String module, String defaultSelModule)throws Exception{
		this.setAllCriteria(new LinkedList<CriteriaPojo>());
		String records="";
		JSONObject recordsObj;
		String none="None";
		JSONArray recordsArray=new JSONArray();
		if(user != null){
			records=ZohoCrmUtil.fetchAllRecords(module, user.getAuthToken());
			if(records != null && !records.equals(""))
			{
				recordsObj = new JSONObject(records);
				recordsArray = (JSONArray) recordsObj.get("data");
				JSONObject jsonOb=new JSONObject();
				if(recordsArray.length()>0)
				{
					for(int i= 0; i<recordsArray.length();i++){
					JSONObject recObj = (JSONObject) recordsArray.get(i);
					String moduleName = recObj.get("recordsdupmerge.Module_Name").toString();
					if(moduleName != null && moduleName.equals(defaultSelModule)){
						jsonOb = recObj;
					}
					}
				}
				if(jsonOb != null && jsonOb.length() >0){
					setCriteriaId(jsonOb.get("id").toString());
					setCriteriaName(jsonOb.get("recordsdupmerge.Criteria_Name").toString());
					String actionName = jsonOb.get("recordsdupmerge.Action").toString();
					String mergeFld1 = jsonOb.get("recordsdupmerge.MergeField_1").toString();
					String mergeFld2 = jsonOb.get("recordsdupmerge.MergeField_2").toString();
					String mergeFld3 = jsonOb.get("recordsdupmerge.MergeField_3").toString();
					if(mergeFld1 == null || mergeFld1.equals("null")){
						setMergeField1(none);
					}else{
						setMergeField1(mergeFld1);
					}
					if(mergeFld2 == null || mergeFld2.equals("null")){
						setMergeField2(none);
					}else{
						setMergeField2(mergeFld2);
					}
					if(mergeFld3 == null || mergeFld2.equals("null")){
						setMergeField3(none);
					}else{
						setMergeField3(mergeFld3);
					}
					if(actionName != null && !"".equals(actionName)){
						setAction(actionName);
					}
					String []operators=  jsonOb.get("recordsdupmerge.Criteria_Pattern").toString().split("(#\\W+)");
//					String []operators1= jsonOb.get("recordsdupmerge.Criteria_Pattern1").toString().split("(#\\W+)");
//					int k=0;
					for(int j=0; j<3; j++){
						CriteriaPojo criteria = new CriteriaPojo();
						String crt1 = jsonOb.get("recordsdupmerge.Criteria_"+(j+1)).toString();
						if(crt1 != null && !crt1.equals("null") && !"".equals(crt1)){
							String []arr = crt1.split("(#\\W+)");
							criteria.setFieldName(arr[0]);
							criteria.setFieldComparator(arr[1]);
							int len = arr.length;
							if(len>2 && arr[2] != null){
								criteria.setFieldCheckValue(arr[2]);
							}else{
								criteria.setFieldCheckValue("");
							}
							if(j>0){
								criteria.setOperator(operators[j-1]);
							}
							this.getAllCriteria().push(criteria);
						}
					}
				}
			}
		}
	}
	public JSONArray populateAllPicklistValues(String module, String fld_api) throws Exception{
		String allFields = ZohoCrmUtil.getTabFields(this.getUser().getAuthToken(), module);
		JSONObject json = new JSONObject(allFields);
		JSONArray jsonArr = (JSONArray)json.get("fields");
		JSONObject fieldInfo = new JSONObject();
		LinkedList<JSONObject> jsonFields= new LinkedList<JSONObject>();
		JSONArray picklistValues = new JSONArray();
		for(int fldIndex=0;fldIndex<jsonArr.length();fldIndex++)
		{
			fieldInfo =(JSONObject) jsonArr.get(fldIndex);
			String fieldApi = (String) fieldInfo.get(Constants.JSON_FIELD_API_NAME);
			if(fieldApi != null && fieldApi.equals(fld_api)){
				picklistValues = (JSONArray) fieldInfo.get("pick_list_values");
			}
		}
		return picklistValues;
	}
	public  void validateAuthToken(String authToken) throws Exception{
		try{
			HashMap response = ZohoCrmUtil.getAllModules(authToken);
		}catch(Exception e){
			setValidAuthToken(false);
		}
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LinkedList<CriteriaPojo> getAllCriteria() {
		return allCriteria;
	}
	public void setAllCriteria(LinkedList<CriteriaPojo> allCriteria) {
		this.allCriteria = allCriteria;
	}

	public LinkedList<JSONObject> getAllLeadsFields() {
		return allLeadsFields;
	}

	public void setAllLeadsFields(LinkedList<JSONObject> allLeadsFields) {
		this.allLeadsFields = allLeadsFields;
	}

	public Boolean isValidAuthToken() {
		return validAuthToken;
	}


	public void setValidAuthToken(Boolean validAuthToken) {
		this.validAuthToken = validAuthToken;
	}

	public Boolean isMetaPopulated() {
		return metaPopulated;
	}

	public void setMetaPopulated(Boolean metaPopulated) {
		this.metaPopulated = metaPopulated;
	}
	public String getCriteriaName() {
		return criteriaName;
	}
	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}
	public String getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(String criteriaId) {
		this.criteriaId = criteriaId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMergeField1() {
		return mergeField1;
	}
	public void setMergeField1(String mergeField1) {
		this.mergeField1 = mergeField1;
	}
	public String getMergeField2() {
		return mergeField2;
	}
	public void setMergeField2(String mergeField2) {
		this.mergeField2 = mergeField2;
	}
	public String getMergeField3() {
		return mergeField3;
	}
	public void setMergeField3(String mergeField3) {
		this.mergeField3 = mergeField3;
	}
}

