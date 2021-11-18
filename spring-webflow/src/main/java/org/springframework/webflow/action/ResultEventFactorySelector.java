/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import java.lang.reflect.Method;

/**
 * Helper that selects the {@link ResultEventFactory} to use for a particular result object.
 * 
 * @see EvaluateAction
 * @see BeanInvokingActionFactory
 * 
 * @author Keith Donald
 */
public class ResultEventFactorySelector {

	/**
	 * The event factory instance for mapping a return value to a success event.
	 */
	private SuccessEventFactory successEventFactory = new SuccessEventFactory();

	/**
	 * The event factory instance for mapping a result object to an event, using the type of the result object as the
	 * mapping criteria.
	 */
	private ResultObjectBasedEventFactory resultObjectBasedEventFactory = new ResultObjectBasedEventFactory();

	/**
	 * Select the appropriate result event factory for attempts to invoke the given method.
	 * @param method the method
	 * @return the result event factory
	 */
	public ResultEventFactory forMethod(Method method) {
		return forType(method.getReturnType());
	}

	/**
	 * Select the appropriate result event factory for the given result.
	 * @param result the result
	 * @return the result event factory
	 */
	public ResultEventFactory forResult(Object result) {
		if (result == null) {
			return successEventFactory;
		} else {
			return forType(result.getClass());
		}
	}

	/**
	 * Select the appropriate result event factory for given result type. This implementation returns
	 * {@link ResultObjectBasedEventFactory} if the type is
	 * {@link ResultObjectBasedEventFactory#isMappedValueType(Class) mapped} by that result event factory, otherwise
	 * {@link SuccessEventFactory} is returned.
	 * @param resultType the result type
	 * @return the result event factory
	 */
	protected ResultEventFactory forType(Class resultType) {
		if (resultObjectBasedEventFactory.isMappedValueType(resultType)) {
			return resultObjectBasedEventFactory;
		} else {
			return successEventFactory;
		}
	}
}