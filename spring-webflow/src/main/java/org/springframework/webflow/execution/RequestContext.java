/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.definition.TransitionDefinition;

/**
 * A context for a single request to manipulate a flow execution. Allows Web Flow users to access contextual information
 * about the executing request, as well as the governing {@link #getFlowExecutionContext() active flow execution}.
 * <p>
 * The term <i>request</i> is used to describe a single call (thread) into the flow system by an external actor to
 * manipulate exactly one flow execution.
 * <p>
 * A new instance of this object is typically created when one of the core operations supported by a flow execution is
 * invoked, either <code>start</code> to launch the flow execution, <code>signalEvent</code> to resume the flow
 * execution, or <code>refresh</code> to reconstitute the flow execution's last view selection for purposes of
 * reissuing a user response.
 * <p>
 * Once created this context object is passed around throughout flow execution request processing where it may be
 * accessed and reasoned upon by SWF-internal artifacts such as states, user-implemented action code, and state
 * transition criteria.
 * <p>
 * When a call into a flow execution returns this object goes out of scope and is disposed of automatically. Thus a
 * request context is an internal artifact used within a FlowExecution: this object is not exposed to external client
 * code, e.g. a view implementation (JSP).
 * <p>
 * The {@link #getRequestScope() requestScope} property may be used as a store for arbitrary data that should exist for
 * the life of this object. Request-scoped data, along with all data in {@link #getFlashScope() flash scope},
 * {@link #getFlowScope() flow scope} and {@link #getConversationScope() conversation scope} is available for exposing
 * to view templates via a {@link #getModel() model} property.
 * <p>
 * The web flow system will ensure that a RequestContext object is local to the current thread. It can be safely
 * manipulated without needing to worry about concurrent access.
 * <p>
 * Note: this request context is in no way linked to an HTTP or Portlet request. It uses the familiar "request" naming
 * convention to indicate a single call to manipulate a runtime execution of a flow definition.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface RequestContext {

	/**
	 * Returns the definition of the flow that is currently executing.
	 * @return the flow definition for the active session
	 * @throws IllegalStateException if the flow execution has not been started at all, or if the execution has ended
	 * and is no longer actively executing
	 */
	public FlowDefinition getActiveFlow() throws IllegalStateException;

	/**
	 * Returns the current state of the executing flow. May return <code>null</code> if this flow execution is in the
	 * process of starting and has not yet entered its start state.
	 * @return the current state, or <code>null</code> if in the process of starting
	 * @throws IllegalStateException if this flow execution has not been started at all, or if this execution has ended
	 * and is no longer actively executing
	 */
	public StateDefinition getCurrentState() throws IllegalStateException;

	/**
	 * Returns a mutable accessor for accessing and/or setting attributes in request scope. <b>Request scoped attributes
	 * exist for the duration of this request only.</b>
	 * @return the request scope
	 */
	public MutableAttributeMap getRequestScope();

	/**
	 * Returns a mutable accessor for accessing and/or setting attributes in flash scope. <b>Flash scoped attributes
	 * exist untill the next event is signaled in the flow execution.</b>
	 * @return the flash scope
	 */
	public MutableAttributeMap getFlashScope();

	/**
	 * Returns a mutable accessor for accessing and/or setting attributes in flow scope. <b>Flow scoped attributes exist
	 * for the life of the active flow session.</b>
	 * @return the flow scope
	 * @see FlowSession
	 */
	public MutableAttributeMap getFlowScope();

	/**
	 * Returns a mutable accessor for accessing and/or setting attributes in conversation scope. <b>Conversation scoped
	 * attributes exist for the life of the executing flow and are shared across all flow sessions.</b>
	 * @return the conversation scope
	 * @see FlowExecutionContext
	 */
	public MutableAttributeMap getConversationScope();

	/**
	 * Returns the immutable input parameters associated with this request into Spring Web Flow. The map returned is
	 * immutable and cannot be changed.
	 * <p>
	 * This is typically a convenient shortcut for accessing the {@link ExternalContext#getRequestParameterMap()}
	 * directly.
	 * @see #getExternalContext()
	 */
	public ParameterMap getRequestParameters();

	/**
	 * Returns the external client context that originated (or triggered) this request.
	 * <p>
	 * Acting as a facade, the returned context object provides a single point of access to the calling client's
	 * environment. It provides normalized access to attributes of the client environment without tying you to specific
	 * constructs within that environment.
	 * <p>
	 * In addition, this context may be downcastable to a specific context type for a specific client environment, such
	 * as a {@link org.springframework.webflow.context.servlet.ServletExternalContext} for servlets or a
	 * {@link org.springframework.webflow.context.portlet.PortletExternalContext} for portlets. Such downcasting will
	 * give you full access to a native HttpServletRequest, for example. With that said, for portability reasons you
	 * should avoid coupling your flow artifacts to a specific deployment environment when possible.
	 * @return the originating external context, the one that triggered the current execution request
	 */
	public ExternalContext getExternalContext();

	/**
	 * Returns contextual information about the flow execution itself. Information in this context typically spans more
	 * than one request.
	 * @return the flow execution context
	 */
	public FlowExecutionContext getFlowExecutionContext();

	/**
	 * Returns the last event signaled during this request. The event may or may not have caused a state transition to
	 * happen.
	 * @return the last signaled event, or null if no event has been signaled yet
	 */
	public Event getLastEvent();

	/**
	 * Returns the last state transition that executed in this request.
	 * @return the last transition, or <code>null</code> if no transition has occured yet
	 */
	public TransitionDefinition getLastTransition();

	/**
	 * Returns a context map for accessing arbitrary attributes about the state of the current request. These attributes
	 * may be used to influence flow execution behavior.
	 * @return the current attributes of this request, or empty if not set
	 */
	public AttributeMap getAttributes();

	/**
	 * Set the contextual attributes describing the state of this request. Overwrites any pre-existing collection.
	 * @param attributes the attributes
	 */
	public void setAttributes(AttributeMap attributes);

	/**
	 * Returns the data model capturing the state of this context, suitable for exposing to clients (mostly web views).
	 * Typically the model will contain the union of the data available in request, flash, session and conversation
	 * scope.
	 * @return the model that can be exposed to a client view for rendering purposes
	 */
	public AttributeMap getModel();

}