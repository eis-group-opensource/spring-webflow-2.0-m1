/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.method.MethodKey;
import org.springframework.binding.method.MethodSignature;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.Action;

/**
 * A helper factory for {@link Action} instances that invoke methods on beans managed in a Spring bean factory.
 * <p>
 * This factory encapsulates the logic required to take an arbitrary <code>java.lang.Object</code> from a Spring bean
 * factory and adapt a method on it to the {@link Action} interface. If the bean you want to use is not managed in a
 * Spring bean factory, consider subclassing {@link AbstractBeanInvokingAction} and using it directly.
 * 
 * @see AbstractBeanInvokingAction
 * 
 * @author Keith Donald
 */
public class BeanInvokingActionFactory {

	/**
	 * Determines which result event factory should be used for each bean invoking action created by this factory.
	 */
	private ResultEventFactorySelector resultEventFactorySelector = new ResultEventFactorySelector();

	/**
	 * Returns the strategy for calculating the result event factory to configure for each bean invoking action created
	 * by this factory.
	 */
	public ResultEventFactorySelector getResultEventFactorySelector() {
		return resultEventFactorySelector;
	}

	/**
	 * Sets the strategy to calculate the result event factory to configure for each bean invoking action created by
	 * this factory.
	 */
	public void setResultEventFactorySelector(ResultEventFactorySelector resultEventFactorySelector) {
		this.resultEventFactorySelector = resultEventFactorySelector;
	}

	/**
	 * Factory method that creates a bean invoking action, an adapter that adapts a method on an abitrary {@link Object}
	 * to the {@link Action} interface. This method is an atomic operation that returns a fully initialized Action. It
	 * encapsulates the selection of the action implementation as well as the action assembly.
	 * @param beanId the id of the bean to be adapted to an Action instance
	 * @param beanFactory the bean factory where the bean is managed
	 * @param methodSignature the method to invoke on the bean when the action is executed (required)
	 * @param resultExposer the specification for what to do with the method return value (optional)
	 * @param conversionService the conversion service to be used to convert method parameters (optional)
	 * @param attributes attributes that may be used to affect the bean invoking action's construction
	 * @return the fully configured bean invoking action instance
	 */
	public Action createBeanInvokingAction(String beanId, BeanFactory beanFactory, MethodSignature methodSignature,
			ActionResultExposer resultExposer, ConversionService conversionService, AttributeMap attributes) {
		Object bean = beanFactory.getBean(beanId);
		AbstractBeanInvokingAction action = new LocalBeanInvokingAction(methodSignature, bean);
		action.setMethodResultExposer(resultExposer);
		MethodKey methodKey = new MethodKey(bean.getClass(), methodSignature.getMethodName(), methodSignature
				.getParameters().getTypesArray());
		action.setResultEventFactory(resultEventFactorySelector.forMethod(methodKey.getMethod()));
		action.setConversionService(conversionService);
		return action;
	}
}