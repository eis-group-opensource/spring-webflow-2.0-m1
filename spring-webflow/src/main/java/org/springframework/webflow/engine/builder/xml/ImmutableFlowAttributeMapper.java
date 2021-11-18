/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import java.io.Serializable;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.engine.support.AbstractFlowAttributeMapper;

/**
 * Simple flow attribute mapper that holds an input and output mapper strategy. This is an internal helper class of the
 * XmlFlowBuilder.
 * 
 * @see org.springframework.webflow.engine.builder.xml.XmlFlowBuilder
 * 
 * @author Keith Donald
 */
final class ImmutableFlowAttributeMapper extends AbstractFlowAttributeMapper implements Serializable {

	private final AttributeMapper inputMapper;

	private final AttributeMapper outputMapper;

	/**
	 * Create a new flow attribute mapper using given mapping strategies.
	 * @param inputMapper the input mapping strategy
	 * @param outputMapper the output mapping strategy
	 */
	public ImmutableFlowAttributeMapper(AttributeMapper inputMapper, AttributeMapper outputMapper) {
		this.inputMapper = inputMapper;
		this.outputMapper = outputMapper;
	}

	protected AttributeMapper getInputMapper() {
		return inputMapper;
	}

	protected AttributeMapper getOutputMapper() {
		return outputMapper;
	}

	public String toString() {
		return new ToStringCreator(this).append("inputMapper", inputMapper).append("outputMapper", outputMapper)
				.toString();
	}
}