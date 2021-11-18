/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.support;

import org.springframework.util.Assert;
import org.springframework.webflow.execution.ViewSelection;

/**
 * Concrete response type that requests a redirect to an external URL outside of Spring Web Flow.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public final class ExternalRedirect extends ViewSelection {

	/**
	 * The arbitrary url path to redirect to.
	 */
	private final String url;

	/**
	 * Creates an external redirect request.
	 * @param url the url path to redirect to
	 */
	public ExternalRedirect(String url) {
		Assert.notNull(url, "The external URL to redirect to is required");
		this.url = url;
	}

	/**
	 * Returns the external URL to redirect to.
	 */
	public String getUrl() {
		return url;
	}

	public boolean equals(Object o) {
		if (!(o instanceof ExternalRedirect)) {
			return false;
		}
		ExternalRedirect other = (ExternalRedirect) o;
		return url.equals(other.url);
	}

	public int hashCode() {
		return url.hashCode();
	}

	public String toString() {
		return "externalRedirect:'" + url + "'";
	}
}