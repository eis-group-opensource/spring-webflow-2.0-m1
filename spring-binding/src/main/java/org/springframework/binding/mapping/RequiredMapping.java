/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.mapping;

import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.SettableExpression;

/**
 * A mapping that is required.
 * 
 * @author Keith Donald
 */
public class RequiredMapping extends Mapping {

	/**
	 * Creates a required mapping.
	 * @param sourceExpression the source mapping expression
	 * @param targetPropertyExpression the target property expression
	 * @param typeConverter a type converter
	 */
	public RequiredMapping(Expression sourceExpression, SettableExpression targetPropertyExpression,
			ConversionExecutor typeConverter) {
		super(sourceExpression, targetPropertyExpression, typeConverter, true);
	}
}
