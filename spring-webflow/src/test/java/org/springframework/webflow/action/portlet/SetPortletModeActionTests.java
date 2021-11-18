/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action.portlet;

import javax.portlet.PortletMode;

import junit.framework.TestCase;

import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.mock.web.portlet.MockActionResponse;
import org.springframework.mock.web.portlet.MockPortletContext;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.webflow.context.portlet.PortletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit test for the {@link SetPortletModeAction} class.
 * 
 * @author Ulrik Sandberg
 */
public class SetPortletModeActionTests extends TestCase {

	private SetPortletModeAction tested;

	protected void setUp() throws Exception {
		super.setUp();
		tested = new SetPortletModeAction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tested = null;
	}

	public void testDoExecute() throws Exception {
		MockActionResponse mockActionResponse = new MockActionResponse();
		PortletExternalContext externalContext = new PortletExternalContext(new MockPortletContext(),
				new MockActionRequest(), mockActionResponse);
		MockRequestContext mockRequestContext = new MockRequestContext();
		mockRequestContext.setExternalContext(externalContext);

		// perform test
		Event result = tested.doExecute(mockRequestContext);

		assertEquals(tested.getEventFactorySupport().getSuccessEventId(), result.getId());
		assertEquals(tested.getPortletMode(), mockActionResponse.getPortletMode());
	}

	public void testDoExecuteWithPortletModeAsAttribute() throws Exception {
		MockActionResponse mockActionResponse = new MockActionResponse();
		PortletExternalContext externalContext = new PortletExternalContext(new MockPortletContext(),
				new MockActionRequest(), mockActionResponse);
		MockRequestContext mockRequestContext = new MockRequestContext();
		mockRequestContext.setExternalContext(externalContext);
		mockRequestContext.setAttribute(SetPortletModeAction.PORTLET_MODE_ATTRIBUTE, PortletMode.HELP);

		// perform test
		Event result = tested.doExecute(mockRequestContext);

		assertEquals(tested.getEventFactorySupport().getSuccessEventId(), result.getId());
		assertEquals(PortletMode.HELP, mockActionResponse.getPortletMode());
	}

	public void testDoExecuteWithWrongResponseClass() throws Exception {
		MockRenderResponse mockRenderResponse = new MockRenderResponse();
		PortletExternalContext externalContext = new PortletExternalContext(new MockPortletContext(),
				new MockRenderRequest(), mockRenderResponse);
		MockRequestContext mockRequestContext = new MockRequestContext();
		mockRequestContext.setExternalContext(externalContext);
		mockRequestContext.setAttribute(SetPortletModeAction.PORTLET_MODE_ATTRIBUTE, PortletMode.HELP);

		// perform test
		try {
			tested.doExecute(mockRequestContext);
			fail("ActionExecutionException expected");
		} catch (IllegalStateException e) {
			// expected
		}
	}
}
