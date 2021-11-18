/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config.scope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

/**
 * Stub implementation for testing the Spring Web Flow scopes.
 * 
 * @author Ben Hale
 */
public class StubObjectFactory implements ObjectFactory {

	private Object value = new Object();

	public Object getObject() throws BeansException {
		return value;
	}

	public Object getValue() {
		return value;
	}

}
