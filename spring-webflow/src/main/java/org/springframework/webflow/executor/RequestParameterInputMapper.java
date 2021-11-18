/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.MappingContext;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;

/**
 * Simple attribute mapper implementation that puts all entries in the request parameter map of a source
 * {@link ExternalContext} into the FlowExecution inputMap. This makes request parameters available to launching flows
 * for input mapping.
 * <p>
 * Used by {@link FlowExecutorImpl} as the default AttributeMapper implementation.
 * 
 * @see ExternalContext#getRequestParameterMap()
 * @see FlowExecutor#launch(String, ExternalContext)
 * 
 * @author Keith Donald
 */
public class RequestParameterInputMapper implements AttributeMapper {
	public void map(Object source, Object target, MappingContext context) {
		ExternalContext externalContext = (ExternalContext) source;
		MutableAttributeMap inputMap = (MutableAttributeMap) target;
		inputMap.putAll(externalContext.getRequestParameterMap().asAttributeMap());
	}
}