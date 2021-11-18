/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.factory;

import org.springframework.core.style.StylerUtils;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.webflow.definition.FlowDefinition;

/**
 * Static factory for creating commonly used flow execution listener criteria.
 * 
 * @see FlowExecutionListenerCriteria
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowExecutionListenerCriteriaFactory {

	/**
	 * Returns a wild card criteria that matches all flows.
	 */
	public FlowExecutionListenerCriteria allFlows() {
		return new WildcardFlowExecutionListenerCriteria();
	}

	/**
	 * Returns a criteria that just matches a flow with the specified id.
	 * @param flowId the flow id to match
	 */
	public FlowExecutionListenerCriteria flow(String flowId) {
		return new FlowIdFlowExecutionListenerCriteria(flowId);
	}

	/**
	 * Returns a criteria that just matches a flow if it is identified by one of the specified ids.
	 * @param flowIds the flow ids to match
	 */
	public FlowExecutionListenerCriteria flows(String[] flowIds) {
		return new FlowIdFlowExecutionListenerCriteria(flowIds);
	}

	/**
	 * A flow execution listener criteria implementation that matches for all flows.
	 */
	private static class WildcardFlowExecutionListenerCriteria implements FlowExecutionListenerCriteria {

		public boolean appliesTo(FlowDefinition definition) {
			return true;
		}

		public String toString() {
			return "*";
		}
	}

	/**
	 * A flow execution listener criteria implementation that matches flows with a specified id.
	 */
	private static class FlowIdFlowExecutionListenerCriteria implements FlowExecutionListenerCriteria {

		/**
		 * The flow ids that apply for this criteria.
		 */
		private String[] flowIds;

		/**
		 * Create a new flow id matching flow execution listener criteria implemenation.
		 * @param flowId the flow id to match
		 */
		public FlowIdFlowExecutionListenerCriteria(String flowId) {
			Assert.notNull(flowId, "The flow id is required");
			this.flowIds = new String[] { flowId };
		}

		/**
		 * Create a new flow id matching flow execution listener criteria implemenation.
		 * @param flowIds the flow ids to match
		 */
		public FlowIdFlowExecutionListenerCriteria(String[] flowIds) {
			Assert.notEmpty(flowIds, "The flow id array is required");
			this.flowIds = flowIds;
		}

		public boolean appliesTo(FlowDefinition definition) {
			for (int i = 0; i < flowIds.length; i++) {
				if (flowIds[i].equals(definition.getId())) {
					return true;
				}
			}
			return false;
		}

		public String toString() {
			return new ToStringCreator(this).append("flowIds", StylerUtils.style(flowIds)).toString();
		}
	}
}