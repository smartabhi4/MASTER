package com.util;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

public class Constants
{
	public static String CriteriaID="CriteriaId";
	public static String CriteriaName="CriteriaName";
	public static String AllCriteria = "allCriteria";
	public static final String ZOHOCRM_URL = "ZOHOCRM_URL";
	public static final String ZOHOCRM_OAUTH_SCOPE = "ZOHOCRM_OAUTH_SCOPE";
	public static final String ZOHOCRM_OAUTH_URL = "ZOHOCRM_OAUTH_URL";
	public static final String ZOHOCRM_OAUTH_CLIENTID = "ZOHOCRM_OAUTH_CLIENTID";
	public static final String DEV_MODE = "DEV_MODE";
	public static final String PLUGIN_TYPE = "PLUGIN_TYPE";
	public static final String PLUGIN_TYPE_WITH_PLUGIN = "WITH_PLUGIN";
	public static final String PLUGIN_TYPE_STAND_ALONE	 = "STAND_ALONE";
	public static final String CACHE_CRM_META="CACHE_CRM_META";
    public static final String FIELD_MODULE_NAME="Module Name";
	public static final String TABLABLE_DUPLICATE_MERGE="DUPLICATE_MERGE_CRITERIA";
	public static final String TABNAME_LEADS = "Leads";
	public static final String PLUGIN_NAMESPACE= "pluginNameSpace";
	
	
	public static final String COMPONENT_LEAD="Leads";
	public static final String COMPONENT_DUPLICATE_MERGE="DUPLICATE_MERGE_CRITERIA";

	/*
	 * ZohoCRM error Codes
	 */
	public static final String ZOHOCRM_ERRORCODE_INVALID_TICKET="4834";
	/*
	 * XML Constants
	 */
	public static final String JSON_FIELD_ATTR_LABEL="field_label";
	public static final String JSON_FIELD_ATTR_TYPE="data_type";
	public static final String JSON_FIELD_ATTR_MAXLENGTH="length";
	public static final String JSON_FIELD_API_NAME="api_name";
	public static final String JSON_COMPARATOR_VALUE="value";

	/*
	 *  Field Constants
	 */
	public static final String FIELD_NAME="Name";
	public static final String FIELD_CRITERIA_NAME="Criteria Name";
	public static final String FIELD_FIELD_NAME="Criteria-Field Name";
	public static final String FIELD_FIELD_COMPARATOR="Criteria-Comparator";
	public static final String FIELD_FIELD_CHECK_VALUE="Criteria-Check";
	public static final String FIELD_AND_CRITERIA_COND="Field-Criteria-Cond";
	public static final String FIELD_FIELD_TO_DELETE="toDelete";
	public static final String FIELD_FIELD_ID="Field_ID";
	public static final String MODULE_NAME="Module Name";
	public static final String FIELD_OPERATOR="operator";
	
	/*
	 * Session Variables
	 */
	public static final String CRM_META_IN_SESSION = "CRM_META_IN_SESSION";

	/*
	 * JsonResponse
	 */
	public static final String RESULT = "result";
	public static final String SUCCESS= "success";
	public static final String ERROR = "error";
	public static final String MESSAGE = "message";
	public static final String SESSION_UNAVAILABLE = "SESSION_UNAVAILABLE";
	
	/*
	 * Criteria Constants
	 */
	public static final LinkedList<JSONObject> CRITERIA_COMPARATOR = new LinkedList<JSONObject>();
	static{
		try {
//			CRITERIA_COMPARATOR.push(new JSONObject()
//											.put("field_label","isn't")
//											.put("data_type","ALL")
//											.put("value","isn't"));
//			
//			CRITERIA_COMPARATOR.push(new JSONObject()
//											.put("field_label","greater than")
//											.put("data_type","NUMERIC"));
//			
//			CRITERIA_COMPARATOR.push(new JSONObject()
//											.put("field_label","less than")
//											.put("data_type","NUMERIC"));
//			
			CRITERIA_COMPARATOR.push(new JSONObject()
											.put("field_label","equals")
											.put("data_type","ALL")
											.put("value", "equals"));
			
//			CRITERIA_COMPARATOR.push(new JSONObject()
//											.put("field_label","contains")
//											.put("data_type","ALL")
//											.put("value", "contains"));
//			
//			CRITERIA_COMPARATOR.push(new JSONObject()
//											.put("field_label","doesn't contain")
//											.put("data_type","ALL")
//											.put("value","doesn't_contain"));
//			
			CRITERIA_COMPARATOR.push(new JSONObject()
											.put("field_label","starts with")
											.put("data_type","ALL")
											.put("value", "starts_with"));
//			CRITERIA_COMPARATOR.push(new JSONObject()
//											.put("field_label","ends with")
//											.put("data_type","ALL")
//											.put("value", "ends_with"));
//			
//			CRITERIA_COMPARATOR.push(new JSONObject()
//											.put("field_label","is empty")
//											.put("data_type","NONE"));
//			
//			CRITERIA_COMPARATOR.push(new JSONObject()
//											.put("field_label","isn't empty")
//											.put("data_type","NONE"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
} 

