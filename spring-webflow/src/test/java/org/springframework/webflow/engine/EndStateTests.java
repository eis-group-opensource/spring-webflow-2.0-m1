/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import junit.framework.TestCase;

import org.springframework.binding.expression.support.StaticExpression;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.MappingBuilder;
import org.springframework.webflow.core.DefaultExpressionParserFactory;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.engine.impl.FlowExecutionImpl;
import org.springframework.webflow.engine.support.ApplicationViewSelector;
import org.springframework.webflow.engine.support.EventIdTransitionCriteria;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.test.MockExternalContext;

/**
 * Tests that each of the Flow state types execute as expected when entered.
 * 
 * @author Keith Donald
 */
public class EndStateTests extends TestCase {

	public void testEndStateTerminateFlow() {
		Flow flow = new Flow("myFlow");
		EndState state = new EndState(flow, "finish");
		state.setViewSelector(view("myViewName"));
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		ApplicationView view = (ApplicationView) flowExecution.start(null, new MockExternalContext());
		assertFalse(flowExecution.isActive());
		assertEquals("myViewName", view.getViewName());
	}

	public void testEndStateTerminateFlowWithOutput() {
		Flow flow = new Flow("myFlow");
		DefaultAttributeMapper inputMapper = new DefaultAttributeMapper();
		MappingBuilder mapping = new MappingBuilder(DefaultExpressionParserFactory.getExpressionParser());
		inputMapper.addMapping(mapping.source("attr1").target("flowScope.attr1").value());
		flow.setInputMapper(inputMapper);

		EndState state = new EndState(flow, "finish");
		DefaultAttributeMapper outputMapper = new DefaultAttributeMapper();
		outputMapper.addMapping(mapping.source("flowScope.attr1").target("attr1").value());
		outputMapper.addMapping(mapping.source("flowScope.attr2").target("attr2").value());
		state.setOutputMapper(outputMapper);

		FlowExecutionListener outputVerifier = new FlowExecutionListenerAdapter() {
			public void sessionEnded(RequestContext context, FlowSession session, AttributeMap output) {
				assertEquals("value1", output.get("attr1"));
				assertNull(output.get("attr2"));
			}
		};
		FlowExecution flowExecution = new FlowExecutionImpl(flow, new FlowExecutionListener[] { outputVerifier }, null);
		LocalAttributeMap input = new LocalAttributeMap();
		input.put("attr1", "value1");
		ViewSelection view = flowExecution.start(input, new MockExternalContext());
		assertFalse(flowExecution.isActive());
		assertEquals(ViewSelection.NULL_VIEW, view);
	}

	protected static TransitionCriteria on(String event) {
		return new EventIdTransitionCriteria(event);
	}

	protected static String to(String stateId) {
		return stateId;
	}

	public static ViewSelector view(String viewName) {
		return new ApplicationViewSelector(new StaticExpression(viewName));
	}
}