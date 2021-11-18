/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A generic strategy interface encapsulating the logic to load an XML-based document.
 * 
 * @author Keith Donald
 */
public interface DocumentLoader {

	/**
	 * Load the XML-based document from the external resource.
	 * @param resource the document resource
	 * @return the loaded (parsed) document
	 * @throws IOException an exception occured accessing the resource input stream
	 * @throws ParserConfigurationException an exception occured building the document parser
	 * @throws SAXException a error occured during document parsing
	 */
	public Document loadDocument(Resource resource) throws IOException, ParserConfigurationException, SAXException;
}