/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import org.springframework.binding.convert.support.GenericConversionService;
import org.springframework.binding.convert.support.TextToBoolean;
import org.springframework.webflow.engine.NullViewSelector;
import org.springframework.webflow.engine.ViewSelector;

import junit.framework.TestCase;

/**
 * Test case for the {@link BaseFlowServiceLocator}.
 * 
 * @author Erwin Vervaet
 */
public class BaseFlowServiceLocatorTests extends TestCase {

	public void testWithCustomConversionService() {
		BaseFlowServiceLocator serviceLocator = new BaseFlowServiceLocator();

		GenericConversionService conversionService = new GenericConversionService();
		conversionService.addConverter(new TextToBoolean("ja", "nee"));
		conversionService.addConverter(new CustomTextToViewSelector(serviceLocator));

		serviceLocator.setConversionService(conversionService);

		assertEquals(Boolean.TRUE, serviceLocator.getConversionService().getConversionExecutor(String.class,
				Boolean.class).execute("ja"));
		assertSame(NullViewSelector.INSTANCE, serviceLocator.getConversionService().getConversionExecutor(String.class,
				ViewSelector.class).execute("custom:"));
	}

	public static class CustomTextToViewSelector extends TextToViewSelector {

		public CustomTextToViewSelector(FlowServiceLocator flowServiceLocator) {
			super(flowServiceLocator);
		}

		protected ViewSelector convertEncodedViewSelector(String encodedView) {
			return NullViewSelector.INSTANCE;
		}
	}
}
