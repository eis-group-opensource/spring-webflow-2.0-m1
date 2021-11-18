/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config.scope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.Ordered;
import org.springframework.webflow.execution.ScopeType;

/**
 * Registers the Spring Web Flow bean scopes with a
 * @{link ConfigurableListableBeanFactory}.
 * 
 * @author Ben Hale
 * @see Scope
 */
public class ScopeRegistrar implements BeanFactoryPostProcessor, Ordered {

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		beanFactory.registerScope(ScopeType.FLASH.getLabel().toLowerCase(), new FlashScope());
		beanFactory.registerScope(ScopeType.FLOW.getLabel().toLowerCase(), new FlowScope());
		beanFactory.registerScope(ScopeType.CONVERSATION.getLabel().toLowerCase(), new ConversationScope());
	}

	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
