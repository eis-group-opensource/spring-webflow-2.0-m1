/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.test.execution;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.builder.FlowBuilder;
import org.springframework.webflow.engine.builder.FlowServiceLocator;
import org.springframework.webflow.engine.builder.xml.XmlFlowBuilder;

/**
 * Base class for flow integration tests that verify an XML flow definition executes as expected.
 * <p>
 * Example usage:
 * 
 * <pre>
 * public class SearchFlowExecutionTests extends AbstractXmlFlowExecutionTests {
 * 
 * 	protected FlowDefinitionResource getFlowDefinitionResource() {
 * 		return createFlowDefinitionResource(&quot;src/main/webapp/WEB-INF/flows/search-flow.xml&quot;);
 * 	}
 * 
 * 	public void testStartFlow() {
 * 		startFlow();
 * 		assertCurrentStateEquals(&quot;displaySearchCriteria&quot;);
 * 	}
 * 
 * 	public void testDisplayCriteriaSubmitSuccess() {
 * 		startFlow();
 * 		MockParameterMap parameters = new MockParameterMap();
 * 		parameters.put(&quot;firstName&quot;, &quot;Keith&quot;);
 * 		parameters.put(&quot;lastName&quot;, &quot;Donald&quot;);
 * 		ViewSelection view = signalEvent(&quot;search&quot;, parameters);
 * 		assertCurrentStateEquals(&quot;displaySearchResults&quot;);
 * 		assertModelAttributeCollectionSize(1, &quot;results&quot;, view);
 * 	}
 * }
 * </pre>
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class AbstractXmlFlowExecutionTests extends AbstractExternalizedFlowExecutionTests {

	/**
	 * Constructs a default XML flow execution test.
	 * @see #setName(String)
	 */
	public AbstractXmlFlowExecutionTests() {
		super();
	}

	/**
	 * Constructs an XML flow execution test with given name.
	 * @param name the name of the test
	 * @since 1.0.2
	 */
	public AbstractXmlFlowExecutionTests(String name) {
		super(name);
	}

	protected FlowBuilder createFlowBuilder(Resource resource, FlowServiceLocator flowServiceLocator) {
		return new XmlFlowBuilder(resource, flowServiceLocator) {
			protected void registerLocalBeans(Flow flow, ConfigurableBeanFactory beanFactory) {
				registerLocalMockServices(flow, beanFactory);
			}
		};
	}

	/**
	 * Template method subclasses may override to register mock implementations of services used locally by the flow
	 * being tested.
	 * @param flow the flow to register the services for
	 * @param beanFactory the local flow service registry; register mock services with it using
	 * {@link ConfigurableBeanFactory#registerSingleton(String, Object)}
	 */
	protected void registerLocalMockServices(Flow flow, ConfigurableBeanFactory beanFactory) {
	}
}