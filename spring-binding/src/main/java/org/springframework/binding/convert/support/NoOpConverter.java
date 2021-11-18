/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.convert.ConversionContext;

/**
 * Package private converter that is a "no op".
 * 
 * @author Keith Donald
 */
class NoOpConverter extends AbstractConverter {

	private Class sourceClass;

	private Class targetClass;

	/**
	 * Create a "no op" converter from given source to given target class.
	 */
	public NoOpConverter(Class sourceClass, Class targetClass) {
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
		return source;
	}

	public Class[] getSourceClasses() {
		return new Class[] { sourceClass };
	}

	public Class[] getTargetClasses() {
		return new Class[] { targetClass };
	}
}