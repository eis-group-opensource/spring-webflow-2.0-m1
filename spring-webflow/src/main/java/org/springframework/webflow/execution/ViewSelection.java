/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Abstract base class for value objects that provide callers into a flow execution information about a logical response
 * to issue and the data necessary to issue it.
 * <p>
 * This class is a generic marker returned when a request into an executing flow has completed processing, indicating a
 * client response needs to be issued. An instance of a ViewSelection subclass represents the selection of a concrete
 * response type. It is expected that callers introspect the returned view selection instance to handle the response
 * types they support.
 * 
 * @see FlowExecution
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class ViewSelection implements Serializable {

	/**
	 * Constant for a <code>null</code> or empty view selection, indicating no response should be issued.
	 */
	public static final ViewSelection NULL_VIEW = new NullView();

	/**
	 * The definition of the 'null' view selection type, indicating that no response should be issued.
	 */
	private static final class NullView extends ViewSelection {

		// resolve the singleton instance
		private Object readResolve() throws ObjectStreamException {
			return NULL_VIEW;
		}

		public String toString() {
			return "null";
		}
	}
}