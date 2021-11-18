/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

public class DefaultDocumentLoaderTests extends TestCase {
	private DefaultDocumentLoader loader = new DefaultDocumentLoader();

	public void testLoad() throws Exception {
		Resource resource = new ClassPathResource("testFlow1.xml", getClass());
		Document document = loader.loadDocument(resource);
		assertNotNull(document);
	}
}
