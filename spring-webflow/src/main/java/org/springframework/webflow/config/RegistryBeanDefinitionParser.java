/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.springframework.webflow.engine.builder.xml.XmlFlowRegistryFactoryBean;
import org.w3c.dom.Element;

/**
 * {@link BeanDefinitionParser} for the <code>&lt;registry&gt;</code> tag.
 * 
 * @author Ben Hale
 */
class RegistryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	// elements and attributes

	private static final String LOCATION_ELEMENT = "location";

	// properties

	private static final String FLOW_LOCATIONS_PROPERTY = "flowLocations";

	private static final String PATH_ATTRIBUTE = "path";

	protected Class getBeanClass(Element element) {
		return XmlFlowRegistryFactoryBean.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder definitionBuilder) {
		List locationElements = DomUtils.getChildElementsByTagName(element, LOCATION_ELEMENT);
		List locations = getLocations(locationElements);
		definitionBuilder.addPropertyValue(FLOW_LOCATIONS_PROPERTY, locations.toArray(new String[locations.size()]));
	}

	/**
	 * Parse location definitions from given list of location elements.
	 */
	private List getLocations(List locationElements) {
		List locations = new ArrayList(locationElements.size());
		for (Iterator i = locationElements.iterator(); i.hasNext();) {
			Element locationElement = (Element) i.next();
			String path = locationElement.getAttribute(PATH_ATTRIBUTE);
			if (StringUtils.hasText(path)) {
				locations.add(path);
			}
		}
		return locations;
	}
}