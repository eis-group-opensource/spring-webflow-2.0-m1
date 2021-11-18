/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.support;

import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.execution.repository.FlowExecutionKey;
import org.springframework.webflow.executor.FlowExecutor;

/**
 * A helper strategy used by the {@link FlowRequestHandler} to extract {@link FlowExecutor} method arguments from a
 * request initiated by an {@link ExternalContext}. The extracted arguments were typically exposed in the previous
 * response (the response that resulted in a new request) using a {@link FlowExecutorArgumentExposer}.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface FlowExecutorArgumentExtractor {

	/**
	 * Returns true if the flow id is extractable from the context.
	 * @param context the context in which a external user event occured
	 * @return true if extractable, false if not
	 */
	public boolean isFlowIdPresent(ExternalContext context);

	/**
	 * Extracts the flow id from the external context.
	 * @param context the context in which a external user event occured
	 * @return the extracted flow id
	 * @throws FlowExecutorArgumentExtractionException if the flow id could not be extracted
	 */
	public String extractFlowId(ExternalContext context) throws FlowExecutorArgumentExtractionException;

	/**
	 * Returns true if the flow execution key is extractable from the context.
	 * @param context the context in which a external user event occured
	 * @return true if extractable, false if not
	 */
	public boolean isFlowExecutionKeyPresent(ExternalContext context);

	/**
	 * Extract the flow execution key from the external context.
	 * @param context the context in which the external user event occured
	 * @return the obtained flow execution key
	 * @throws FlowExecutorArgumentExtractionException if the flow execution key could not be extracted
	 */
	public String extractFlowExecutionKey(ExternalContext context) throws FlowExecutorArgumentExtractionException;

	/**
	 * Returns true if the event id is extractable from the context.
	 * @param context the context in which a external user event occured
	 * @return true if extractable, false if not
	 */
	public boolean isEventIdPresent(ExternalContext context);

	/**
	 * Extract the flow execution event id from the external context.
	 * <p>
	 * This method should only be called if a {@link FlowExecutionKey} was successfully extracted, indicating a request
	 * to resume a flow execution.
	 * @param context the context in which a external user event occured
	 * @return the event id
	 * @throws FlowExecutorArgumentExtractionException if the event id could not be extracted
	 */
	public String extractEventId(ExternalContext context) throws FlowExecutorArgumentExtractionException;

}