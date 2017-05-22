package com.util;

import static j2html.TagCreator.tag;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
public class CriteriaPojo
{
	private String moduleName;
	private String criteriaId;
	private String criteriaName;
	private String fieldName;
	private String fieldComparator;
	private String fieldCheckValue;
	private String operator="";
//	private Boolean toDeleteFlag;
	/*
	 * json to POJO
	 */
	public CriteriaPojo()
	{
//		this.moduleName = (String) json.get(Constants.MODULE_NAME);
//		this.criteriaId =  (String)json.get(Constants.FIELD_FIELD_ID);
//		this.setCriteriaName((String)json.get(Constants.FIELD_CRITERIA_NAME));
	}
	public CriteriaPojo(JSONObject criteriaData) throws JSONException
	{
		com.util.JSONObject json = new com.util.JSONObject(criteriaData.toString());
		if(json!=null)
		{
			this.operator = (String ) json.get(Constants.FIELD_OPERATOR);
			this.fieldName =  (String)json.get(Constants.FIELD_FIELD_NAME);
			this.fieldComparator =  (String)json.get(Constants.FIELD_FIELD_COMPARATOR);
			this.fieldCheckValue =  (String)json.get(Constants.FIELD_FIELD_CHECK_VALUE);
//			String toDelete = (String)json.get(Constants.FIELD_FIELD_TO_DELETE);
//			this.setToDeleteFlag(new Boolean(toDelete));
			
//			if(this.getFieldChangeValue()==null || this.getFieldChangeValue().isEmpty())
//			{
//				this.setFieldChangeValue("0");
//			}
//			if(this.getCriteriaName()==null || this.getCriteriaName().isEmpty())
//			{
//				this.setCriteriaName("Criteria");
//			}
		}
	}
	public String toZohoCRMXmlData(String recordName,Integer rowCount)
	{
		List<Tag> FieldAttributes = new ArrayList<Tag>();

		/*
		 * Add Criteria ID tag only when editing an existing component
		 * 
		 * Ignore the tag to new Record
		 */
//		if(this.criteriaId!=null)
//		{
//		FieldAttributes.add(
//				tag("FL")
//				.attr("val", "Id")
//				.withText(this.criteriaId)
//				);
//		}
//		FieldAttributes.add(
//				tag("FL")
//				.attr("val", recordName.concat(" ").concat(Constants.MODULE_NAME))
//				.withText(this.getModuleName())
//				);
//
//		FieldAttributes.add(
//				tag("FL")
//				.attr("val", recordName.concat(" ").concat(Constants.FIELD_NAME))
//				.withText(this.getCriteriaName())
//				);
		FieldAttributes.add(
				tag("FL")
				.attr("val", Constants.FIELD_FIELD_NAME)
				.withText(this.fieldName)
				);
		
		FieldAttributes.add(
				tag("FL")
				.attr("val", Constants.FIELD_FIELD_COMPARATOR)
				.withText(this.fieldComparator)
				);
		
		FieldAttributes.add(
				tag("FL")
				.attr("val", Constants.FIELD_FIELD_CHECK_VALUE)
				.withText(this.fieldCheckValue)
				);
		
		/*
		 * Construct a row tag with all the fields for the record
		 */
		ContainerTag dataTag 
		= tag("row")
		.attr("no", rowCount.toString())
		.with(FieldAttributes);
		return dataTag.toString();
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldComparator() {
		return fieldComparator;
	}
	public void setFieldComparator(String fieldComparator) {
		this.fieldComparator = fieldComparator;
	}
	public String getFieldCheckValue() {
		return fieldCheckValue;
	}
	public void setFieldCheckValue(String fieldCheckValue) {
		this.fieldCheckValue = fieldCheckValue;
	}
	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
//		toString.append("criteriaId : ").append(criteriaId).append("\n");
//		toString.append("criteriaName : ").append(getCriteriaName()).append("\n");
		toString.append("fieldName : ").append(fieldName).append("\n");
		toString.append("fieldComparator : ").append(fieldComparator).append("\n");
		toString.append("fieldCheckValue : ").append(fieldCheckValue).append("\n");
		return toString.toString();
	}
	public String getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(String criteriaId) {
		this.criteriaId = criteriaId;
	}
//	public Boolean getToDeleteFlag() {
//		return toDeleteFlag;
//	}
//	public void setToDeleteFlag(Boolean toDeleteFlag) {
//		this.toDeleteFlag = toDeleteFlag;
//	}
	public String getCriteriaName() {
		return criteriaName;
	}
	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
} 

