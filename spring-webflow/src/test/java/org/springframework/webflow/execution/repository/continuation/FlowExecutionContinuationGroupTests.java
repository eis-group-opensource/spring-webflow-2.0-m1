/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import junit.framework.TestCase;

import org.springframework.webflow.config.FlowExecutorFactoryBean;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.conversation.impl.SessionBindingConversationManager;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.registry.FlowDefinitionLocator;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.definition.registry.StaticFlowDefinitionHolder;
import org.springframework.webflow.engine.builder.AbstractFlowBuilder;
import org.springframework.webflow.engine.builder.FlowAssembler;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.engine.impl.FlowExecutionImplStateRestorer;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.repository.FlowExecutionKey;
import org.springframework.webflow.execution.repository.FlowExecutionLock;
import org.springframework.webflow.execution.repository.NoSuchFlowExecutionException;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.execution.support.FlowExecutionRedirect;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.executor.ResponseInstruction;
import org.springframework.webflow.executor.support.RequestParameterFlowExecutorArgumentHandler;
import org.springframework.webflow.test.MockExternalContext;

/**
 * Unit tests for {@link FlowExecutionContinuationGroup}.
 * 
 * @author Erwin Vervaet
 */
public class FlowExecutionContinuationGroupTests extends TestCase {

	public void testUpdateFlowExecution() {
		FlowExecutionContinuationGroup group = new FlowExecutionContinuationGroup(-1);
		assertEquals(0, group.getContinuationCount());
		FlowExecutionContinuation continuation1 = new TestFlowExecutionContinuation();
		group.add("1", continuation1);
		assertEquals(1, group.getContinuationCount());
		assertSame(continuation1, group.get("1"));
		FlowExecutionContinuation continuation2 = new TestFlowExecutionContinuation();
		group.add("2", continuation2);
		assertEquals(2, group.getContinuationCount());
		assertSame(continuation1, group.get("1"));
		assertSame(continuation2, group.get("2"));
		FlowExecutionContinuation updatedContinuation2 = new TestFlowExecutionContinuation();
		group.add("2", updatedContinuation2);
		assertEquals(2, group.getContinuationCount());
		assertSame(continuation1, group.get("1"));
		assertSame(updatedContinuation2, group.get("2"));
	}

	public void testUpdateFlowExecutionWithMaxContinuations() {
		FlowExecutionContinuationGroup group = new FlowExecutionContinuationGroup(2);
		FlowExecutionContinuation continuation1 = new TestFlowExecutionContinuation();
		group.add("1", continuation1);
		FlowExecutionContinuation continuation2 = new TestFlowExecutionContinuation();
		group.add("2", continuation2);
		assertEquals(2, group.getContinuationCount());
		assertSame(continuation1, group.get("1"));
		assertSame(continuation2, group.get("2"));
		FlowExecutionContinuation updatedContinuation2 = new TestFlowExecutionContinuation();
		group.add("2", updatedContinuation2);
		assertEquals(2, group.getContinuationCount());
		assertSame(continuation1, group.get("1"));
		assertSame(updatedContinuation2, group.get("2"));
		FlowExecutionContinuation continuation3 = new TestFlowExecutionContinuation();
		group.add("3", continuation3);
		assertEquals(2, group.getContinuationCount());
		try {
			group.get("1");
			fail();
		} catch (ContinuationNotFoundException e) {
			// expected
		}
		assertSame(updatedContinuation2, group.get("2"));
		assertSame(continuation3, group.get("3"));
		updatedContinuation2 = new TestFlowExecutionContinuation();
		group.add("2", updatedContinuation2);
		FlowExecutionContinuation continuation4 = new TestFlowExecutionContinuation();
		group.add("4", continuation4);
		assertEquals(2, group.getContinuationCount());
		try {
			group.get("3");
			fail();
		} catch (ContinuationNotFoundException e) {
			// expected
		}
		assertSame(updatedContinuation2, group.get("2"));
		assertSame(continuation4, group.get("4"));
	}

	public void testViaFlowExecutor() throws Exception {
		FlowDefinitionRegistry registry = new FlowDefinitionRegistryImpl();
		FlowDefinition testFlow = new FlowAssembler("testFlow", new TestFlowBuilder()).assembleFlow();
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(testFlow));

		ConversationManager conversationManager = new SessionBindingConversationManager();

		FlowExecutorFactoryBean flowExecutorFactory = new FlowExecutorFactoryBean();
		flowExecutorFactory.setDefinitionLocator(registry);
		flowExecutorFactory.setConversationManager(conversationManager);
		flowExecutorFactory.afterPropertiesSet();
		FlowExecutor flowExecutor = (FlowExecutor) flowExecutorFactory.getObject();

		MockExternalContext externalContext = new MockExternalContext();

		GroupGetter groupGetter = new GroupGetter(registry, conversationManager);

		// obtain continuation group
		ResponseInstruction response = flowExecutor.launch("testFlow", externalContext);
		externalContext.putRequestParameter("_flowExecutionKey", response.getFlowExecutionKey());
		FlowExecutionContinuationGroup group = groupGetter.getContinuationGroup(externalContext);
		assertNotNull(group);

		assertTrue(response.getViewSelection() instanceof FlowExecutionRedirect);
		assertEquals(1, group.getContinuationCount());
		externalContext.putRequestParameter("_flowExecutionKey", response.getFlowExecutionKey());
		response = flowExecutor.refresh(response.getFlowExecutionKey(), externalContext);
		assertEquals("viewName", ((ApplicationView) response.getViewSelection()).getViewName());
		assertEquals(1, group.getContinuationCount());

		externalContext.putRequestParameter("_flowExecutionKey", response.getFlowExecutionKey());
		response = flowExecutor.resume(response.getFlowExecutionKey(), "next", externalContext);
		assertTrue(response.getViewSelection() instanceof FlowExecutionRedirect);
		assertEquals(2, group.getContinuationCount());
		externalContext.putRequestParameter("_flowExecutionKey", response.getFlowExecutionKey());
		response = flowExecutor.refresh(response.getFlowExecutionKey(), externalContext);
		assertEquals("nextViewName", ((ApplicationView) response.getViewSelection()).getViewName());
		assertEquals(2, group.getContinuationCount());

		externalContext.putRequestParameter("_flowExecutionKey", response.getFlowExecutionKey());
		response = flowExecutor.refresh(response.getFlowExecutionKey(), externalContext);
		assertEquals("nextViewName", ((ApplicationView) response.getViewSelection()).getViewName());
		assertEquals(2, group.getContinuationCount());

		externalContext.putRequestParameter("_flowExecutionKey", response.getFlowExecutionKey());
		response = flowExecutor.resume(response.getFlowExecutionKey(), "end", externalContext);

		try {
			groupGetter.getContinuationGroup(externalContext);
			fail();
		} catch (NoSuchFlowExecutionException e) {
			// expected
		}
	}

	private static class TestFlowExecutionContinuation extends FlowExecutionContinuation {

		public FlowExecution unmarshal() throws ContinuationUnmarshalException {
			return null;
		}

		public byte[] toByteArray() {
			return new byte[0];
		}
	}

	private static class TestFlowBuilder extends AbstractFlowBuilder {
		public void buildStates() throws FlowBuilderException {
			addViewState("viewState", "viewName", transition(on("next"), to("nextViewState")));
			addViewState("nextViewState", "nextViewName", transition(on("end"), to("endState")));
			addEndState("endState");
		}
	}

	private static class GroupGetter extends ContinuationFlowExecutionRepository {

		public GroupGetter(FlowDefinitionLocator definitionLocator, ConversationManager conversationManager) {
			super(new FlowExecutionImplStateRestorer(definitionLocator), conversationManager);
		}

		public FlowExecutionContinuationGroup getContinuationGroup(ExternalContext externalContext) {
			ExternalContextHolder.setExternalContext(externalContext);
			try {
				FlowExecutionKey key = parseFlowExecutionKey(new RequestParameterFlowExecutorArgumentHandler()
						.extractFlowExecutionKey(externalContext));
				FlowExecutionLock lock = getLock(key);
				lock.lock();
				try {
					return getContinuationGroup(key);
				} finally {
					lock.unlock();
				}
			} finally {
				ExternalContextHolder.setExternalContext(null);
			}
		}
	}
}
