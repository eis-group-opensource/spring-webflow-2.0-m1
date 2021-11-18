/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

import junit.framework.TestCase;

/**
 * Unit tests for {@link CollectionUtils}.
 */
public class CollectionUtilsTests extends TestCase {

	public void testSingleEntryMap() {
		AttributeMap map1 = CollectionUtils.singleEntryMap("foo", "bar");
		AttributeMap map2 = CollectionUtils.singleEntryMap("foo", "bar");
		assertEquals(map1, map2);
	}
}
