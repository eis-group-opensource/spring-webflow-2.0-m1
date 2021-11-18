/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.struts;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.springframework.webflow.context.servlet.ServletExternalContext;

/**
 * Provides consistent access to a Struts environment from within the Spring Web Flow system. Represents the context of
 * a request into SWF from Struts.
 * 
 * @author Keith Donald
 */
public class StrutsExternalContext extends ServletExternalContext {

	/**
	 * The Struts action mapping associated with this request.
	 */
	private ActionMapping actionMapping;

	/**
	 * The Struts action form associated with this request.
	 */
	private ActionForm actionForm;

	/**
	 * Creates a new Struts external context.
	 * @param mapping the action mapping
	 * @param form the action form
	 * @param context the servlet context
	 * @param request the request
	 * @param response the response
	 */
	public StrutsExternalContext(ActionMapping mapping, ActionForm form, ServletContext context,
			HttpServletRequest request, HttpServletResponse response) {
		super(context, request, response);
		this.actionMapping = mapping;
		this.actionForm = form;
	}

	/**
	 * Returns the action form.
	 */
	public ActionForm getActionForm() {
		return actionForm;
	}

	/**
	 * Returns the action mapping.
	 */
	public ActionMapping getActionMapping() {
		return actionMapping;
	}
}