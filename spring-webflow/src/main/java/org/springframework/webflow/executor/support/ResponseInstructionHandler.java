/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.support;

import org.springframework.core.NestedRuntimeException;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.execution.support.ExternalRedirect;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;
import org.springframework.webflow.execution.support.FlowExecutionRedirect;
import org.springframework.webflow.executor.ResponseInstruction;

/**
 * Abstract helper class that allows easy handling of all known view selection types. Users need to implement each of
 * the hook methods dealing with a particular type of view selection, typically in an anonymous inner subclass of this
 * class.
 * 
 * @see ViewSelection
 * 
 * @since 1.0.2
 * 
 * @author Erwin Vervaet
 */
public abstract class ResponseInstructionHandler {

	private Object result;

	/**
	 * Set the object resulting from response handling. This is optional.
	 * @param result the result object
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * Returns the object resulting from response handling. This is optional and will only be set if the subclass calls
	 * {@link #setResult(Object)} to set the result object.
	 * @return the result object, or null if none
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * Issue a response for given response instruction. Will delegate to any of the available hook methods depending on
	 * the type of view selection contained in the response instruction.
	 * @param responseInstruction the response instruction to issue a response for
	 * @return this object, for call chaining
	 * @throws Exception when an error occured
	 */
	public final ResponseInstructionHandler handle(ResponseInstruction responseInstruction) throws Exception {
		if (responseInstruction.isApplicationView()) {
			handleApplicationView((ApplicationView) responseInstruction.getViewSelection());
		} else if (responseInstruction.isFlowDefinitionRedirect()) {
			handleFlowDefinitionRedirect((FlowDefinitionRedirect) responseInstruction.getViewSelection());
		} else if (responseInstruction.isFlowExecutionRedirect()) {
			handleFlowExecutionRedirect((FlowExecutionRedirect) responseInstruction.getViewSelection());
		} else if (responseInstruction.isExternalRedirect()) {
			handleExternalRedirect((ExternalRedirect) responseInstruction.getViewSelection());
		} else if (responseInstruction.isNull()) {
			handleNull();
		} else {
			throw new IllegalArgumentException("Don't know how to handle response instruction " + responseInstruction);
		}
		return this;
	}

	/**
	 * Quietly issue a response for given response instruction, turning any Exception raised while handling the response
	 * instruction into a RuntimeException. Will delegate to any of the available hook methods depending on the type of
	 * view selection contained in the response instruction.
	 * @param responseInstruction the response instruction to issue a response for
	 * @return this object, for call chaining
	 */
	public final ResponseInstructionHandler handleQuietly(ResponseInstruction responseInstruction) {
		try {
			return handle(responseInstruction);
		} catch (Exception e) {
			throw new RuntimeResponseHandlingException("Unexpected exception handling response instruction "
					+ responseInstruction + "", e);
		}
	}

	// template methods

	/**
	 * Issue a response for given application view.
	 * @param view the application view to issue a response for
	 * @throws Exception when an error occured
	 * @see ResponseInstruction#isActiveView()
	 * @see ApplicationView
	 */
	protected abstract void handleApplicationView(ApplicationView view) throws Exception;

	/**
	 * Issue a response for given flow definition redirect.
	 * @param redirect the flow definition redirect to issue a response for
	 * @throws Exception when an error occured
	 * @see ResponseInstruction#isFlowDefinitionRedirect()
	 * @see FlowDefinitionRedirect
	 */
	protected abstract void handleFlowDefinitionRedirect(FlowDefinitionRedirect redirect) throws Exception;

	/**
	 * Issue a response for given flow execution redirect.
	 * @param redirect the flow execution redirect to issue a response for
	 * @throws Exception when an error occured
	 * @see ResponseInstruction#isFlowExecutionRedirect()
	 * @see FlowExecutionRedirect
	 */
	protected abstract void handleFlowExecutionRedirect(FlowExecutionRedirect redirect) throws Exception;

	/**
	 * Issue a response for given external redirect.
	 * @param redirect the external redirect to issue a response for
	 * @throws Exception when an error occured
	 * @see ResponseInstruction#isExternalRedirect()
	 * @see ExternalRedirect
	 */
	protected abstract void handleExternalRedirect(ExternalRedirect redirect) throws Exception;

	/**
	 * Issue a respone for the null view selection.
	 * @throws Exception
	 * @see ResponseInstruction#isNull()
	 * @see ViewSelection#NULL_VIEW
	 */
	protected abstract void handleNull() throws Exception;

	/**
	 * Thrown during handleQuietly.
	 * @author Keith Donald
	 */
	public static class RuntimeResponseHandlingException extends NestedRuntimeException {
		public RuntimeResponseHandlingException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}