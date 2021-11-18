/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context;

import org.springframework.util.Assert;

/**
 * Simple holder class that associates an {@link ExternalContext} instance with the current thread. The ExternalContext
 * will not be inherited by any child threads spawned by the current thread.
 * <p>
 * Used as a central holder for the current ExternalContext in Spring Web Flow, wherever necessary. Often used by
 * artifacts needing access to the current application session.
 * 
 * @see ExternalContext
 * 
 * @author Keith Donald
 */
public final class ExternalContextHolder {

	private static final ThreadLocal externalContextHolder = new ThreadLocal();

	/**
	 * Associate the given ExternalContext with the current thread.
	 * @param externalContext the current ExternalContext, or <code>null</code> to reset the thread-bound context
	 */
	public static void setExternalContext(ExternalContext externalContext) {
		externalContextHolder.set(externalContext);
	}

	/**
	 * Return the ExternalContext associated with the current thread, if any.
	 * @return the current ExternalContext
	 * @throws IllegalStateException if no ExternalContext is bound to this thread
	 */
	public static ExternalContext getExternalContext() {
		Assert.state(externalContextHolder.get() != null, "No external context is bound to this thread");
		return (ExternalContext) externalContextHolder.get();
	}

	// not instantiable
	private ExternalContextHolder() {
	}
}