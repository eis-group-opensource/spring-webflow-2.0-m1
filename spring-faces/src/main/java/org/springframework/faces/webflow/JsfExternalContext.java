/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import javax.faces.context.FacesContext;

import org.springframework.binding.collection.SharedMapDecorator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.LocalParameterMap;
import org.springframework.webflow.core.collection.LocalSharedAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.core.collection.SharedAttributeMap;

/**
 * Provides contextual information about a JSF environment that has interacted with SWF.
 * 
 * @author Keith Donald
 */
public class JsfExternalContext implements ExternalContext {

	/**
	 * The JSF Faces context.
	 */
	private FacesContext facesContext;

	/**
	 * The id of the action or "command button" that fired.
	 */
	private String actionId;

	/**
	 * The action outcome.
	 */
	private String outcome;

	/**
	 * An accessor for the JSF request parameter map.
	 */
	private ParameterMap requestParameterMap;

	/**
	 * An accessor for the JSF request attribute map.
	 */
	private MutableAttributeMap requestMap;

	/**
	 * An accessor for the JSF session map.
	 */
	private SharedAttributeMap sessionMap;

	/**
	 * An accessor for the JSF application map.
	 */
	private SharedAttributeMap applicationMap;

	/**
	 * Creates a JSF External Context.
	 * @param facesContext the JSF faces context
	 */
	public JsfExternalContext(FacesContext facesContext) {
		this.facesContext = facesContext;
		initMaps(facesContext);
	}

	/**
	 * Initializes parameter and attribute maps from context data structures.
	 * @param facesContext the faces context
	 */
	private void initMaps(FacesContext facesContext) {
		this.requestParameterMap = new LocalParameterMap(facesContext.getExternalContext().getRequestParameterMap());
		this.requestMap = new LocalAttributeMap(facesContext.getExternalContext().getRequestMap());
		this.sessionMap = new LocalSharedAttributeMap(new SessionSharedMap(facesContext));
		this.applicationMap = new LocalSharedAttributeMap(new ApplicationSharedMap(facesContext));
	}

	public String getContextPath() {
		return facesContext.getExternalContext().getRequestContextPath();
	}

	public String getDispatcherPath() {
		return facesContext.getExternalContext().getRequestServletPath();
	}

	public String getRequestPathInfo() {
		return facesContext.getExternalContext().getRequestPathInfo();
	}

	public ParameterMap getRequestParameterMap() {
		return requestParameterMap;
	}

	public MutableAttributeMap getRequestMap() {
		return requestMap;
	}

	public SharedAttributeMap getSessionMap() {
		return sessionMap;
	}

	public SharedAttributeMap getGlobalSessionMap() {
		return getSessionMap();
	}

	public SharedAttributeMap getApplicationMap() {
		return applicationMap;
	}

	/**
	 * Returns the JSF FacesContext.
	 */
	public FacesContext getFacesContext() {
		return facesContext;
	}

	/**
	 * Returns the action identifier.
	 */
	public String getActionId() {
		return actionId;
	}

	/**
	 * Returns the action outcome.
	 */
	public String getOutcome() {
		return outcome;
	}

	/**
	 * Records the action and outcome context information when navigation handling occurs.
	 * @param actionId the from action identifier
	 * @param outcome the action outcome
	 */
	public void handleNavigationCalled(String actionId, String outcome) {
		this.actionId = actionId;
		this.outcome = outcome;
	}

	/**
	 * An accessor of a JSF session map.
	 * @author Keith Donald
	 */
	private static class SessionSharedMap extends SharedMapDecorator {

		private FacesContext facesContext;

		public SessionSharedMap(FacesContext facesContext) {
			super(facesContext.getExternalContext().getSessionMap());
			this.facesContext = facesContext;
		}

		public Object getMutex() {
			return facesContext.getExternalContext().getSession(false);
		}
	}

	/**
	 * An accessor of an JSF application map.
	 * @author Keith Donald
	 */
	private static class ApplicationSharedMap extends SharedMapDecorator {

		private FacesContext facesContext;

		public ApplicationSharedMap(FacesContext facesContext) {
			super(facesContext.getExternalContext().getApplicationMap());
			this.facesContext = facesContext;
		}

		public Object getMutex() {
			return facesContext.getExternalContext().getContext();
		}
	}

	public String toString() {
		return new ToStringCreator(this).append("actionId", actionId).append("outcome", outcome).append("facesContext",
				facesContext).toString();
	}
}