/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.State;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.FlowSessionStatus;

/**
 * Implementation of the FlowSession interfaced used internally by the <code>FlowExecutionImpl</code>. This class is
 * closely coupled with <code>FlowExecutionImpl</code> and <code>RequestControlContextImpl</code>. The three
 * classes work together to form a complete flow execution implementation.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 * @author Ben Hale
 */
class FlowSessionImpl implements FlowSession, Externalizable {

	/**
	 * The flow definition (a singleton).
	 * <p>
	 * Transient to support restoration by the {@link FlowExecutionImplStateRestorer}.
	 */
	private transient Flow flow;

	/**
	 * Set so the transient {@link #flow} field can be restored by the {@link FlowExecutionImplStateRestorer}.
	 */
	private String flowId;

	/**
	 * The current state of this flow session.
	 * <p>
	 * Transient to support restoration by the {@link FlowExecutionImplStateRestorer}.
	 */
	private transient State state;

	/**
	 * Set so the transient {@link #state} field can be restored by the {@link FlowExecutionImplStateRestorer}.
	 */
	private String stateId;

	/**
	 * The session status; may be CREATED, STARTING, ACTIVE, PAUSED, SUSPENDED, or ENDED.
	 */
	private FlowSessionStatus status = FlowSessionStatus.CREATED;

	/**
	 * The session data model ("flow scope").
	 */
	private MutableAttributeMap scope = new LocalAttributeMap();

	/**
	 * The parent session of this session (may be <code>null</code> if this is a root session.)
	 */
	private FlowSessionImpl parent;

	/**
	 * Default constructor required for externalizable serialization. Should NOT be called programmatically.
	 */
	public FlowSessionImpl() {
	}

	/**
	 * Create a new flow session.
	 * @param flow the flow definition associated with this flow session
	 * @param parent this session's parent (may be null)
	 */
	public FlowSessionImpl(Flow flow, FlowSessionImpl parent) {
		setFlow(flow);
		this.parent = parent;
	}

	// implementing FlowSession

	public FlowDefinition getDefinition() {
		return flow;
	}

	public StateDefinition getState() {
		return state;
	}

	public FlowSessionStatus getStatus() {
		return status;
	}

	public MutableAttributeMap getScope() {
		return scope;
	}

	public FlowSession getParent() {
		return parent;
	}

	public boolean isRoot() {
		return parent == null;
	}

	// custom serialization

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		flowId = (String) in.readObject();
		stateId = (String) in.readObject();
		status = (FlowSessionStatus) in.readObject();
		scope = (MutableAttributeMap) in.readObject();
		parent = (FlowSessionImpl) in.readObject();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(flowId);
		out.writeObject(stateId);
		out.writeObject(status);
		out.writeObject(scope);
		out.writeObject(parent);
	}

	// package private setters for setting/updating internal state
	// used by FlowExecutionImplStateRestorer

	/**
	 * Restores the definition of this flow session.
	 * @param flow the flow sessions definition
	 * @see FlowExecutionImplStateRestorer
	 */
	void setFlow(Flow flow) {
		Assert.notNull(flow, "The flow is required");
		this.flow = flow;
		this.flowId = flow.getId();
	}

	/**
	 * Set the current state of this flow session.
	 * @param state the state that is currently active in this flow session
	 * @see FlowExecutionImpl#setCurrentState(State)
	 * @see FlowExecutionImplStateRestorer
	 */
	void setState(State state) {
		Assert.notNull(state, "The state is required");
		Assert.isTrue(flow == state.getOwner(),
				"The state does not belong to the flow associated with this flow session");
		this.state = state;
		this.stateId = state.getId();
	}

	/**
	 * Set the status of this flow session.
	 * @param status the new status to set
	 */
	void setStatus(FlowSessionStatus status) {
		Assert.notNull(status, "The flow session status is requred");
		this.status = status;
	}

	/**
	 * Returns the id of the flow of this session.
	 */
	String getFlowId() {
		return flowId;
	}

	/**
	 * Returns the id of the current state of this session.
	 */
	String getStateId() {
		return stateId;
	}

	public String toString() {
		return new ToStringCreator(this).append("flow", flowId).append("state", stateId).append("scope", scope).append(
				"status", status).toString();
	}
}