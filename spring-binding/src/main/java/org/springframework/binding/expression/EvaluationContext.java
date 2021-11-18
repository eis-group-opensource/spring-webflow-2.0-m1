/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression;

import java.util.Map;

/**
 * A context object with two main responsibities:
 * <ol>
 * <li>Exposing information to an expression to influence an evaluation attempt.
 * <li>Providing operations for recording progress or errors during the expression evaluation process.
 * </ol>
 * 
 * @author Keith Donald
 */
public interface EvaluationContext {

	/**
	 * Returns a map of attributes that can be used to influence expression evaluation.
	 * @return the evaluation attributes
	 */
	public Map getAttributes();

}