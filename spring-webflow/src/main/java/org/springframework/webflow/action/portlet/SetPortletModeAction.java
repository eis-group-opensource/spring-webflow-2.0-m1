/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action.portlet;

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.context.portlet.PortletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Action implementation that changes a PortletResponse mode. The action only generates the
 * {@link org.springframework.webflow.action.AbstractAction#success()} event. All error cases result in an exception
 * being thrown.
 * <p>
 * This class is usefull when you want to change the current PortletMode before entering a specific state, e.g. it can
 * be the first state in a subflow.
 * <p>
 * Note: if you can, change the PortletMode using Portlet URLs (PortletURL class or portlet TAG).
 * 
 * @author J.Enrique Ruiz
 * @author Cesar Ordinana
 * @author Erwin Vervaet
 */
public class SetPortletModeAction extends AbstractAction {

	/**
	 * The portlet mode to set can be specified in an action state action attribute with this name ("portletMode").
	 */
	public static final String PORTLET_MODE_ATTRIBUTE = "portletMode";

	/**
	 * The default portlet mode. Default is "view".
	 */
	private PortletMode portletMode = PortletMode.VIEW;

	/**
	 * Returns the mode that will be set in the response.
	 */
	public PortletMode getPortletMode() {
		return portletMode;
	}

	/**
	 * Sets the mode that will be set in the response.
	 */
	public void setPortletMode(PortletMode portletMode) {
		Assert.notNull(portletMode, "The portlet mode is required and cannot be null");
		this.portletMode = portletMode;
	}

	/**
	 * Sets the PortletMode.
	 * @param context the action execution context, for accessing and setting data in "flow scope" or "request scope"
	 * @return the action result event
	 * @throws Exception an <b>unrecoverable</b> exception occured, either checked or unchecked
	 */
	protected Event doExecute(RequestContext context) throws Exception {
		Assert.isInstanceOf(PortletExternalContext.class, context.getExternalContext(), "'"
				+ ClassUtils.getShortName(this.getClass()) + "' can only work with 'PortletExternalContext': ");
		PortletExternalContext portletContext = (PortletExternalContext) context.getExternalContext();
		if (portletContext.getResponse() instanceof ActionResponse) {
			PortletMode mode = (PortletMode) context.getAttributes().get(PORTLET_MODE_ATTRIBUTE, PortletMode.class,
					getPortletMode());
			((ActionResponse) portletContext.getResponse()).setPortletMode(mode);
			return success();
		} else {
			// portlet mode and the window state can be changed through
			// ActionResponse only, if this is not the case, it means that this
			// action has been invoked directly in a RenderRequest
			throw new IllegalStateException("SetPortletModeAction can only be invoked within a Action request -- "
					+ "make sure you are not invoking it in a RenderRequest");
		}
	}
}