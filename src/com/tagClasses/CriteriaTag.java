//$Id$
package com.tagClasses;
import static j2html.TagCreator.div;
import static j2html.TagCreator.input;
import static j2html.TagCreator.option;
import static j2html.TagCreator.select;
import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.json.JSONObject;

import com.util.Constants;
import com.util.CriteriaPojo;

public class CriteriaTag extends SimpleTagSupport{
private LinkedList<CriteriaPojo> criteriaList ;
private static LinkedList<JSONObject> fieldsList;
private Boolean createEmptyTemplate = false;
private Boolean createModuleDropdown= false;
private HashMap<String, String> moduleList;
private static String selectedModule;
private static ContainerTag moduleSelectTag;
private static Boolean setflag= false;
private static String opSelOption="";

private static final String CRITERIA_NAME_ELEMENT_CLASS = "CRName";
private static final String CRITERIA_FIELD_NAME_ELEMENT_CLASS = "CRFieldName";
private static final String CRITERIA_FIELD_NAME_OPERATOR_CLASS = "CROPERATOR";
private static final String CRITERIA_FIELD_COMPARATOR_ELEMENT_CLASS = "CRComparator";
private static final String CRITERIA_FIELD_CHECK_ELEMENT_CLASS = "CRCheck";

private static final String SELECT_TAG_CLASS = "fieldsSelectTag";
private static final String INPUT_TAG_CLASS = "inputTag";
private static final String CRITERIA_ROW_CLASS = "criteriaTR";
private static final String CRITERIA_ADD_CLASS = "addCriteria";
private static final String CRITERIA_DELETE_CLASS = "delCriteria";
private static final String FIELD_MODULE_NAME_ELEMENT_CLASS="ModuleName";

	@Override
	public void doTag() throws JspException, IOException
	{
    	JspWriter writer = getJspContext().getOut();
    	try
    	{
    		String criteriaHTML = null;
    		if(getCreateModuleDropdown()){
    			criteriaHTML =getModuleRow(moduleList, new CriteriaPojo());
    			writer.print(criteriaHTML.toString());
    		}
    		else if(getCreateEmptyTemplate())
    		{
					criteriaHTML = constructEmptyCriteria();
					writer.print(criteriaHTML);
    		}
    		else if (getCriteriaList()!=null) 
	    	{
    			criteriaHTML = constructCriterias(writer);
    			writer.println(criteriaHTML);
	    	}
    	//	writer.print(criteriaHTML);
    		
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	/*
	 * Construct an empty Criteria template
	 * 
	 * used as a reference for creating new LeadScore Criteria
	 */
	private String constructEmptyCriteria() throws Exception
	{
//		getModuleRow(getModuleList(), new CriteriaPojo());
		ContainerTag trTag = getCriteriaRow(getFieldsList(), new CriteriaPojo());
		return trTag.toString();
	}
	/*
	 * Construct a list of all LeadScore Criteria's
	 */
	private String constructCriterias(JspWriter writer) throws Exception
	{
    	StringBuilder htmlBuilder = new StringBuilder(100000);
    	for(int criteriaIndex = getCriteriaList().size() -1 ; criteriaIndex>=0;criteriaIndex--)
    	{
    		CriteriaPojo criteria = getCriteriaList().get(criteriaIndex);
    		opSelOption = criteria.getOperator();
    		if(!"".equals(opSelOption)){
        		setflag= true;
        		}
    		ContainerTag trTag = getCriteriaRow(fieldsList, criteria);
    		if(trTag!=null)
    		{
    			htmlBuilder.append(trTag.toString());
//    			writer.print(trTag.toString());
    		}
//    		if(criteriaIndex < getCriteriaList().size() -1){
//    		}
    	}
    	setflag= false;
    	return htmlBuilder.toString();// htmlBuilder.toString();
	}
	
	/*
	 * Construct the Dup Merge Module DropDown For Criteria  
	 */
	private static String getModuleRow(HashMap<String, String> modList ,CriteriaPojo criteria)throws Exception{
		if(selectedModule !=null && selectedModule !=""){
		 moduleSelectTag= constructModuleSelectTag(modList, selectedModule);
		}else{
//			criteria.setModuleName(selectedModule);
			moduleSelectTag= constructModuleSelectTag(modList, selectedModule);
		}
		ContainerTag trTag = tr().withClass(FIELD_MODULE_NAME_ELEMENT_CLASS)
				 .with( 
		   					td().with(
		   							div()
		   							.withClass("newSelect")
		   							.with(
		   									moduleSelectTag
		   								.withClass(FIELD_MODULE_NAME_ELEMENT_CLASS)
		   								.withName(Constants.FIELD_MODULE_NAME)
		   							)
		   							)
		   					);
		return trTag.toString();
	}
	private static ContainerTag getCriteriaRow(LinkedList<JSONObject> fieldsList,CriteriaPojo criteria)throws Exception
	{

		ContainerTag fieldSelectTag = constructFieldSelectTag(fieldsList, criteria.getFieldName(), true);
		if(fieldSelectTag==null)
		{
			return null;
		}
		ContainerTag trTag = tr().withClass(CRITERIA_ROW_CLASS);
		if(setflag){
			List<String> operatorsList= new ArrayList<String>();
			operatorsList.add("OR");
			operatorsList.add("AND");
			ContainerTag operatorSelTag = constructOperatorSelTag(operatorsList, opSelOption );
			trTag.with( td().with(
					 div()
					.withClass("newSelect")
					.with(
							operatorSelTag
							.withClass(CRITERIA_FIELD_NAME_OPERATOR_CLASS)
							.withName("operator")
						)
					)
					);
			setflag=false;
		}
		trTag = trTag
				 .with( 
						 td().with(
	        					div()
	        					.withClass("newSelect")
	        					.with(
	        							fieldSelectTag
	        							.withClass(CRITERIA_FIELD_NAME_ELEMENT_CLASS)
	        							.withName(Constants.FIELD_FIELD_NAME)
	        						)
	        					),
	   					td().with(
	   							div()
	   							.withClass("newSelect")
	   							.with(
	   									constructFieldSelectTag(Constants.CRITERIA_COMPARATOR, criteria.getFieldComparator(), false)
	   									.withClass(CRITERIA_FIELD_COMPARATOR_ELEMENT_CLASS)
	   									.withName(Constants.FIELD_FIELD_COMPARATOR)
	   									.withPlaceholder("Comparator")
	   								)
	   							),
	   					td().with(
	   							input()
		   							.withClass(INPUT_TAG_CLASS.concat(" ").concat(CRITERIA_FIELD_CHECK_ELEMENT_CLASS))
		   							.withValue(criteria.getFieldCheckValue())
		   							.withName(Constants.FIELD_FIELD_CHECK_VALUE)
		   							.withPlaceholder("Compare Value")
	   							),
    					td().with(
    							div()
    								.withClass(CRITERIA_DELETE_CLASS.concat(" ").concat("icon"))
    								.with(
    										div().withClass("minus")
    									)
	        					),
	        			td().with(
	        					div()
    								.withClass(CRITERIA_ADD_CLASS.concat(" ").concat("icon"))
    								.with(
    										div().withClass("plus")
    									)
	        					)
	        			);
		return trTag;
	}
	/*
	 * Construct The select tag with a list of all available Fields
	 * 
	 * and preselect The field from Criteria Object
	 */
	private static ContainerTag constructFieldSelectTag(LinkedList<JSONObject> fieldsList,String selectedOption, Boolean condition)throws Exception
	{
    	Boolean criteriaFieldVisible = false;
		List<Tag> optionsTagList=  new LinkedList<Tag>(); 
		for(int i = 0 ; i<fieldsList.size();i++)
        {
			JSONObject field = fieldsList.get(i);
			
			String fieldLable = (String)field.get(Constants.JSON_FIELD_ATTR_LABEL);
			String fieldType = (String)field.get(Constants.JSON_FIELD_ATTR_TYPE);
			String field_api=null;
			String value="";
			if( condition == true){
			field_api = (String )field.get(Constants.JSON_FIELD_API_NAME);
			}else{
				value= (String) field.get(Constants.JSON_COMPARATOR_VALUE);
			}
			ContainerTag option;
			if( condition == true){
				option  = option().withValue(field_api).attr("data-type",fieldType).attr("api_name",field_api).withText(fieldLable);
				if(field_api.equals(selectedOption))
				{
					criteriaFieldVisible = Boolean.TRUE;
					option.attr("selected", "selected");
				}
			}else{
				 option  = option().withValue(value).attr("data-type",fieldType).withText(fieldLable);
				 if(fieldLable.equals(selectedOption))
					{
						criteriaFieldVisible = Boolean.TRUE;
						option.attr("selected", "selected");
					}
			}
			optionsTagList.add(option);
        }
		/*
		 * if selectedOption (Criteria Field) is passed and  Not available in field listing
		 * 
		 * add selectedOption to the end and disable it 
		 */
//		if(selectedOption == null && !criteriaFieldVisible)
//		{
//			return null;
//		}
		ContainerTag selectTag = select().withClass(SELECT_TAG_CLASS).with(optionsTagList);
		return selectTag;
	}
	/*
	 * Getters and setters
	 */
	private static ContainerTag constructModuleSelectTag(HashMap<String, String> moduleList,String selectedOption)throws Exception{
		Boolean criteriaModuleVisible = false;
		List<Tag> optionsTagList=  new LinkedList<Tag>(); 
		Iterator itr = moduleList.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry pair = (Map.Entry) itr.next();
			String key = pair.getKey().toString();
			String value=pair.getValue().toString();
			
			ContainerTag option  = option().withValue(key).withText(key);
			 if(key.equals(selectedOption)){
				criteriaModuleVisible = Boolean.TRUE;
				option.attr("selected", "selected");
			}
			optionsTagList.add(option);
		}
		
		if(selectedOption!=null && !criteriaModuleVisible )
		{
			return null;
		}
		ContainerTag selectTag = select().withClass(SELECT_TAG_CLASS).attr("onchange","getFields()").with(optionsTagList);
		return selectTag;
	}
	public static ContainerTag constructOperatorSelTag(List operators, String selOption){
		List<Tag> operatorOptionList = new LinkedList<Tag>();
		
		for(int i=0; i<2;i++){
		String key=operators.get(i).toString();
		ContainerTag option  = option().withValue(key).withText(key);
		
		if(key.equals(selOption)){
			option.attr("selected", "selected");
		}
		operatorOptionList.add(option);
		}
		ContainerTag selectTag = select().withClass(SELECT_TAG_CLASS).with(operatorOptionList);
		return selectTag;
	}
	public LinkedList<CriteriaPojo> getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(LinkedList<CriteriaPojo> criteriaList) {
		this.criteriaList = criteriaList;
	}

	public LinkedList<JSONObject> getFieldsList() {
		return fieldsList;
	}

	public Boolean getCreateModuleDropdown() {
		return createModuleDropdown;
	}
	public void setCreateModuleDropdown(Boolean createModuleDropdown) {
		this.createModuleDropdown = createModuleDropdown;
	}
	public void setFieldsList(LinkedList<JSONObject> fieldsList) {
		this.fieldsList = fieldsList;
	}
	public Boolean getCreateEmptyTemplate() {
		return createEmptyTemplate;
	}
	public void setCreateEmptyTemplate(Boolean createEmptyTemplate) {
		this.createEmptyTemplate = createEmptyTemplate;
	}
	public HashMap<String, String> getModuleList() {
		return moduleList;
	}
	public void setModuleList(HashMap<String, String> moduleList) {
		this.moduleList = moduleList;
	}
	public String getSelectedModule() {
		return selectedModule;
	}
	public void setSelectedModule(String selectedModule) {
		this.selectedModule = selectedModule;
	}
}

