/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.FlowSession;

/**
 * Flow {@link Scope scope} implementation.
 * 
 * @see FlowSession#getScope()
 * 
 * @author Ben Hale
 */
public class FlowScope extends AbstractWebFlowScope {
	protected MutableAttributeMap getScope() {
		return getFlowExecutionContext().getActiveSession().getScope();
	}

	public Object get(String name, ObjectFactory objectFactory) {
		MutableAttributeMap scope = getScope();
		Object scopedObject = scope.get(name);
		if (scopedObject == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("No scoped instance '" + name + "' found; creating new instance");
			}
			scopedObject = objectFactory.getObject();
			scope.put(name, scopedObject);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Returning scoped instance '" + name + "'");
			}
		}
		return scopedObject;
	}

	public Object resolveContextualObject(String key) {
		throw null;
	}
}
