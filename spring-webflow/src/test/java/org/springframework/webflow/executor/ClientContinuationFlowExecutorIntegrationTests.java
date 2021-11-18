/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor;

public class ClientContinuationFlowExecutorIntegrationTests extends FlowExecutorIntegrationTests {
	protected String[] getConfigLocations() {
		return new String[] { "org/springframework/webflow/executor/context.xml",
				"org/springframework/webflow/executor/repository-client.xml" };
	}
}