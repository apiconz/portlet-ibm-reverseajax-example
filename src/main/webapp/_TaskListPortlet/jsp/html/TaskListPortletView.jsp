<%@page session="false" contentType="text/html" pageEncoding="ISO-8859-1" import="java.util.*,javax.portlet.*,com.ibm.realtimetasklist.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>        
<%@taglib uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model" prefix="portlet-client-model" %>        
<portlet:defineObjects/>
<portlet-client-model:init>
      <portlet-client-model:require module="ibm.portal.xml.*"/>
      <portlet-client-model:require module="ibm.portal.portlet.*"/>   
</portlet-client-model:init>

<style type="text/css">
    body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }
</style>

<script type="text/javascript">
    dojo.require("dojo.io.script");
    dojo.require("dojox.cometd");
    dojo.require("dojox.cometd.callbackPollTransport");
    
    
    <portlet:namespace/>cometdConfig = function() {
    	//if not connected, connect to web messaging service servlet
    	if(dojox.cometd.state() == "unconnected"){
        	dojox.cometd.init("<%= renderRequest.getContextPath()+"/webmsgServlet" %>");
        }
        //subscribe to private topic
        dojox.cometd.subscribe("/taskList/<%= renderRequest.getRemoteUser() %>", "<portlet:namespace/>publishHandler");
        
        //subscribe to public topic
        dojox.cometd.subscribe("/taskList", "<portlet:namespace/>publishHandler");
        
    }
    dojo.addOnLoad(<portlet:namespace/>cometdConfig);
    commonHandler = function(message){alert(message);}
    
    <portlet:namespace/>publishHandler = function (message) {
            console.log("<portlet:namespace/>received", message);
            dojo.byId("<portlet:namespace/>taskList").
            appendChild(document.createElement("div")).appendChild(document.createTextNode(message.data));
    }
</script>

<div>
<h2>Real-Time Task List for user <u><font color="Red"> <%= renderRequest.getRemoteUser() %></font></u></h2>
</div>
<div id="<portlet:namespace/>taskList">
    <strong>
        Tasks:
    </strong>
</div>