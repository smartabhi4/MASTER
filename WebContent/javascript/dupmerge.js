function successMsg(message)
{
	$('.loadingMsg').slideUp("slow",function()
	{
		$('.successMsg').text(message);
		$('.successMsg').slideDown(function() 
			{
				$('.successMsg').delay(3000).slideUp();
			});
	});
}
function loadingStart()
{
	$('.loadingMsg').text("Loading...");
	$('.loadingMsg').slideDown();
}
$(document).ready(function(){

	negativeSignDisabled();
	hideAppropriateElements();
	bindAllElements();
});
/*
 * Function to bind all elements to respective trigger
 */
function bindAllElements()
{
	$(".delCriteria").click(function(){
		deleteCriteria(this);
	});
	
	$(".addCriteria").click(function(){
		addNewCriteria();
	});
	$(".CRComparator").change(function(){
		comparatorDataTypeValidation()
	});
}
/*
 * Hide unnecessary AddCriteriaButton and reset Link from page
 */
function hideAppropriateElements()
{
	/*
	 * Hide unnecessary Dom Elemeents
	 */
	var criteriaCount = $("#criteriaList .criteriaTR").filter(":visible").size();
	/*
	 * if criteria Count is 0 (No criteria is added)
	 * show only the addCriteriaButton
	 */
	if(criteriaCount==0)
	{
		$("#criteriaHeadder").hide();
		$("#saveCriteriaBTN").hide();
		$("#resetCriteriaBTN").hide();
		$("#addCriteriaBTN").show();
	}
	else
	{
		$("#criteriaHeadder").show();
		$("#saveCriteriaBTN").show();
		$("#resetCriteriaBTN").show();
		$("#addCriteriaBTN").hide();
	}
	/*
	 * Hide all but last add CriteriaAdd button
	 */
	$("#criteriaList .addCriteria").show();
	$("#criteriaList .addCriteria").filter(":visible").not(":last").hide();
	
	/*
	 * Do DataType validation for all Comparators and check Value
	 */
	comparatorDataTypeValidation();
}
/*
 * Handle Compare value Validation
 * remove compare value for -  is empty and is not empty
 * handle type = number for numeric comparison
 */
function comparatorDataTypeValidation()
{
	$("#criteriaList .CRComparator :selected").each(function(){
	    var dataType = $(this).attr("data-type");
	    var correspondingCompareElement = $(this).parents("tr").find(".CRCheck")
	    
	    /*
	     *  remove all dataSpecific Validations before applying new validation
	     */
	     correspondingCompareElement.prop("disabled",false);
	     correspondingCompareElement.removeAttr("type");
	    if(dataType==="NONE"){
	    	
//	    	correspondingCompareElement.val("");
	        correspondingCompareElement.prop("disabled",true);
	    }
	    if(dataType==="NUMERIC"){
	    	correspondingCompareElement.attr("type","number");
	    }
	    
	});
}
/*
 * Fetch criteria list from server and re populate them using ajax
 * 
 * Ensured new criterias have their respective ids populated to them
 */
function reloadCriteriaList()
{
	url = window.location;
	$("#content").load(window.location.pathname+" #content > *",function(){
		bindAllElements()
		hideAppropriateElements();
		negativeSignDisabled();
	});
}
/*
 * Add a new criteria block to the list
 * 
 * Clone it from the hidden reference Criteria block present in the page
 */
function addNewCriteria()
{
	debugger;
	var criteriaCount = $("#criteriaList .criteriaTR").filter(":visible").size();
	if(criteriaCount >2){
		$('#saveCriteriaBTN').prop("disabled", true);
	}
		if(criteriaCount==0){
			$("tbody").append($("#referenceCriteria").find("tr"));
		}else{
			$("tbody").append($("#referenceCriteria").find("tr"));
		    var test1 =$('tr').find('td').find('div.newSelect').find("select").closest('select.CRFieldName');
		    var len = test1.length;
		    $(test1[len-1]).parent().parent('td').before($('<td>'+$('.operators').html()+'</td>'));
	     }
	negativeSignDisabled();
	hideAppropriateElements();
}
/*
 * Delete the criteria from the list
 * 
 * if it is a newly added criteria 
 * 		-> remove it directly
 * if it is an existing criteria
 *  	-> mark with deleteFlag
 */
function deleteCriteria(btn)
{
	debugger;
	var criteriaCount = $("#criteriaList .criteriaTR").filter(":visible").size();
	if(criteriaCount<=4){
		$('#saveCriteriaBTN').prop("disabled", false);
	}
	var criteriaTr = $(btn).parents("tr")[0];
	var criteriaId = $("#criteriaId").val();
	if(criteriaId!= null  && criteriaId !="")
	{
		$(criteriaTr).attr("toDelete","true");
		$(criteriaTr).hide();
//		$(criteriaTr).find("td.operators").remove();
	}
	else
	{
		$(criteriaTr).find("td.operators").remove();
		$(criteriaTr).remove();
		$(criteriaTr).hide();
	}
	negativeSignDisabled();
	hideAppropriateElements();
}

/*
 * Send the normalised list of all the criterias to the server
 */
function saveDupMergeCriteria()
{
	/*
	 * Convert JsonArray of all the Criterias into String for POST
	 * 
	 */
	debugger;
	loadingStart();
	var criteriaPostParam= JSON.stringify(serialiseAllCriteria());
	$.ajax({
		type: 'POST',
		url:"saveCriteria.do",
		data: "criteriaList="+criteriaPostParam,
		dataType: "json",
	}).done(function(response){
		/*
		 * if criteria saved succesfullt
		 * reload the criterialist and send an alert message
		 * this will help populate all the required it's
		 */
		if(response.result==="success")
		{
			reloadCriteriaList();
			successMsg("Criteria Added Successfully");
		}	
		else if(response.result==="error")
		{
			if(response.message==="SESSION_UNAVAILABLE")
			{
				alert("Session unavailable, Kindly reload this page");
			}
		}
	});
}
/*
 * Update the content in CriteriaView page for reset 
 */
function resetCriteriaBuilder()
{
	reloadCriteriaList();
}
/*
 * Serialise the criteria list
 */
function serialiseAllCriteria()
{
	//Serialise through all Criteria TR in the page
	var flag= true;
	var selModule= $('.ModuleName').val();	
	var crtName = $('#CRName').val();
	var criteriaId = $('#criteriaId').val();
	var modData = {};
	modData["Module Name"]=selModule;
	modData["Criteria Name"]= crtName;
	
	var updatedOptions = $("#UpdateOptions :selected").val();
	var updatedFieldVal = $("#updateFieldValue :selected").val();
	var updateValue= $("#updatedValue").val();
	modData["UpdateOptions"]= updatedOptions;
	modData["updateFieldValue"]=updatedFieldVal;
	modData["updatedValue"]=updateValue;
	
	var selFieldMerge1 = $("#selFieldMerge1 :selected").val();
	var selFieldMerge2 = $("#selFieldMerge2 :selected").val();
	var selFieldMerge3 = $("#selFieldMerge3 :selected").val();
	
	modData["selFieldMerge1"]= selFieldMerge1;
	modData["selFieldMerge2"]= selFieldMerge2;
	modData["selFieldMerge3"]= selFieldMerge3;
	if(criteriaId ==null || criteriaId ==""){
		modData["isNew"]= true;
		modData["FIELD_ID"]="";
	}else{
		modData["FIELD_ID"]=criteriaId;
		modData["isNew"]= false;
	}
	var allCriteria = [];
	var count=1;
	$("#criteriaList .criteriaTR").each(function(){
		/*
		 * Data Contains all Criteria Sepcific Data
		 */
		var data = JSON.parse("{}");
		/*
		 * Fetch Criteria ID
		 */
//		var criteriaId = $(this).attr("id");
//		if(!criteriaId && flag)
//		{
//			data["isNew"]=true;
//		}
//		else
//		{
//			data["Field_ID"]=criteriaId;
//		}
		/*
		 * Check for record Delete attribute 
		 */
		var toDelete = $(this).attr("toDelete");
		if(toDelete)
		{
			data["toDelete"]="true";
		}
		
		/*
		 * Fetch all criteria data inside TR
		 */
		var allInputs = $(this).find("input,select");
		//Loop through all input elements inside this TR
		$(allInputs).each(function(){
				var inputName = $(this).attr("name");
				var value = $(this).val();
				if(!$(this).prop("disabled"))
				{
					data[inputName] = value;
				}
				else
				{
					data[inputName] = "";
				}
		});
		allCriteria.push(data);
	});
	allCriteria.push(modData);
	return allCriteria;
}	
function getFields(){
	var dModule=$('.ModuleName option:selected').val();
	var mod= $('.ModuleName option:selected').text();
	var module = mod.split(dModule).join("");
	if(module ==""){
		module=dModule;
	}
	var criteriaCount = $("#criteriaList .criteriaTR").filter(":visible").size();
	var test1 =$('tr').find('td').find('div.newSelect').find("select").closest('select.CRFieldName');
	var len= test1.length;
	$.ajax({
		type: "GET",
		url:"/merge/getAllDetails.do?module="+module,
	}).done(function(response){
		$('#mainContent').html(response);
		debugger;
		negativeSignDisabled();
	});
}
function deleteModuleCriteria(criteriaId){
	var module= $('.ModuleName').val();
	var data="";
	$.ajax({
		type: "DELETE",
		url:"deleteModuleRecord.do?CriteriaId="+criteriaId,
	}).done(function(response){
		var result = response.result;
		if(result == "success"){
			reloadCriteriaList();
			successMsg("Criteria Created Successfully");
		}else{
			if(result ==="SESSION_UNAVAILABLE")
			{
				alert("Session unavailable, Kindly reload this page");
			}
		}
	});
}
function negativeSignDisabled(){
	debugger;
	var criteriaCount = $("#criteriaList .criteriaTR").filter(":visible").size();
	if(criteriaCount>1){
		$('#criteriaList').find('.delCriteria').first().addClass("disabledbutton");
	}else{
		$('#criteriaList').find('.delCriteria').first().removeClass('disabledbutton');
	}
}
function setFldPicklistValues(){
	debugger;
	var type = $('#updateFieldValue option:selected').attr('data-type');
	var fld_api = $('#updateFieldValue option:selected').attr('value');
	var module =  $('.ModuleName option:selected').attr('value');
	var data="";
//	if(type=="picklist"){
//		$.ajax({
//			type: "GET",
//			url:"getAllDetails.do",
//			data:data+"module="+module+"&fldtype="+type+"&fld_api="+fld_api,
//		}).done(function(response){
//			debugger;
	debugger;
			//var pickListArray = JSON.parse(fldValArray);
		if(type== "picklist"){
		
			for(j = 0; j<fldValArray.length;j++){
				var valsArray = fldValArray[j];
				if(valsArray.data_type == type && valsArray.api_name== fld_api){
					var jsonPicklistArr = valsArray.pick_list_values;
					for (i=0;i<jsonPicklistArr.length;i++) {
						var obj = jsonPicklistArr[i];
						var key=obj.display_value;
						var val=obj.actual_value;
						if(key != "None"){
							var option= "<option value='"+val+"'>"+key+"</option>";
						$('#updatedValuePicklist').append(option);
						}else{
							var option= "<option value='"+val+"' selected >"+key+"</option>";
							$('#updatedValuePicklist').append(option);
						}
					}
				}
			}
		}
}
