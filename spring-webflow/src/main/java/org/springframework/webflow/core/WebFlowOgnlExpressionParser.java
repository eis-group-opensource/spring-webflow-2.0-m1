/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core;

import java.util.Map;

import ognl.OgnlException;
import ognl.PropertyAccessor;

import org.springframework.binding.collection.MapAdaptable;
import org.springframework.binding.expression.ognl.OgnlExpressionParser;
import org.springframework.webflow.core.collection.MutableAttributeMap;

/**
 * An extension of {@link OgnlExpressionParser} that registers web flow specific property accessors.
 * 
 * @author Keith Donald
 */
class WebFlowOgnlExpressionParser extends OgnlExpressionParser {

	/**
	 * Creates a webflow-specific ognl expression parser.
	 */
	public WebFlowOgnlExpressionParser() {
		addPropertyAccessor(MapAdaptable.class, new MapAdaptablePropertyAccessor());
		addPropertyAccessor(MutableAttributeMap.class, new MutableAttributeMapPropertyAccessor());
	}

	/**
	 * The {@link MapAdaptable} property accessor.
	 * 
	 * @author Keith Donald
	 */
	private static class MapAdaptablePropertyAccessor implements PropertyAccessor {
		public Object getProperty(Map context, Object target, Object name) throws OgnlException {
			return ((MapAdaptable) target).asMap().get(name);
		}

		public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
			throw new UnsupportedOperationException(
					"Cannot mutate immutable attribute collections; operation disallowed");
		}
	}

	/**
	 * The {@link MutableAttributeMap} property accessor.
	 * 
	 * @author Keith Donald
	 */
	private static class MutableAttributeMapPropertyAccessor extends MapAdaptablePropertyAccessor {
		public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
			((MutableAttributeMap) target).put((String) name, value);
		}
	}
}