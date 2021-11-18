/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config;

import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.factory.FlowExecutionListenerCriteria;

/**
 * Dummy test implementation of a FlowExecutionListenerCriteria. Not intended for actual use.
 * 
 * @author Erwin Vervaet
 */
public class TestFlowExecutionListenerCriteria implements FlowExecutionListenerCriteria {

	public boolean appliesTo(FlowDefinition definition) {
		return definition.getAttributes().contains("dummy");
	}

}
