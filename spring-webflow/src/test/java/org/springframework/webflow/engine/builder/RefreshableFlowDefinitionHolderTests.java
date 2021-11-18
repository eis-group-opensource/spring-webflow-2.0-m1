/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.engine.builder.xml.XmlFlowBuilder;
import org.springframework.webflow.util.ResourceHolder;

/**
 * Unit tests for {@link RefreshableFlowDefinitionHolder}.
 */
public class RefreshableFlowDefinitionHolderTests extends TestCase {

	public void testNoRefreshOnNoChange() {
		File parent = new File("src/test/java/org/springframework/webflow/engine/builder/xml");
		Resource location = new FileSystemResource(new File(parent, "flow.xml"));
		XmlFlowBuilder flowBuilder = new XmlFlowBuilder(location);
		FlowAssembler assembler = new FlowAssembler("flow", flowBuilder);
		RefreshableFlowDefinitionHolder holder = new RefreshableFlowDefinitionHolder(assembler);
		assertEquals("flow", holder.getFlowDefinitionId());
		assertSame(flowBuilder, holder.getFlowBuilder());
		assertEquals(0, holder.getLastModified());
		assertTrue(!holder.isAssembled());
		FlowDefinition flow1 = holder.getFlowDefinition();
		assertTrue(holder.isAssembled());
		long lastModified = holder.getLastModified();
		assertTrue(lastModified != -1);
		assertTrue(lastModified > 0);
		FlowDefinition flow2 = holder.getFlowDefinition();
		assertEquals("flow", flow2.getId());
		assertEquals(lastModified, holder.getLastModified());
		assertSame(flow1, flow2);
	}

	public void testReloadOnChange() throws Exception {
		MockFlowBuilder mockFlowBuilder = new MockFlowBuilder();
		FlowAssembler assembler = new FlowAssembler("mockFlow", mockFlowBuilder);
		RefreshableFlowDefinitionHolder holder = new RefreshableFlowDefinitionHolder(assembler);

		mockFlowBuilder.lastModified = 0L;
		assertEquals(0, mockFlowBuilder.buildCallCount);
		holder.getFlowDefinition();
		assertEquals(1, mockFlowBuilder.buildCallCount);
		holder.getFlowDefinition();
		assertEquals(1, mockFlowBuilder.buildCallCount);
		holder.getFlowDefinition();
		assertEquals(1, mockFlowBuilder.buildCallCount);
		mockFlowBuilder.lastModified = 10L;
		holder.getFlowDefinition();
		assertEquals(2, mockFlowBuilder.buildCallCount);
		holder.getFlowDefinition();
		assertEquals(2, mockFlowBuilder.buildCallCount);
		holder.refresh();
		assertEquals(3, mockFlowBuilder.buildCallCount);
		holder.refresh();
		assertEquals(4, mockFlowBuilder.buildCallCount);
	}

	private class MockFlowBuilder extends AbstractFlowBuilder implements ResourceHolder {

		public int buildCallCount = 0;
		public long lastModified = 0L;

		public void buildStates() throws FlowBuilderException {
			addEndState("end");
			buildCallCount++;
		}

		public Resource getResource() {
			return new AbstractResource() {

				public File getFile() throws IOException {
					return new File("mock") {
						public long lastModified() {
							return lastModified;
						}
					};
				}

				public String getDescription() {
					return null;
				}

				public InputStream getInputStream() throws IOException {
					return null;
				}
			};
		}
	}

}