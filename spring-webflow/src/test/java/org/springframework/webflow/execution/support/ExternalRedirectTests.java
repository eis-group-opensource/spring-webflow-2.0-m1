/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.support;

import junit.framework.TestCase;

/**
 * Unit tests for {@link ExternalRedirect}.
 */
public class ExternalRedirectTests extends TestCase {

	private ExternalRedirect redirect;

	protected void setUp() throws Exception {
	}

	public void testStaticExpression() {
		redirect = new ExternalRedirect("my/url");
		assertEquals("my/url", redirect.getUrl());
	}
}