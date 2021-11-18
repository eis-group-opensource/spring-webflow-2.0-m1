/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

/**
 * Mock implementation of the <code>FacesContext</code> class to facilitate standalone Action unit tests.
 * <p>
 * NOT intended to be used for anything but standalone unit tests. This is a simple state holder, a <i>stub</i>
 * implementation, at least if you follow <a href="http://www.martinfowler.com/articles/mocksArentStubs.html">Martin
 * Fowler's</a> reasoning. This class is called <i>Mock</i>FacesContext to be consistent with the naming convention in
 * the rest of the Spring framework (e.g. MockHttpServletRequest, ...).
 * 
 * @see javax.faces.context.FacesContext
 * 
 * @author Ulrik Sandberg
 */
public class MockFacesContext extends FacesContext {
	private ExternalContext externalContext;

	private Application application;

	private UIViewRoot viewRoot;

	public Application getApplication() {
		return application;
	}

	/**
	 * Set the application to be used by this faces context.
	 * @param application the applicaiton to set.
	 */
	public void setApplication(Application application) {
		this.application = application;
	}

	public Iterator getClientIdsWithMessages() {
		return null;
	}

	public ExternalContext getExternalContext() {
		return externalContext;
	}

	/**
	 * Set the external context of this faces context.
	 * @param externalContext the external context to set.
	 */
	public void setExternalContext(ExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	public Severity getMaximumSeverity() {
		return null;
	}

	public Iterator getMessages() {
		return null;
	}

	public Iterator getMessages(String arg0) {
		return null;
	}

	public RenderKit getRenderKit() {
		return null;
	}

	public boolean getRenderResponse() {
		return false;
	}

	public boolean getResponseComplete() {
		return false;
	}

	public ResponseStream getResponseStream() {
		return null;
	}

	public void setResponseStream(ResponseStream arg0) {

	}

	public ResponseWriter getResponseWriter() {
		return null;
	}

	public void setResponseWriter(ResponseWriter arg0) {
	}

	public UIViewRoot getViewRoot() {
		return viewRoot;
	}

	public void setViewRoot(UIViewRoot viewRoot) {
		this.viewRoot = viewRoot;
	}

	public void addMessage(String arg0, FacesMessage arg1) {
	}

	public void release() {
	}

	public void renderResponse() {
	}

	public void responseComplete() {
	}
}