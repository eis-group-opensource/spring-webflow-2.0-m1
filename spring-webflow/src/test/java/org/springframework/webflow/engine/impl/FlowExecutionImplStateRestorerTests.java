/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.registry.FlowDefinitionLocator;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.builder.DefaultFlowServiceLocator;
import org.springframework.webflow.engine.builder.xml.XmlFlowRegistrar;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.factory.FlowExecutionListenerLoader;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockParameterMap;

/**
 * Test case for {@link FlowExecutionImplStateRestorer}.
 * 
 * @author Erwin Vervaet
 */
public class FlowExecutionImplStateRestorerTests extends TestCase {

	private FlowExecutionImpl flowExecution;

	private FlowDefinitionLocator flowLocator;

	private FlowExecutionImplStateRestorer stateRestorer;

	protected void setUp() throws Exception {
		FlowDefinitionRegistry registry = new FlowDefinitionRegistryImpl();
		XmlFlowRegistrar registrar = new XmlFlowRegistrar(new DefaultFlowServiceLocator(registry,
				new StaticListableBeanFactory()));
		registrar.addLocation(new ClassPathResource("testFlow.xml", getClass()));
		registrar.addLocation(new ClassPathResource("external-subflow.xml", getClass()));
		registrar.registerFlowDefinitions(registry);
		final Flow flow = (Flow) registry.getFlowDefinition("testFlow");
		flowLocator = registry;

		FlowExecutionListener listener1 = new FlowExecutionListenerAdapter() {
		};
		final FlowExecutionListener[] listeners = new FlowExecutionListener[] { listener1 };

		MutableAttributeMap attributes = new LocalAttributeMap();
		attributes.put("foo", "bar");
		flowExecution = new FlowExecutionImpl(flow, listeners, attributes);

		MutableAttributeMap conversationScope = new LocalAttributeMap();
		conversationScope.put("baz", "bear");
		flowExecution.setConversationScope(conversationScope);

		FlowExecutionListenerLoader listenerLoader = new FlowExecutionListenerLoader() {
			public FlowExecutionListener[] getListeners(FlowDefinition flow) {
				return listeners;
			}
		};
		stateRestorer = new FlowExecutionImplStateRestorer(flowLocator);
		stateRestorer.setExecutionListenerLoader(listenerLoader);
		stateRestorer.setExecutionAttributes(attributes);
	}

	public void testRehydrate() throws Exception {
		// setup some input data
		MockParameterMap input = new MockParameterMap();
		input.put("name", "value");
		// start the flow execution
		flowExecution.start(null, new MockExternalContext(input));
		runFlowExecutionRestoreTest();
	}

	public void testRehydrateNotStarted() throws Exception {
		// don't start the flow execution
		runFlowExecutionRestoreTest();
	}

	protected void runFlowExecutionRestoreTest() throws Exception {
		// serialize the flowExecution
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bout);
		oout.writeObject(flowExecution);
		oout.flush();

		// deserialize the flowExecution
		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
		ObjectInputStream oin = new ObjectInputStream(bin);
		FlowExecutionImpl restoredFlowExecution = (FlowExecutionImpl) oin.readObject();
		assertNotNull(restoredFlowExecution);
		assertNull(restoredFlowExecution.getDefinition());

		stateRestorer.restoreState(restoredFlowExecution, flowExecution.getConversationScope());
		assertNotNull(restoredFlowExecution.getDefinition());
		assertEquals(flowExecution.isActive(), restoredFlowExecution.isActive());
		if (flowExecution.isActive()) {
			assertEquals(flowExecution.getActiveSession().getScope().asMap(), restoredFlowExecution.getActiveSession()
					.getScope().asMap());
			assertEquals(flowExecution.getActiveSession().getState().getId(), restoredFlowExecution.getActiveSession()
					.getState().getId());
			assertEquals(flowExecution.getActiveSession().getDefinition().getId(), restoredFlowExecution
					.getActiveSession().getDefinition().getId());
			assertSame(flowExecution.getDefinition(), restoredFlowExecution.getDefinition());
		}
		assertEquals(flowExecution.getListeners().size(), restoredFlowExecution.getListeners().size());
		assertEquals(flowExecution.getConversationScope(), restoredFlowExecution.getConversationScope());
		assertEquals(flowExecution.getAttributes(), flowExecution.getAttributes());
	}
}