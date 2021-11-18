/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.util.Assert;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.engine.Flow;

/**
 * Abstract base implementation of a flow builder defining common functionality needed by most concrete flow builder
 * implementations. This class implements all optional parts of the FlowBuilder process as no-op methods. Subclasses are
 * only required to implement {@link #init(String, AttributeMap)} and {@link #buildStates()}.
 * <p>
 * This class also provides a {@link FlowServiceLocator} for use by subclasses in the flow construction process.
 * 
 * @see org.springframework.webflow.engine.builder.FlowServiceLocator
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class BaseFlowBuilder implements FlowBuilder {

	/**
	 * The <code>Flow</code> built by this builder.
	 */
	private Flow flow;

	/**
	 * Locates actions, attribute mappers, and other artifacts needed by the flow built by this builder.
	 */
	private FlowServiceLocator flowServiceLocator;

	/**
	 * Default constructor for subclassing. Sets up use of a {@link BaseFlowServiceLocator}.
	 * @see #setFlowServiceLocator(FlowServiceLocator)
	 */
	protected BaseFlowBuilder() {
		setFlowServiceLocator(new BaseFlowServiceLocator());
	}

	/**
	 * Creates a flow builder using the given locator to link in artifacts.
	 * @param flowServiceLocator the locator for services needed by this builder to build its Flow
	 */
	protected BaseFlowBuilder(FlowServiceLocator flowServiceLocator) {
		setFlowServiceLocator(flowServiceLocator);
	}

	/**
	 * Returns the configured flow service locator.
	 */
	public FlowServiceLocator getFlowServiceLocator() {
		return flowServiceLocator;
	}

	/**
	 * Sets the flow service locator to use. Defaults to {@link BaseFlowServiceLocator}.
	 */
	public void setFlowServiceLocator(FlowServiceLocator flowServiceLocator) {
		Assert.notNull(flowServiceLocator, "The flow service locator is required");
		this.flowServiceLocator = flowServiceLocator;
	}

	/**
	 * Set the flow being built by this builder. Typically called during initialization to set the initial flow
	 * reference returned by {@link #getFlow()} after building.
	 */
	protected void setFlow(Flow flow) {
		this.flow = flow;
	}

	public abstract void init(String flowId, AttributeMap attributes) throws FlowBuilderException;

	public void buildVariables() throws FlowBuilderException {
	}

	public void buildInputMapper() throws FlowBuilderException {
	}

	public void buildStartActions() throws FlowBuilderException {
	}

	public void buildInlineFlows() throws FlowBuilderException {
	}

	public abstract void buildStates() throws FlowBuilderException;

	public void buildGlobalTransitions() throws FlowBuilderException {
	}

	public void buildEndActions() throws FlowBuilderException {
	}

	public void buildOutputMapper() throws FlowBuilderException {
	}

	public void buildExceptionHandlers() throws FlowBuilderException {
	}

	/**
	 * Get the flow (result) built by this builder.
	 */
	public Flow getFlow() {
		return flow;
	}

	public void dispose() {
		setFlow(null);
	}

	// helpers for use in subclasses

	/**
	 * Returns a conversion executor capable of converting string objects to the target class aliased by the provided
	 * alias.
	 * @param targetAlias the target class alias, e.g. "long" or "float"
	 * @return the conversion executor, or <code>null</code> if no suitable converter exists for given alias
	 */
	protected ConversionExecutor fromStringTo(String targetAlias) {
		return getFlowServiceLocator().getConversionService().getConversionExecutorByTargetAlias(String.class,
				targetAlias);
	}

	/**
	 * Returns a converter capable of converting a string value to the given type.
	 * @param targetType the type you wish to convert to (from a string)
	 * @return the converter
	 * @throws ConversionException when the converter cannot be found
	 */
	protected ConversionExecutor fromStringTo(Class targetType) throws ConversionException {
		return getFlowServiceLocator().getConversionService().getConversionExecutor(String.class, targetType);
	}
}