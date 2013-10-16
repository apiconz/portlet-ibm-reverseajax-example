<%@page session="false" contentType="text/html" pageEncoding="ISO-8859-1" import="java.util.*,javax.portlet.*,com.ibm.realtimetasklist.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>        
<%@taglib uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model" prefix="portlet-client-model" %>        
<portlet:defineObjects/>
<portlet-client-model:init>
      <portlet-client-model:require module="ibm.portal.xml.*"/>
      <portlet-client-model:require module="ibm.portal.portlet.*"/>   
</portlet-client-model:init> 
<div>
	<form action="<portlet:actionURL/>">
		<input type="text" name="TaskString">
		<select name="TaskOwner">
			<option value="taskcommon">Common</option>
			<option value="taskuser1" >User 1</option>
			<option value="taskuser2" >User 2</option>
			<option value="taskuser3" >User 3</option>
		</select>
		<input type="submit" name="Submit">
	</form>
</div>