/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import junit.framework.TestCase;

import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.conversation.impl.SessionBindingConversationManager;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.definition.registry.StaticFlowDefinitionHolder;
import org.springframework.webflow.engine.SimpleFlow;
import org.springframework.webflow.engine.impl.FlowExecutionImplFactory;
import org.springframework.webflow.engine.impl.FlowExecutionImplStateRestorer;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.repository.FlowExecutionKey;
import org.springframework.webflow.execution.repository.FlowExecutionLock;
import org.springframework.webflow.execution.repository.NoSuchFlowExecutionException;
import org.springframework.webflow.execution.repository.support.FlowExecutionStateRestorer;
import org.springframework.webflow.test.MockExternalContext;

/**
 * Unit tests for {@link ClientContinuationFlowExecutionRepository}.
 */
public class ClientContinuationFlowExecutionRepositoryTests extends TestCase {

	private ClientContinuationFlowExecutionRepository repository;

	private FlowExecution execution;

	private FlowExecutionKey key;

	private FlowExecutionLock lock;

	protected void setUp() throws Exception {
		FlowDefinitionRegistry registry = new FlowDefinitionRegistryImpl();
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(new SimpleFlow()));
		execution = new FlowExecutionImplFactory().createFlowExecution(registry.getFlowDefinition("simpleFlow"));
		FlowExecutionStateRestorer stateRestorer = new FlowExecutionImplStateRestorer(registry);
		repository = new ClientContinuationFlowExecutionRepository(stateRestorer,
				new SessionBindingConversationManager());
		ExternalContextHolder.setExternalContext(new MockExternalContext());
	}

	public void testPutExecution() {
		key = repository.generateKey(execution);
		assertNotNull(key);
		lock = repository.getLock(key);
		lock.lock();
		repository.putFlowExecution(key, execution);
		FlowExecution persisted = repository.getFlowExecution(key);
		assertNotNull(persisted);
		lock.unlock();
	}

	public void testGetNextKey() {
		key = repository.generateKey(execution);
		assertNotNull(key);
		lock = repository.getLock(key);
		lock.lock();
		repository.putFlowExecution(key, execution);
		FlowExecutionKey nextKey = repository.getNextKey(execution, key);
		repository.putFlowExecution(nextKey, execution);
		FlowExecution persisted = repository.getFlowExecution(nextKey);
		assertNotNull(persisted);
		lock.unlock();
	}

	public void testGetNextKeyVerifyKeyChanged() {
		key = repository.generateKey(execution);
		assertNotNull(key);
		lock = repository.getLock(key);
		lock.lock();
		repository.putFlowExecution(key, execution);
		FlowExecutionKey nextKey = repository.getNextKey(execution, key);
		repository.putFlowExecution(nextKey, execution);
		repository.getFlowExecution(key);
		repository.getFlowExecution(nextKey);
		lock.unlock();
	}

	public void testRemove() {
		testPutExecution();
		lock.lock();
		repository.removeFlowExecution(key);
		try {
			repository.getFlowExecution(key);
			fail("should've throw nsfee");
		} catch (NoSuchFlowExecutionException e) {
		}
		lock.unlock();
	}

	public void testLock() {
		testPutExecution();
		FlowExecutionLock lock = repository.getLock(key);
		lock.lock();
		repository.getFlowExecution(key);
		lock.unlock();
	}

	public void testLockLock() {
		testPutExecution();
		FlowExecutionLock lock = repository.getLock(key);
		lock.lock();
		lock.lock();
		repository.getFlowExecution(key);
		lock.unlock();
		lock.unlock();
	}
}