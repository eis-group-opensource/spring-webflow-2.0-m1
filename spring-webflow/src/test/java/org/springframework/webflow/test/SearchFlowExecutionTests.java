/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.MappingContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.definition.registry.FlowDefinitionResource;
import org.springframework.webflow.engine.EndState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

/**
 * Sample {@link AbstractXmlFlowExecutionTests} subclass.
 */
public class SearchFlowExecutionTests extends AbstractXmlFlowExecutionTests {

	public void testStartFlow() {
		ApplicationView view = applicationView(startFlow());
		assertCurrentStateEquals("enterCriteria");
		assertViewNameEquals("searchCriteria", view);
		assertModelAttributeNotNull("searchCriteria", view);
	}

	public void testCriteriaSubmitSuccess() {
		startFlow();
		MockParameterMap parameters = new MockParameterMap();
		parameters.put("firstName", "Keith");
		parameters.put("lastName", "Donald");
		ApplicationView view = applicationView(signalEvent("search", parameters));
		assertCurrentStateEquals("displayResults");
		assertViewNameEquals("searchResults", view);
		assertModelAttributeCollectionSize(1, "results", view);
	}

	public void testNewSearch() {
		testCriteriaSubmitSuccess();
		ApplicationView view = applicationView(signalEvent("newSearch"));
		assertCurrentStateEquals("enterCriteria");
		assertViewNameEquals("searchCriteria", view);
	}

	public void testSelectValidResult() {
		testCriteriaSubmitSuccess();
		MockParameterMap parameters = new MockParameterMap();
		parameters.put("id", "1");
		ApplicationView view = applicationView(signalEvent("select", parameters));
		assertCurrentStateEquals("displayResults");
		assertViewNameEquals("searchResults", view);
		assertModelAttributeCollectionSize(1, "results", view);
	}

	protected FlowDefinitionResource getFlowDefinitionResource() {
		return new FlowDefinitionResource("search-flow", new ClassPathResource("search-flow.xml",
				SearchFlowExecutionTests.class));
	}

	protected void registerMockServices(MockFlowServiceLocator serviceRegistry) {
		Flow mockDetailFlow = new Flow("detail-flow");
		mockDetailFlow.setInputMapper(new AttributeMapper() {
			public void map(Object source, Object target, MappingContext context) {
				assertEquals("id of value 1 not provided as input by calling search flow", new Long(1),
						((AttributeMap) source).get("id"));
			}
		});
		// test responding to finish result
		new EndState(mockDetailFlow, "finish");

		serviceRegistry.registerSubflow(mockDetailFlow);
		serviceRegistry.registerBean("phonebook", new TestPhoneBook());
	}

	public static class TestPhoneBook {

		public List search(Object criteria) {
			ArrayList res = new ArrayList();
			res.add(new Object());
			return res;
		}

		public Object getPerson(Long id) {
			return new Object();
		}

		public Object getPerson(String userId) {
			return new Object();
		}

	}

}