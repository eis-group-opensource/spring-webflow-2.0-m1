/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config;

import org.springframework.core.enums.StaticLabeledEnum;
import org.springframework.webflow.execution.repository.continuation.ClientContinuationFlowExecutionRepository;
import org.springframework.webflow.execution.repository.continuation.ContinuationFlowExecutionRepository;
import org.springframework.webflow.execution.repository.support.SimpleFlowExecutionRepository;

/**
 * Type-safe enumeration of logical flow execution repository types.
 * 
 * @see org.springframework.webflow.execution.repository.FlowExecutionRepository
 * 
 * @author Keith Donald
 */
public class RepositoryType extends StaticLabeledEnum {

	/**
	 * The 'simple' flow execution repository type.
	 * @see SimpleFlowExecutionRepository
	 */
	public static final RepositoryType SIMPLE = new RepositoryType(0, "Simple");

	/**
	 * The 'continuation' flow execution repository type.
	 * @see ContinuationFlowExecutionRepository
	 */
	public static final RepositoryType CONTINUATION = new RepositoryType(1, "Continuation");

	/**
	 * The 'client' (continuation) flow execution repository type.
	 * @see ClientContinuationFlowExecutionRepository
	 */
	public static final RepositoryType CLIENT = new RepositoryType(2, "Client");

	/**
	 * The 'singleKey' flow execution repository type.
	 * @see SimpleFlowExecutionRepository
	 * @see SimpleFlowExecutionRepository#setAlwaysGenerateNewNextKey(boolean)
	 */
	public static final RepositoryType SINGLEKEY = new RepositoryType(3, "Single Key");

	/**
	 * Private constructor because this is a typesafe enum!
	 */
	private RepositoryType(int code, String label) {
		super(code, label);
	}
}