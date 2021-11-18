/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * EntityResolver implementation for the Spring Web Flow 1.0 XML Schema. This will load the XSD from the classpath.
 * <p>
 * The xmlns of the XSD expected to be resolved:
 * 
 * <pre>
 *     &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 *     &lt;flow xmlns=&quot;http://www.springframework.org/schema/webflow&quot;
 *           xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;
 *           xsi:schemaLocation=&quot;http://www.springframework.org/schema/webflow
 *                               http://www.springframework.org/schema/webflow/spring-webflow-1.0.xsd&quot;&gt;
 * </pre>
 * 
 * @author Erwin Vervaet
 * @author Ben Hale
 */
public class WebFlowEntityResolver implements EntityResolver {

	private static final String WEBFLOW_ELEMENT = "spring-webflow-1.0";

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		if (systemId != null && systemId.indexOf(WEBFLOW_ELEMENT) > systemId.lastIndexOf("/")) {
			String filename = systemId.substring(systemId.indexOf(WEBFLOW_ELEMENT));
			try {
				Resource resource = new ClassPathResource(filename, getClass());
				InputSource source = new InputSource(resource.getInputStream());
				source.setPublicId(publicId);
				source.setSystemId(systemId);
				return source;
			} catch (IOException ex) {
				// fall through below
			}
		}
		// let the parser handle it
		return null;
	}
}