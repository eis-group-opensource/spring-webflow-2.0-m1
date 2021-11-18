/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.support.DefaultConversionService;
import org.springframework.binding.method.MethodInvoker;
import org.springframework.binding.method.MethodSignature;
import org.springframework.util.Assert;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Base class for actions that delegate to methods on beans (POJOs - Plain Old Java Objects). Acts as an adapter that
 * adapts an {@link Object} method to the Spring Web Flow {@link Action} contract.
 * <p>
 * Subclasses are required to implement the {@link #getBean(RequestContext)} method, returning the bean on which a
 * method should be invoked.
 * 
 * @see BeanInvokingActionFactory
 * 
 * @author Keith Donald
 */
public abstract class AbstractBeanInvokingAction extends AbstractAction {

	/**
	 * The signature of the method to invoke on the target bean, capable of resolving the method when used with a
	 * {@link MethodInvoker}. Required.
	 */
	private MethodSignature methodSignature;

	/**
	 * The method invoker that performs the action-&gt;bean method binding, accepting a {@link MethodSignature} and
	 * {@link #getBean(RequestContext) target bean} instance.
	 */
	private MethodInvoker methodInvoker = new MethodInvoker();

	/**
	 * The specification (configuration) for how bean method return values should be exposed to an executing flow that
	 * invokes this action.
	 */
	private ActionResultExposer methodResultExposer;

	/**
	 * The strategy that adapts bean method return values to Event objects.
	 */
	private ResultEventFactory resultEventFactory = new SuccessEventFactory();

	/**
	 * Creates a new bean invoking action.
	 * @param methodSignature the signature of the method to invoke
	 */
	protected AbstractBeanInvokingAction(MethodSignature methodSignature) {
		Assert.notNull(methodSignature, "The signature of the target method to invoke is required");
		this.methodSignature = methodSignature;
	}

	/**
	 * Returns the signature of the method to invoke on the target bean.
	 */
	public MethodSignature getMethodSignature() {
		return methodSignature;
	}

	/**
	 * Returns the configuration for how bean method return values should be exposed to an executing flow that invokes
	 * this action.
	 */
	public ActionResultExposer getMethodResultExposer() {
		return methodResultExposer;
	}

	/**
	 * Configures how bean method return values should be exposed to an executing flow that invokes this action. This is
	 * optional. By default the bean method return values do not get exposed to the executing flow.
	 */
	public void setMethodResultExposer(ActionResultExposer methodResultExposer) {
		this.methodResultExposer = methodResultExposer;
	}

	/**
	 * Returns the event adaption strategy used by this action.
	 */
	protected ResultEventFactory getResultEventFactory() {
		return resultEventFactory;
	}

	/**
	 * Set the bean return value-&gt;event adaption strategy. Defaults to {@link SuccessEventFactory}, so all bean
	 * method return values will be interpreted as "success".
	 */
	public void setResultEventFactory(ResultEventFactory resultEventFactory) {
		this.resultEventFactory = resultEventFactory;
	}

	/**
	 * Set the conversion service to perform type conversion of event parameters to method arguments as neccessary.
	 * Defaults to {@link DefaultConversionService}.
	 */
	public void setConversionService(ConversionService conversionService) {
		methodInvoker.setConversionService(conversionService);
	}

	/**
	 * Returns the bean method invoker helper.
	 */
	protected MethodInvoker getMethodInvoker() {
		return methodInvoker;
	}

	protected Event doExecute(RequestContext context) throws Exception {
		Object bean = getBean(context);
		Object returnValue = getMethodInvoker().invoke(methodSignature, bean, context);
		if (methodResultExposer != null) {
			methodResultExposer.exposeResult(returnValue, context);
		}
		return resultEventFactory.createResultEvent(bean, returnValue, context);
	}

	// subclassing hooks

	/**
	 * Retrieves the bean to invoke a method on. Subclasses need to implement this method.
	 * @param context the flow execution request context
	 * @return the bean on which to invoke methods
	 * @throws Exception when the bean cannot be retreived
	 */
	protected abstract Object getBean(RequestContext context) throws Exception;

}