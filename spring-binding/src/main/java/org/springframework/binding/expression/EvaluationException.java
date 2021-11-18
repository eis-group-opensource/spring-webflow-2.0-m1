/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression;

import org.springframework.core.NestedRuntimeException;

/**
 * Indicates an expression evaluation failed.
 * 
 * @author Keith Donald
 */
public class EvaluationException extends NestedRuntimeException {

	/**
	 * The evaluation attempt that failed. Transient because an EvaluationAttempt is not serializable.
	 */
	private transient EvaluationAttempt evaluationAttempt;

	/**
	 * Creates a new evaluation exception.
	 * @param evaluationAttempt the evaluation attempt that failed
	 * @param cause the underlying cause of this exception
	 */
	public EvaluationException(EvaluationAttempt evaluationAttempt, Throwable cause) {
		super("Expression " + evaluationAttempt
				+ " failed - make sure the expression is evaluatable on the target object", cause);
		this.evaluationAttempt = evaluationAttempt;
	}

	/**
	 * Returns the evaluation attempt that failed.
	 */
	public EvaluationAttempt getEvaluationAttempt() {
		return evaluationAttempt;
	}
}