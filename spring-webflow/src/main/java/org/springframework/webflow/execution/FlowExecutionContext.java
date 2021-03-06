/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;

/**
 * Provides contextual information about a flow execution. A flow execution is an runnable instance of a
 * {@link FlowDefinition}. In other words, it is the central Spring Web Flow construct for carrying out a conversation
 * with a client. This immutable interface provides access to runtime information about the conversation, such as it's
 * {@link #isActive() status} and {@link #getActiveSession() current state}.
 * <p>
 * An object implementing this interface is also traversable from a execution request context (see
 * {@link org.springframework.webflow.execution.RequestContext#getFlowExecutionContext()}).
 * <p>
 * This interface provides information that may span more than one request in a thread safe manner. The
 * {@link RequestContext} interface defines a <i>request specific</i> control interface for manipulating exactly one
 * flow execution locally from exactly one request.
 * 
 * @see FlowDefinition
 * @see FlowSession
 * @see RequestContext
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface FlowExecutionContext {

	/**
	 * Returns the root flow definition associated with this executing flow.
	 * <p>
	 * A call to this method always returns the same flow definition -- the top-level "root" -- no matter what flow may
	 * actually be active (for example, if subflows have been spawned).
	 * @return the root flow definition
	 */
	public FlowDefinition getDefinition();

	/**
	 * Is the flow execution active?
	 * <p>
	 * All methods on an active flow execution context can be called successfully. If the flow execution is not active,
	 * a caller cannot access some methods such as {@link #getActiveSession()}.
	 * @return true if active, false if the flow execution has terminated
	 */
	public boolean isActive();

	/**
	 * Returns the active flow session of this flow execution. The active flow session is the currently executing
	 * session -- it may be the "root flow" session, or it may be a subflow session if this flow execution has spawned a
	 * subflow.
	 * @return the active flow session
	 * @throws IllegalStateException if this flow execution has not been started at all or if this execution has ended
	 * and is no longer actively executing
	 */
	public FlowSession getActiveSession() throws IllegalStateException;

	/**
	 * Returns a mutable map for data held in "flash scope". Attributes in this map are cleared out on the next event
	 * signaled against this flow execution. Flash attributes survive flow execution refresh operations.
	 * @return flash scope
	 */
	public MutableAttributeMap getFlashScope();

	/**
	 * Returns a mutable map for data held in "conversation scope". Conversation scope is a data structure that exists
	 * for the life of this flow execution and is accessible to all flow sessions.
	 * @return conversation scope
	 */
	public MutableAttributeMap getConversationScope();

	/**
	 * Returns runtime execution attributes that may influence the behavior of flow artifacts, such as states and
	 * actions.
	 * @return execution attributes
	 */
	public AttributeMap getAttributes();
}