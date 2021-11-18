/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Converts a textual representation of a class object to a <code>Class</code> instance.
 * 
 * @author Keith Donald
 */
public class TextToClass extends ConversionServiceAwareConverter {

	private static final String ALIAS_PREFIX = "type:";

	private static final String CLASS_PREFIX = "class:";

	public Class[] getSourceClasses() {
		return new Class[] { String.class };
	}

	public Class[] getTargetClasses() {
		return new Class[] { Class.class };
	}

	protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
		String text = (String) source;
		if (StringUtils.hasText(text)) {
			String classNameOrAlias = text.trim();
			if (classNameOrAlias.startsWith(CLASS_PREFIX)) {
				return ClassUtils.forName(text.substring(CLASS_PREFIX.length()));
			} else if (classNameOrAlias.startsWith(ALIAS_PREFIX)) {
				String alias = text.substring(ALIAS_PREFIX.length());
				Class clazz = getConversionService().getClassByAlias(alias);
				Assert.notNull(clazz, "No class found associated with type alias '" + alias + "'");
				return clazz;
			} else {
				// try first an aliased based lookup
				if (getConversionService() != null) {
					Class aliasedClass = getConversionService().getClassByAlias(classNameOrAlias);
					if (aliasedClass != null) {
						return aliasedClass;
					}
				}
				// treat as a class name
				return ClassUtils.forName(classNameOrAlias);
			}
		} else {
			return null;
		}
	}
}