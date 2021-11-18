/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.MappingContext;
import org.springframework.util.Assert;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Action that executes an attribute mapper to map information in the request context. Both the source and the target of
 * the mapping will be the request context. This allows for maximum flexibility when defining attribute mapping
 * expressions (e.g. "${flowScope.someAttribute}").
 * <p>
 * This action always returns the {@link org.springframework.webflow.action.AbstractAction#success() success} event. If
 * something goes wrong while executing the mapping, an exception is thrown.
 * 
 * @see org.springframework.binding.mapping.AttributeMapper
 * @see org.springframework.webflow.execution.RequestContext
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class AttributeMapperAction extends AbstractAction {

	/**
	 * The attribute mapper strategy to delegate to perform the mapping.
	 */
	private AttributeMapper attributeMapper;

	/**
	 * Creates a new attribute mapper action that delegates to the configured attribute mapper to complete the mapping
	 * process.
	 * @param attributeMapper the mapper
	 */
	public AttributeMapperAction(AttributeMapper attributeMapper) {
		Assert.notNull(attributeMapper, "The attribute mapper is required");
		this.attributeMapper = attributeMapper;
	}

	protected Event doExecute(RequestContext context) throws Exception {
		// map attributes from and to the request context
		attributeMapper.map(context, context, getMappingContext(context));
		return success();
	}

	/**
	 * Returns a context containing extra data available during attribute mapping. The default implementation just
	 * returns null. Subclasses can override this if necessary.
	 */
	protected MappingContext getMappingContext(RequestContext context) {
		return null;
	}
}