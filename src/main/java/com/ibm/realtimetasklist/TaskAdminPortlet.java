package com.ibm.realtimetasklist;

import java.io.*;
import java.util.*;
import javax.portlet.*;

import com.ibm.websphere.webmsg.publisher.Publisher;
import com.ibm.websphere.webmsg.publisher.PublisherException;
import com.ibm.websphere.webmsg.publisher.jndijms.BayeuxJmsTextMsg;
import com.ibm.websphere.webmsg.publisher.jndijms.JmsPublisherServlet;

/**
 * A sample portlet based on GenericPortlet
 */
public class TaskAdminPortlet extends GenericPortlet {

	public static final String JSP_FOLDER    = "/_TaskAdminPortlet/jsp/";    // JSP folder name

	public static final String VIEW_JSP      = "TaskAdminPortletView";         // JSP file name to be rendered on the view mode
	public static final String SESSION_BEAN  = "TaskAdminPortletSessionBean";  // Bean name for the portlet session
	public static final String FORM_SUBMIT   = "TaskAdminPortletFormSubmit";   // Action name for submit form
	public static final String FORM_TEXT     = "TaskAdminPortletFormText";     // Parameter name for the text input
	/**
	 * @see javax.portlet.Portlet#init()
	 */
	public void init() throws PortletException{
		super.init();
	}

	/**
	 * Serve up the <code>view</code> mode.
	 * 
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		// Set the MIME type for the render response
		response.setContentType(request.getResponseContentType());

		// Check if portlet session exists
		TaskAdminPortletSessionBean sessionBean = getSessionBean(request);
		if( sessionBean==null ) {
			response.getWriter().println("<b>NO PORTLET SESSION YET</b>");
			return;
		}

		// Invoke the JSP to render
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(getJspFilePath(request, VIEW_JSP));
		rd.include(request,response);
	}

	/**
	 * Process an action request.
	 * 
	 * @see javax.portlet.Portlet#processAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, java.io.IOException {

		String taskString = request.getParameter("TaskString");
		String taskOwner = request.getParameter("TaskOwner");
		
		//Here comes the code that will create the task in database or process engine or whatever
		
		//After creating the task we will push a notification through the owner's topic
		Publisher publisher = (Publisher) getPortletContext().getAttribute(
		        JmsPublisherServlet.PUBLISHER_SERVLET_CONTEXT_KEY);
		try {
			if("taskcommon".equals(taskOwner)){//public task
				publisher.publish(new BayeuxJmsTextMsg("/taskList", taskString));
			}else{//private task
				publisher.publish(new BayeuxJmsTextMsg("/taskList/"+taskOwner, taskString));
			}
		} catch (PublisherException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get SessionBean.
	 * 
	 * @param request PortletRequest
	 * @return TaskAdminPortletSessionBean
	 */
	private static TaskAdminPortletSessionBean getSessionBean(PortletRequest request) {
		PortletSession session = request.getPortletSession();
		if( session == null )
			return null;
		TaskAdminPortletSessionBean sessionBean = (TaskAdminPortletSessionBean)session.getAttribute(SESSION_BEAN);
		if( sessionBean == null ) {
			sessionBean = new TaskAdminPortletSessionBean();
			session.setAttribute(SESSION_BEAN,sessionBean);
		}
		return sessionBean;
	}

	/**
	 * Returns JSP file path.
	 * 
	 * @param request Render request
	 * @param jspFile JSP file name
	 * @return JSP file path
	 */
	private static String getJspFilePath(RenderRequest request, String jspFile) {
		String markup = request.getProperty("wps.markup");
		if( markup == null )
			markup = getMarkup(request.getResponseContentType());
		return JSP_FOLDER + markup + "/" + jspFile + "." + getJspExtension(markup);
	}

	/**
	 * Convert MIME type to markup name.
	 * 
	 * @param contentType MIME type
	 * @return Markup name
	 */
	private static String getMarkup(String contentType) {
		if( "text/vnd.wap.wml".equals(contentType) )
			return "wml";
        else
            return "html";
	}

	/**
	 * Returns the file extension for the JSP file
	 * 
	 * @param markupName Markup name
	 * @return JSP extension
	 */
	private static String getJspExtension(String markupName) {
		return "jsp";
	}

}
