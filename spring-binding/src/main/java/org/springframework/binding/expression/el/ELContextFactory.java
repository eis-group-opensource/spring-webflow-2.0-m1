/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.el;

import javax.el.ELContext;
import javax.el.ELResolver;

/**
 * A factory for creating a EL context object that will be used to evaluate a target object of an EL expression.
 * 
 * @author Jeremy Grelle
 */
public interface ELContextFactory {

	/**
	 * Configures and returns an {@link ELContext} to be used in parsing EL expressions.
	 * @return ELContext The configured ELContext instance for parsing expressions.
	 */
	public ELContext getParseContext();

	/**
	 * Configures and returns an {@link ELContext} to be used in evaluating EL expressions on the given base target
	 * object. In certain environments the target will be null and the base object of the expression is expected to be
	 * resolved via the ELContext's {@link ELResolver} chain.
	 * @param target The base object for the expression evaluation.
	 * @return ELContext The configured ELContext instance for evaluating expressions.
	 */
	public ELContext getEvaluationContext(Object target);

}