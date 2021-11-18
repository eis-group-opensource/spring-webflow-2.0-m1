/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

import junit.framework.TestCase;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;

/**
 * Unit tests for {@link FlowDefinitionRegistryImpl}.
 */
public class FlowDefinitionRegistryImplTests extends TestCase {

	private FlowDefinitionRegistryImpl registry = new FlowDefinitionRegistryImpl();

	private FlowDefinition fooFlow;

	private FlowDefinition barFlow;

	protected void setUp() {
		fooFlow = new FooFlow();
		barFlow = new BarFlow();
	}

	public void testEmptyRegistryAsserts() {
		assertEquals(0, registry.getFlowDefinitionCount());
		assertEquals(0, registry.getFlowDefinitionPaths().length);
	}

	public void testNoSuchFlowDefinition() {
		try {
			registry.getFlowDefinition("bogus");
			fail("Should've bombed with NoSuchFlow");
		} catch (NoSuchFlowDefinitionException e) {
		}
	}

	public void testNoSuchFlowDefinitionWithNamespace() {
		try {
			registry.getFlowDefinition("/namespace/bogus");
			fail("Should've bombed with NoSuchFlow");
		} catch (NoSuchFlowDefinitionException e) {

		}
	}

	public void testRegisterFlow() {
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(fooFlow));
		assertEquals(1, registry.getFlowDefinitionCount());
		assertEquals("/foo", registry.getFlowDefinitionPaths()[0]);
		assertEquals("foo", registry.getFlowDefinition("foo").getId());
	}

	public void testRegisterFlowWithNamespace() {
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(barFlow), "/namespace");
		assertEquals(1, registry.getFlowDefinitionCount());
		assertEquals("/namespace/bar", registry.getFlowDefinitionPaths()[0]);
		assertEquals("bar", registry.getFlowDefinition("/namespace/bar").getId());
	}

	public void testRegisterFlowSameIds() {
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(fooFlow));
		FooFlow newFlow = new FooFlow();
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(newFlow));
		assertEquals(1, registry.getFlowDefinitionCount());
		assertSame(newFlow, registry.getFlowDefinition("foo"));
	}

	public void testRegisterFlowSameIdsWithNamespace() {
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(barFlow), "/namespace");
		BarFlow newFlow = new BarFlow();
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(newFlow), "/namespace");
		assertEquals(1, registry.getFlowDefinitionCount());
		assertSame(newFlow, registry.getFlowDefinition("/namespace/bar"));
	}

	public void testRegisterMultipleFlows() {
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(fooFlow));
		FooFlow newFlow = new FooFlow();
		newFlow.id = "bar";
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(newFlow));
		assertEquals(2, registry.getFlowDefinitionCount());
		assertSame(fooFlow, registry.getFlowDefinition("foo"));
		assertSame(newFlow, registry.getFlowDefinition("bar"));
	}

	public void testRegisterMultipleFlowsWithNamespace() {
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(barFlow), "/namespace");
		BarFlow newFlow = new BarFlow();
		newFlow.id = "foo";
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(newFlow), "/namespace");
		assertEquals(2, registry.getFlowDefinitionCount());
		assertSame(barFlow, registry.getFlowDefinition("/namespace/bar"));
		assertSame(newFlow, registry.getFlowDefinition("/namespace/foo"));
	}

	public void testRefresh() {
		testRegisterMultipleFlows();
		registry.refresh();
		assertEquals(2, registry.getFlowDefinitionCount());
		assertSame(fooFlow, registry.getFlowDefinition("foo"));
	}

	public void testRefreshWithNamespace() {
		testRegisterMultipleFlowsWithNamespace();
		registry.refresh();
		assertEquals(2, registry.getFlowDefinitionCount());
		assertSame(barFlow, registry.getFlowDefinition("/namespace/bar"));
	}

	public void testRefreshValidFlow() {
		testRegisterMultipleFlows();
		registry.refresh("foo");
		assertEquals(2, registry.getFlowDefinitionCount());
		assertSame(fooFlow, registry.getFlowDefinition("foo"));
	}

	public void testRefreshValidFlowWithNamespace() {
		testRegisterMultipleFlowsWithNamespace();
		registry.refresh("/namespace/bar");
		assertEquals(2, registry.getFlowDefinitionCount());
		assertSame(barFlow, registry.getFlowDefinition("/namespace/bar"));
	}

	public void testRefreshNoSuchFlow() {
		testRegisterMultipleFlows();
		try {
			registry.refresh("bogus");
			fail("Should've bombed with NoSuchFlow");
		} catch (NoSuchFlowDefinitionException e) {

		}
	}

	public void testRefreshNoSuchFlowWithNamespace() {
		testRegisterMultipleFlowsWithNamespace();
		try {
			registry.refresh("/namespace/bogus");
			fail("Should've bombed with NoSuchFlow");
		} catch (NoSuchFlowDefinitionException e) {

		}
	}

	public void testParentHierarchy() {
		testRegisterMultipleFlows();
		FlowDefinitionRegistryImpl child = new FlowDefinitionRegistryImpl();
		child.setParent(registry);
		FooFlow fooFlow = new FooFlow();
		child.registerFlowDefinition(new StaticFlowDefinitionHolder(fooFlow));
		assertSame(fooFlow, child.getFlowDefinition("foo"));
		assertEquals("bar", child.getFlowDefinition("bar").getId());
	}

	public void testParentHierarchyWithNamespace() {
		testRegisterMultipleFlowsWithNamespace();
		FlowDefinitionRegistryImpl child = new FlowDefinitionRegistryImpl();
		child.setParent(registry);
		BarFlow barFlow = new BarFlow();
		child.registerFlowDefinition(new StaticFlowDefinitionHolder(barFlow), "/namespace");
		assertSame(barFlow, child.getFlowDefinition("/namespace/bar"));
		assertEquals("bar", child.getFlowDefinition("/namespace/bar").getId());
	}

	private static class FooFlow implements FlowDefinition {
		private String id = "foo";

		public AttributeMap getAttributes() {
			return null;
		}

		public String getCaption() {
			return null;
		}

		public String getDescription() {
			return null;
		}

		public String getId() {
			return id;
		}

		public StateDefinition getStartState() {
			return null;
		}

		public StateDefinition getState(String id) throws IllegalArgumentException {
			return null;
		}
	}

	private static class BarFlow implements FlowDefinition {
		private String id = "bar";

		public AttributeMap getAttributes() {
			return null;
		}

		public String getCaption() {
			return null;
		}

		public String getDescription() {
			return null;
		}

		public String getId() {
			return id;
		}

		public StateDefinition getStartState() {
			return null;
		}

		public StateDefinition getState(String id) throws IllegalArgumentException {
			return null;
		}
	}
}