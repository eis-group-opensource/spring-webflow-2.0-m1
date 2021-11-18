/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import org.springframework.webflow.engine.WildcardTransitionCriteria;
import org.springframework.webflow.test.MockRequestContext;

import junit.framework.TestCase;

/**
 * Unit tests for {@link NotTransitionCriteria}.
 * 
 * @author Erwin Vervaet
 */
public class NotTransitionCriteriaTests extends TestCase {

	public void testNull() {
		try {
			new NotTransitionCriteria(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void testNegation() {
		assertFalse(new NotTransitionCriteria(WildcardTransitionCriteria.INSTANCE).test(new MockRequestContext()));
	}

}
