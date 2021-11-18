/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.springframework.webflow.execution.factory.ConditionalFlowExecutionListenerLoader;
import org.w3c.dom.Element;

/**
 * {@link BeanDefinitionParser} for the <code>&lt;execution-listeners&gt;</code> tag.
 * 
 * @author Ben Hale
 */
class ExecutionListenersBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	// elements and attributes

	private static final String LISTENER_ELEMENT = "listener";

	// properties

	private static final String LISTENERS_PROPERTY = "listeners";

	private static final String CRITERIA_ATTRIBUTE = "criteria";

	private static final String REF_ATTRIBUTE = "ref";

	protected Class getBeanClass(Element element) {
		return ConditionalFlowExecutionListenerLoader.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder definitionBuilder) {
		List listenerElements = DomUtils.getChildElementsByTagName(element, LISTENER_ELEMENT);
		definitionBuilder.addPropertyValue(LISTENERS_PROPERTY, getListenersWithCriteria(listenerElements));
	}

	/**
	 * Creates a map of listeners with their associated criteria.
	 * @param listeners the list of listener elements from the bean definition
	 * @return a map containing keys that are references to given listeners and values of string that represent the
	 * criteria
	 */
	private Map getListenersWithCriteria(List listeners) {
		Map listenersWithCriteria = new ManagedMap(listeners.size());
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			Element listenerElement = (Element) i.next();
			RuntimeBeanReference ref = new RuntimeBeanReference(listenerElement.getAttribute(REF_ATTRIBUTE));
			String criteria = listenerElement.getAttribute(CRITERIA_ATTRIBUTE);
			listenersWithCriteria.put(ref, criteria);
		}
		return listenersWithCriteria;
	}
}