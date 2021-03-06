/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;

import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.FlowExecutionContextHolder;

/**
 * A static utility class for accessing the current flow execution holder.
 * <p>
 * By default, the current flow execution holder is associated with the current thread via the {@link FacesContext}'s
 * {@link ExternalContext#getRequestMap()}.
 * 
 * @author Keith Donald
 * @author Craig McClanahan
 */
public class FlowExecutionHolderUtils {

	/**
	 * Returns the current flow execution holder for the given faces context.
	 * @param context faces context
	 * @return the flow execution holder, or <code>null</code> if none set.
	 */
	public static FlowExecutionHolder getFlowExecutionHolder(FacesContext context) {
		return (FlowExecutionHolder) context.getExternalContext().getRequestMap().get(getFlowExecutionHolderKey());
	}

	/**
	 * Sets the current flow execution holder for the given faces context.
	 * @param holder the flow execution holder
	 * @param context faces context
	 */
	public static void setFlowExecutionHolder(FlowExecutionHolder holder, FacesContext context) {
		context.getExternalContext().getRequestMap().put(getFlowExecutionHolderKey(), holder);
		FlowExecutionContextHolder.setFlowExecutionContext(holder.getFlowExecution());
	}

	/**
	 * Returns true if the flow execution has been restored in the current thread.
	 * @param context the faces context
	 * @return true if restored, false otherwise
	 */
	public static boolean isFlowExecutionRestored(FacesContext context) {
		return getFlowExecutionHolder(context) != null;
	}

	/**
	 * Returns the current flow execution in the given faces context.
	 * @param context faces context
	 * @return the flow execution or <code>null</code> if no execution is bound
	 */
	public static FlowExecution getCurrentFlowExecution(FacesContext context) {
		FlowExecutionHolder holder = getFlowExecutionHolder(context);
		if (holder != null) {
			return holder.getFlowExecution();
		} else {
			return null;
		}
	}

	/**
	 * Returns the current required flow execution in the given faces context.
	 * @param context faces context
	 * @return the flow execution
	 * @throws EvaluationException if no flow execution was bound
	 */
	public static FlowExecution getRequiredCurrentFlowExecution(FacesContext context) throws EvaluationException {
		FlowExecution execution = getCurrentFlowExecution(context);
		if (execution != null) {
			return execution;
		} else {
			throw new EvaluationException("No current FlowExecution bound to the Faces Context "
					+ "- was the current flow execution not restored before a view referenced it? "
					+ "Has the flow execution ended or expired?");
		}
	}

	/**
	 * Cleans up the current flow execution in the faces context if necessary. Specifically, handles unlocking the
	 * execution if necessary, setting the holder to null, and cleaning up the flow execution context thread local. Can
	 * be safely called even if no execution is bound or one is bound but not locked.
	 * @param context the faces context
	 */
	public static void cleanupCurrentFlowExecution(FacesContext context) {
		if (isFlowExecutionRestored(context)) {
			FlowExecutionContextHolder.setFlowExecutionContext(null);
			getFlowExecutionHolder(context).unlockFlowExecutionIfNecessary();
			context.getExternalContext().getRequestMap().remove(getFlowExecutionHolderKey());
		}
	}

	/**
	 * Returns the key used to index the flow execution holder in the request attributes.
	 */
	static String getFlowExecutionHolderKey() {
		return FlowExecutionHolder.class.getName();
	}
}