/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.test;

import java.util.HashMap;

import org.springframework.webflow.core.collection.LocalParameterMap;
import org.springframework.webflow.core.collection.ParameterMap;

/**
 * A extension of parameter map that allows for mutation of parameters. Useful as a stub for testing.
 * 
 * @see ParameterMap
 * 
 * @author Keith Donald
 */
public class MockParameterMap extends LocalParameterMap {

	/**
	 * Creates a new parameter map, initially empty.
	 */
	public MockParameterMap() {
		super(new HashMap());
	}

	/**
	 * Add a new parameter to this map.
	 * @param parameterName the parameter name
	 * @param parameterValue the parameter value
	 * @return this, to support call chaining
	 */
	public MockParameterMap put(String parameterName, String parameterValue) {
		getMapInternal().put(parameterName, parameterValue);
		return this;
	}

	/**
	 * Add a new multi-valued parameter to this map.
	 * @param parameterName the parameter name
	 * @param parameterValues the parameter values
	 * @return this, to support call chaining
	 */
	public MockParameterMap put(String parameterName, String[] parameterValues) {
		getMapInternal().put(parameterName, parameterValues);
		return this;
	}
}