/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import java.io.Serializable;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.MappingContext;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.engine.FlowAttributeMapper;
import org.springframework.webflow.execution.RequestContext;

/**
 * Convenient base class for attribute mapper implementations. Encapsulates common attribute mapper workflow. Contains
 * no state. Subclasses must override the {@link #getInputMapper()} and {@link #getOutputMapper()} methods to return the
 * input mapper and output mapper, respectively.
 * 
 * @author Keith Donald
 */
public abstract class AbstractFlowAttributeMapper implements FlowAttributeMapper, Serializable {

	/**
	 * Returns the input mapper to use to map attributes of a parent flow {@link RequestContext} to a subflow input
	 * attribute {@link AttributeMap map}.
	 * @return the input mapper, or null if none
	 * @see #createFlowInput(RequestContext)
	 */
	protected abstract AttributeMapper getInputMapper();

	/**
	 * Returns the output mapper to use to map attributes from a subflow output attribute map to the
	 * {@link RequestContext}.
	 * @return the output mapper, or null if none
	 * @see #mapFlowOutput(AttributeMap, RequestContext)
	 */
	protected abstract AttributeMapper getOutputMapper();

	public MutableAttributeMap createFlowInput(RequestContext context) {
		if (getInputMapper() != null) {
			LocalAttributeMap input = new LocalAttributeMap();
			// map from request context to input map
			getInputMapper().map(context, input, getMappingContext(context));
			return input;
		} else {
			// an empty, but modifiable map
			return new LocalAttributeMap();
		}
	}

	public void mapFlowOutput(AttributeMap subflowOutput, RequestContext context) {
		if (getOutputMapper() != null && subflowOutput != null) {
			// map from subflow output map to request context
			getOutputMapper().map(subflowOutput, context, getMappingContext(context));
		}
	}

	/**
	 * Returns a map of contextual data available during mapping. This implementation just returns null.
	 */
	protected MappingContext getMappingContext(RequestContext context) {
		return null;
	}
}