/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config;

import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.webflow.config.scope.ScopeRegistrar;
import org.w3c.dom.Element;

/**
 * {@link BeanDefinitionParser} for the <code>&lt;enable-scopes&gt;</code> tag.
 * 
 * @author Ben Hale
 */
class EnableScopesBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	protected Class getBeanClass(Element element) {
		return ScopeRegistrar.class;
	}

	protected boolean shouldGenerateId() {
		return true;
	}

}
