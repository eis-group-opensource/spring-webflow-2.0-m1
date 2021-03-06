/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import junit.framework.TestCase;

import org.springframework.binding.mapping.Mapping;
import org.springframework.binding.mapping.MappingBuilder;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.core.collection.CollectionUtils;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.ScopeType;
import org.springframework.webflow.test.MockFlowSession;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Test case for {@link ConfigurableFlowAttributeMapper}.
 * 
 * @author Erwin Vervaet
 */
public class ConfigurableFlowAttributeMapperTests extends TestCase {

	private ConfigurableFlowAttributeMapper mapper;

	private MockRequestContext context;

	private MockFlowSession parentSession;

	private MockFlowSession subflowSession;

	private MappingBuilder mapping;

	protected void setUp() throws Exception {
		mapper = new ConfigurableFlowAttributeMapper();
		mapping = new MappingBuilder(mapper.getExpressionParser());
		context = new MockRequestContext();
		parentSession = new MockFlowSession();
		subflowSession = new MockFlowSession();
		subflowSession.setParent(parentSession);
	}

	public void testAttributeMapping() {
		mapper.addInputAttribute("x");
		mapper.addOutputAttribute("y");

		context.setActiveSession(parentSession);
		context.getFlowScope().put("x", "xValue");
		MutableAttributeMap input = mapper.createFlowInput(context);
		assertEquals(1, input.size());
		assertEquals("xValue", input.get("x"));

		parentSession.getScope().clear();

		MutableAttributeMap subflowOutput = new LocalAttributeMap();
		subflowOutput.put("y", "yValue");
		mapper.mapFlowOutput(subflowOutput, context);
		assertEquals(1, parentSession.getScope().size());
		assertEquals("yValue", parentSession.getScope().get("y"));
	}

	public void testDirectMapping() {
		mapper.addInputMapping(mapping.source("${flowScope.x}").target("${y}").value());
		mapper.addOutputMapping(mapping.source("y").target("flowScope.y").value());

		context.setActiveSession(parentSession);
		context.getFlowScope().put("x", "xValue");
		MutableAttributeMap input = mapper.createFlowInput(context);
		assertEquals(1, input.size());
		assertEquals("xValue", input.get("y"));

		parentSession.getScope().clear();

		MutableAttributeMap subflowOutput = new LocalAttributeMap();
		subflowOutput.put("y", "xValue");
		mapper.mapFlowOutput(subflowOutput, context);
		assertEquals(1, parentSession.getScope().size());
		assertEquals("xValue", parentSession.getScope().get("y"));
	}

	public void testBeanPropertyMapping() {
		mapper.addInputMappings(new Mapping[] { mapping.source("flowScope.bean.prop").target("attr").value(),
				mapping.source("flowScope.bean").target("otherBean").value(),
				mapping.source("flowScope.otherAttr").target("otherBean.prop ").value() });
		mapper.addOutputMappings(new Mapping[] { mapping.source("bean.prop").target("flowScope.attr").value(),
				mapping.source("bean").target("flowScope.otherBean").value(),
				mapping.source("otherAttr").target("flowScope.otherBean.prop").value() });

		TestBean bean = new TestBean();
		bean.setProp("value");

		context.setActiveSession(parentSession);
		context.getFlowScope().put("bean", bean);
		context.getFlowScope().put("otherAttr", "otherValue");
		MutableAttributeMap input = mapper.createFlowInput(context);
		assertEquals(2, input.size());
		assertEquals("value", input.get("attr"));
		assertEquals("otherValue", ((TestBean) input.get("otherBean")).getProp());

		parentSession.getScope().clear();
		bean.setProp("value");

		MutableAttributeMap subflowOutput = new LocalAttributeMap();
		subflowOutput.put("bean", bean);
		subflowOutput.put("otherAttr", "otherValue");
		mapper.mapFlowOutput(subflowOutput, context);
		assertEquals(2, parentSession.getScope().size());
		assertEquals("value", parentSession.getScope().get("attr"));
		assertEquals("otherValue", ((TestBean) parentSession.getScope().get("otherBean")).getProp());
	}

	public void testExpressionMapping() {
		mapper.addInputMappings(new Mapping[] { mapping.source("${requestScope.a}").target("b").value(),
				mapping.source("${flowScope.x}").target("y").value() });
		mapper.addOutputMappings(new Mapping[] { mapping.source("b").target("flowScope.c").value(),
				mapping.source("y").target("flowScope.z").value() });

		context.setActiveSession(parentSession);
		context.getRequestScope().put("a", "aValue");
		context.getFlowScope().put("x", "xValue");
		MutableAttributeMap input = mapper.createFlowInput(context);
		assertEquals(2, input.size());
		assertEquals("aValue", input.get("b"));
		assertEquals("xValue", input.get("y"));

		parentSession.getScope().clear();

		MutableAttributeMap subflowOutput = new LocalAttributeMap();
		subflowOutput.put("b", "aValue");
		subflowOutput.put("y", "xValue");
		mapper.mapFlowOutput(subflowOutput, context);
		assertEquals(2, parentSession.getScope().size());
		assertEquals("aValue", parentSession.getScope().get("c"));
		assertEquals("xValue", parentSession.getScope().get("z"));
	}

	public void testNullMapping() {
		mapper.addInputMappings(new Mapping[] { mapping.source("${flowScope.x}").target("y").value(),
				mapping.source("${flowScope.a}").target("b").value() });
		mapper.addOutputMappings(new Mapping[] { mapping.source("y").target("flowScope.c").value(),
				mapping.source("b").target("flowScope.z").value() });

		parentSession.getScope().put("x", null);

		context.setActiveSession(parentSession);
		MutableAttributeMap input = mapper.createFlowInput(context);
		assertEquals(0, input.size());
		assertFalse(input.contains("y"));
		assertFalse(input.contains("b"));

		parentSession.getScope().clear();

		mapper.mapFlowOutput(CollectionUtils.EMPTY_ATTRIBUTE_MAP, context);
		assertEquals(0, parentSession.getScope().size());
		assertFalse(parentSession.getScope().contains("c"));
		assertFalse(parentSession.getScope().contains("z"));
	}

	public void testFormActionInCombinationWithMapping() throws Exception {
		context.setLastEvent(new Event(this, "start"));

		context.setActiveSession(parentSession);
		assertTrue(context.getFlowScope().size() == 0);

		FormAction action = new FormAction();
		action.setFormObjectName("command");
		action.setFormObjectClass(TestBean.class);
		action.setFormObjectScope(ScopeType.FLOW);
		action.setFormErrorsScope(ScopeType.FLOW);
		context.setAttribute("method", "setupForm");

		action.execute(context);

		assertEquals(4, context.getFlowScope().size());
		assertNotNull(context.getFlowScope().get("command"));

		mapper.addInputMapping(mapping.source("${flowScope.command}").target("command").value());
		MutableAttributeMap input = mapper.createFlowInput(context);

		assertEquals(1, input.size());
		assertSame(parentSession.getScope().get("command"), input.get("command"));
		assertTrue(subflowSession.getScope().size() == 0);
		subflowSession.getScope().replaceWith(input);

		context.setActiveSession(subflowSession);
		assertEquals(1, context.getFlowScope().size());

		action.execute(context);

		assertEquals(4, context.getFlowScope().size());
		assertSame(parentSession.getScope().get("command"), context.getFlowScope().get("command"));
	}

	public static class TestBean {
		private String prop;

		public String getProp() {
			return prop;
		}

		public void setProp(String prop) {
			this.prop = prop;
		}
	}
}