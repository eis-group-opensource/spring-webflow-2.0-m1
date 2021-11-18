/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

import junit.framework.TestCase;

public class FlowPathUtilsTests extends TestCase {

	public void testNamespaceWithNoSlash() {
		assertEquals("Incorrect namespace", "", FlowPathUtils.extractFlowNamespace("flow"));
	}

	public void testNamespaceWithSlash() {
		assertEquals("Incorrect namespace", "", FlowPathUtils.extractFlowNamespace("/flow"));
	}

	public void testNamespaceWithNamespace() {
		assertEquals("Incorrect namespace", "/namespace", FlowPathUtils.extractFlowNamespace("/namespace/flow"));
	}

	public void teswtNamespaceWithComplexNamespace() {
		assertEquals("Incorrect namespace", "/complex/namespace", FlowPathUtils
				.extractFlowNamespace("/complex/namespace/flow"));
	}

	public void testNamespaceEmpty() {
		try {
			FlowPathUtils.extractFlowNamespace("");
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testNamespaceWhitespace() {
		try {
			FlowPathUtils.extractFlowNamespace(" ");
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testNamespaceNull() {
		try {
			FlowPathUtils.extractFlowNamespace(null);
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testIdWithNoSlash() {
		assertEquals("Incorrect id", "flow", FlowPathUtils.extractFlowId("flow"));
	}

	public void testIdWithSlash() {
		assertEquals("Incorrect id", "flow", FlowPathUtils.extractFlowId("/flow"));
	}

	public void testIdWithNamespace() {
		assertEquals("Incorrect id", "flow", FlowPathUtils.extractFlowId("/namespace/flow"));
	}

	public void testIdWithComplexNamespace() {
		assertEquals("Incorrect id", "flow", FlowPathUtils.extractFlowId("/complex/namespace/flow"));
	}

	public void testIdEmpty() {
		try {
			FlowPathUtils.extractFlowId("");
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testIdWhitespace() {
		try {
			FlowPathUtils.extractFlowId(" ");
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testIdNull() {
		try {
			FlowPathUtils.extractFlowId(null);
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testPathWithEmptyNamespace() {
		assertEquals("Incorrect path", "/flow", FlowPathUtils.buildFlowPath("", "flow"));
	}

	public void testPathWithNamespace() {
		assertEquals("Incorrect path", "/namespace/flow", FlowPathUtils.buildFlowPath("/namespace", "flow"));
	}

	public void testPathWithComplexNamespace() {
		assertEquals("Incorrect path", "/complex/namespace/flow", FlowPathUtils.buildFlowPath("/complex/namespace",
				"flow"));
	}

	public void testPathWithNullNamespace() {
		try {
			FlowPathUtils.buildFlowPath(null, "flow");
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testPathWithEmptyId() {
		try {
			FlowPathUtils.buildFlowPath("", "");
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testPathWithWhitespaceId() {
		try {
			FlowPathUtils.buildFlowPath("", " ");
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testPathWithNullId() {
		try {
			FlowPathUtils.buildFlowPath("", null);
			fail("Should have detected empty input");
		} catch (IllegalArgumentException e) {
		}
	}
}
