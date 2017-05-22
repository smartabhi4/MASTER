<%String context= request.getContextPath() ;%>
<link rel="stylesheet" type="text/css" href="<%=context%>/dupmergeCss/criteriaBuilder.css">
<div id="mainContent">
<%@page buffer="128kb" %>
<%@taglib prefix="CBuilder" uri="criteriaTabLibrary" %>
<%@page import="com.util.CriteriaPojo" %>
<script type="text/javascript" src="<%=context%>/javascript/jquery-1.11.0.min.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
var fldValArray = <%=request.getAttribute("fldValsArray")%>;
</script>
<script type="text/javascript" src="<%=context%>/javascript/dupmerge.js"></script>
<style>
ul li{
  display: inline;
  color: brown;
}
li{
padding-right: 40px;
}
.hideFlds{
display:none;
}
.disabledbutton {
    pointer-events: none;
    opacity: 0.4;
}

</style>
<div class="successMsg" style="display:none"></div> 
<div class="loadingMsg" style="display:none"></div> 
<div id="content">
			<!-- reference criteriaBlock -->
			<p class="run-text">Duplicate Merge Criteria Builder</p>
			<div id="criteriaNameAndModuleDiv" style="margin:0 auto;width:860px;z-index:1; padding:4px; background:#8ceef7;postion: absolute;">
			<div style="height: 45px; margin-left: 30%; margin-right: auto; align: center;">
				<span>Module Name  :   </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<CBuilder:criteria createModuleDropdown="${true}" moduleList="${moduleList }" selectedModule="${selectedModule}" />
			</div>
			<input type="hidden" id="criteriaId" value="${CriteriaId}" />
			<div style="margin-top: 10px; height: 45px; margin-left: 30%; margin-right: auto; align: center;">
				<span style="margin-top: 2px;">Enter Criteria Name  :   </span>&nbsp;&nbsp;&nbsp;<input style="width: 200px; height:35px; padding: 0 5px;align: center; font-size:15px;" type="text" name="CRName" id="CRName" value="${CriteriaName}" placeholder="Criteria Name" required="required"/>
			</div>
			</div>
			<div id="referenceCriteriaDiv" style="margin-top: 30px;">
				<table style="height: 45px; margin-left: auto; margin-right: auto;" id="referenceCriteria">
					<tbody id="referenceCriteria">
						<CBuilder:criteria createEmptyTemplate="${true}" moduleList="${moduleList }" fieldsList="${fldList}" selectedModule="${selectedModule}"/>
					</tbody>
				</table>
			</div>

			<!-- criteria Listing Block -->
			<table style="height: 45px; width:860px ; margin:0 auto; background: #3bd7e2;" id="criteriaList">
				<tbody>
					<tr id="criteriaHeadder">
						<!-- <td class="center">Criteria Name</td> -->
						<td class="center">Field</td>
						<td class="center" colspan="2">Condition</td>
						<td class="center">Condition Value</td>
					</tr>
					<!-- print each row of criteria  -->
					<CBuilder:criteria criteriaList="${allCriteria}" fieldsList="${fldList}" />
				</tbody>
			</table>
			<div id="mergeCrtDiv" style="background: #0e8e79; width: 860px; margin: 0 auto;z-index:1; padding:4px;">
			<span style="margin-left:40%; font-size:20px; text-decoration: underline;">Set Merge Criteria</span>
			<div align="center" style="position:relative;"><ul>
			<li>
			Select Field 1:
			</li>
			<li>
			<select  id="selFieldMerge1" name="selFieldMerge1">
			<option value="None">None</option>
			<c:forEach var="index" items="${fldList}">
			<c:choose>
			<c:when test="${selMFld1 eq index.get('api_name')}">
				<option value="${index.get('api_name')}" selected="selected" ><c:out value="${index.get('field_label')}" /></option>
			</c:when>
			<c:otherwise>
				<option value="${index.get('api_name')}" ><c:out value="${index.get('field_label')}" /></option>
			</c:otherwise>
			</c:choose>
			</c:forEach>
			</select>
			</li>
			</ul>
			</div>
			<div align="center" style="position:relative;"><ul>
			<li>
			Select Field 2:
			</li>
			<li>
			<select id="selFieldMerge2" name="selFieldMerge2">
			<option value="None">None</option>
			<c:forEach var="index" items="${fldList}">
			<c:choose>
			<c:when test="${selMFld2 eq index.get('api_name')}">
				<option value="${index.get('api_name')}" selected="selected" ><c:out value="${index.get('field_label')}" /></option>
			</c:when>
			<c:otherwise>
				<option value="${index.get('api_name')}" ><c:out value="${index.get('field_label')}" /></option>
			</c:otherwise>
			</c:choose>
			</c:forEach>
			</select>
			</li>
			</ul>
			</div>
			<div align="center" style="position:relative;"><ul>
			<li>		
			Select Field 3:
			</li>
			<li>
			<select  id="selFieldMerge3" name="selFieldMerge3">
			<option value="None">None</option>
			<c:forEach var="index" items="${fldList}">
			<c:choose>
			<c:when test="${selMFld3 eq index.get('api_name')}">
				<option value="${index.get('api_name')}" selected="selected" ><c:out value="${index.get('field_label')}" /></option>
			</c:when>
			<c:otherwise>
				<option value="${index.get('api_name')}" ><c:out value="${index.get('field_label')}" /></option>
			</c:otherwise>
			</c:choose>
			</c:forEach>
			</select>
			</li>
			</ul>
			</div>
			<div align="center" style="position:relative;">
			<ul class="updateSetValue">
			<li>
			<select name="UpdateOptions" id="UpdateOptions"><option value="UPDATE">UPDATE</option></select>
			</li>
			<li>
			<select name="updateFieldValue" id="updateFieldValue" onchange="handleTypeUpdateFld(this)">
			<option value="None">None</option>
			<c:forEach var="index" items="${fldList}">
			<c:choose>
			<c:when test="${SelActionFld eq index.get('api_name')}">
				<option value="${index.get('api_name')}" selected="selected" data-type="text" ><c:out value="${index.get('field_label')}" /></option>
			</c:when>
			<c:otherwise>
				<option value="${index.get('api_name')}" data-type="${index.get('data_type')}" ><c:out value="${index.get('field_label')}" /></option>
			</c:otherwise>
			</c:choose>
			</c:forEach>
			</select>
			
			</ul>
			</div>
			</div>
			<div align="center">
			   <c:choose>
			    <c:when test="${not empty CriteriaId && CriteriaId != null }">
				<button class="deleteBTN" id="deleteModuleCriteria" onclick="deleteModuleCriteria('<c:out value="${CriteriaId}" />')">DELETE</button>
				</c:when>
				</c:choose>
				<button onclick="saveDupMergeCriteria()" class="okBTN"	id="saveCriteriaBTN">SAVE</button>
				<button onclick="resetCriteriaBuilder()" class="resetBTN" id="resetCriteriaBTN">RESET</button>
				<button onclick="addNewCriteria()" class="okBTN" id="addCriteriaBTN">Add Criteria</button>
				<!-- <button onclick=""/> -->
			</div>
		<%-- </c:if> --%>
		<div id="operators" class="operators" style="display:none;">
		   <div class="operatorDiv">
		  <select name="operator" id="operator" >
		      <option value="OR" >OR</option>
		      <option value="AND" >AND</option>
		  </select>
		  </div>
		</div>
		<!-- 	Check for Plugin is not  installed -->
		<%--  <c:if test="${!pluginInstalled}">
			<p class="run-text">DUPMERGE has not been installed in your Account</p>
		</c:if>  --%>
	<!-- 	Check for invalid AuthToken -->
</div>
</div>
<script>
$(document).ready(function(){
//	hideEveryTypeRelatedInputs
	handleTypeUpdateFld();
});
  function handleTypeUpdateFld(){
    	 debugger;
    	 var type = $('#updateFieldValue option:selected').attr('data-type');
    	 var check= $('.updateSetValue').find('li#updatedValueli').val();
    	 if(check != "undefined"){
    		 $('#updatedValueli').remove();
    	 }
    	 if((type == "text" || type=="website")){
    		 $('.updateSetValue').append('<li id="updatedValueli"><input name="updatedValueText" id="updatedValueText" placeholder="Update The Value" value="${SelActionVal}" /></li>');
    	 }else if(type=="textarea"){
    		 $('.updateSetValue').append('<li id="updatedValueli"><textarea name="updatedValueTextArea" id="updatedValueTextArea" placeholder="Update The Value"  class="hideFlds"><c:out value="${SelActionVal}"/></textarea>');
    	 }else if(type=="email"){
    		 $('.updateSetValue').append('<li id="updatedValueli"><input type="email" name="updatedValueEmail" id="updatedValueEmail" placeholder="Update The Value" value="${SelActionVal}" />');
    	 }else if(type=="phone" || type=="integer"){
    		 $('.updateSetValue').append('<li id="updatedValueli"><input type="number" name="updatedValueNumber" id="updatedValueNumber" placeholder="Update The Value" value="${SelActionVal}" />');
    	 }else if(type=="currency" || type=="double"){
    		 $('.updateSetValue').append('<li id="updatedValueli"><input type="number" step="0.01" name="updatedValueDouble" id="updatedValueDouble" placeholder="Update The Value" value="${SelActionVal}"/>');
    	 }else if(type=="boolean"){
    		 $('.updateSetValue').append('<li id="updatedValueli"><input type="checkbox" name="updatedValueCheckBox" id="updatedValueCheckbox" value="${SelActionVal}" checked="false" /></li>');
    	 }else if(type=="datetime"){
    		 $('.updateSetValue').append('<li id="updatedValueli"><input type="datetime" name="updatedValueDateTime" id="updatedValueDateTime" />');
    	 }else if(type=="picklist"){
    		$('.updateSetValue').append('<li id="updatedValueli"><select name="updatedValuePicklist" id="updatedValuePicklist" ></select></li>');
    		 setFldPicklistValues();
    		
    	 }
     }
  function hideEveryTypeRelatedInputs(){
	  $('#updatedValueText').hide();
		 $('#updatedValueTextArea').hide();
		 $('#updatedValueEmail').hide();
		 $('#updatedValueNumber').hide();
		 $('#updatedValueDouble').hide();
		 $('#updatedValueCheckbox').hide();
		 $('#updatedValueDateTime').hide();
  }
</script>
