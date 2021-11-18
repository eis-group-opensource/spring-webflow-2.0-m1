/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import junit.framework.TestCase;

import org.springframework.webflow.engine.NullViewSelector;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.test.MockRequestContext;

public class NullViewSelectionTests extends TestCase {

	private MockRequestContext context = new MockRequestContext();

	public void testMakeSelection() {
		assertEquals(ViewSelection.NULL_VIEW, NullViewSelector.INSTANCE.makeEntrySelection(context));
	}

	public void testMakeRefreshSelection() {
		assertEquals(ViewSelection.NULL_VIEW, NullViewSelector.INSTANCE.makeRefreshSelection(context));
	}
}
