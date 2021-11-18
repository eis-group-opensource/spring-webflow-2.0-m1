/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.registry.FlowDefinitionConstructionException;
import org.springframework.webflow.definition.registry.FlowDefinitionHolder;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.util.ResourceHolder;

/**
 * A flow definition holder that can detect changes on an underlying flow definition resource and refresh that resource
 * automatically.
 * <p>
 * This class is thread-safe.
 * <p>
 * Note that this {@link FlowDefinition} holder uses a {@link Flow} assembler. This is normal since a {@link Flow} is a
 * {@link FlowDefinition}! This class bridges the <i>abstract</i> world of {@link FlowDefinition flow definitions}
 * with the <i>concrete</i> world of {@link Flow flow implementations}.
 * 
 * @see FlowDefinition
 * @see Flow
 * @see FlowAssembler
 * 
 * @author Keith Donald
 */
public class RefreshableFlowDefinitionHolder implements FlowDefinitionHolder {

	private static final Log logger = LogFactory.getLog(RefreshableFlowDefinitionHolder.class);

	/**
	 * The flow definition assembled by this assembler.
	 */
	private FlowDefinition flowDefinition;

	/**
	 * The flow assembler.
	 */
	private FlowAssembler assembler;

	/**
	 * A last modified date for the backing flow definition resource, used to support automatic reassembly on resource
	 * change.
	 */
	private long lastModified;

	/**
	 * A flag indicating whether or not this holder is in the middle of the assembly process.
	 */
	private boolean assembling;

	/**
	 * Creates a new refreshable flow definition holder that uses the configured assembler (GOF director) to drive flow
	 * assembly, on initial use and on any resource change or refresh.
	 * @param assembler the flow assembler to use
	 */
	public RefreshableFlowDefinitionHolder(FlowAssembler assembler) {
		this.assembler = assembler;
	}

	public String getFlowDefinitionId() {
		return assembler.getFlowId();
	}

	public synchronized FlowDefinition getFlowDefinition() throws FlowDefinitionConstructionException {
		if (assembling) {
			// must return early assembly result
			return getFlowBuilder().getFlow();
		}
		if (!isAssembled()) {
			lastModified = calculateLastModified();
			assembleFlow();
		} else {
			refreshIfChanged();
		}
		return flowDefinition;
	}

	public synchronized void refresh() throws FlowBuilderException {
		assembleFlow();
	}

	// internal helpers

	/**
	 * Returns the flow builder that actually builds the Flow definition.
	 */
	protected FlowBuilder getFlowBuilder() {
		return assembler.getFlowBuilder();
	}

	/**
	 * Reassemble the flow if its underlying resource has changed.
	 */
	protected void refreshIfChanged() {
		if (this.lastModified == -1) {
			// just ignore, tracking last modified date not supported
			return;
		}
		long calculatedLastModified = calculateLastModified();
		if (this.lastModified < calculatedLastModified) {
			if (logger.isDebugEnabled()) {
				logger.debug("Resource modification detected, reloading flow definition with id '"
						+ assembler.getFlowId() + "'");
			}
			assembleFlow();
			this.lastModified = calculatedLastModified;
		}
	}

	/**
	 * Helper that retrieves the last modified date by querying the backing flow resource.
	 * @return the last modified date, or -1 if it could not be retrieved
	 */
	protected long calculateLastModified() {
		if (getFlowBuilder() instanceof ResourceHolder) {
			Resource resource = ((ResourceHolder) getFlowBuilder()).getResource();
			if (logger.isDebugEnabled()) {
				logger.debug("Calculating last modified timestamp for flow definition resource '" + resource + "'");
			}
			try {
				return resource.getFile().lastModified();
			} catch (IOException e) {
				// ignore, last modified checks not supported
			}
		}
		return -1;
	}

	/**
	 * Returns the last modified date of the backed flow definition resource.
	 * @return the last modified date
	 */
	protected long getLastModified() {
		return lastModified;
	}

	/**
	 * Assemble the held flow definition, delegating to the configured FlowAssembler (director).
	 */
	protected void assembleFlow() throws FlowBuilderException {
		if (logger.isDebugEnabled()) {
			logger.debug("Assembling flow definition with id '" + assembler.getFlowId() + "'");
		}
		try {
			assembling = true;
			flowDefinition = assembler.assembleFlow();
		} finally {
			assembling = false;
		}
	}

	/**
	 * Returns a flag indicating if this holder has performed and completed flow definition assembly.
	 */
	protected boolean isAssembled() {
		return flowDefinition != null;
	}

	/**
	 * Returns a flag indicating if this holder is performing assembly.
	 */
	protected boolean isAssembling() {
		return assembling;
	}
}