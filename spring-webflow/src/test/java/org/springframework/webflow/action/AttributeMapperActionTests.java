/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import junit.framework.TestCase;

import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.MappingBuilder;
import org.springframework.webflow.core.DefaultExpressionParserFactory;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit test for the {@link AttributeMapperAction}.
 * 
 * @author Erwin Vervaet
 */
public class AttributeMapperActionTests extends TestCase {

	private MappingBuilder mappingBuilder = new MappingBuilder(DefaultExpressionParserFactory.getExpressionParser());

	public void testMapping() throws Exception {
		DefaultAttributeMapper mapper = new DefaultAttributeMapper();
		mapper.addMapping(mappingBuilder.source("${externalContext.requestParameterMap.foo}")
				.target("${flowScope.bar}").value());
		AttributeMapperAction action = new AttributeMapperAction(mapper);

		MockRequestContext context = new MockRequestContext();
		context.putRequestParameter("foo", "value");

		assertTrue(context.getFlowScope().size() == 0);

		action.execute(context);

		assertEquals(1, context.getFlowScope().size());
		assertEquals("value", context.getFlowScope().get("bar"));
	}

	public void testNullIllegalArgument() {
		try {
			new AttributeMapperAction(null);
			fail("Should've thrown illegal argument");
		} catch (IllegalArgumentException e) {

		}
	}
}