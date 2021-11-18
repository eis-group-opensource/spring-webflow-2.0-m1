/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.util.Assert;

/**
 * Registers all 'from string' converters known to a conversion service with a Spring bean factory.
 * <p>
 * Acts as bean factory post processor, registering property editor adapters for each supported conversion with a
 * <code>java.lang.String sourceClass</code>. This makes for very convenient use with the Spring container.
 * 
 * @author Keith Donald
 */
public class CustomConverterConfigurer implements BeanFactoryPostProcessor, InitializingBean {

	private ConversionService conversionService;

	/**
	 * Create a new configurer.
	 * @param conversionService the conversion service to take converters from
	 */
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(conversionService, "The conversion service is required");
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ConversionExecutor[] executors = conversionService.getConversionExecutorsForSource(String.class);
		for (int i = 0; i < executors.length; i++) {
			ConverterPropertyEditorAdapter editor = new ConverterPropertyEditorAdapter(executors[i]);
			beanFactory.registerCustomEditor(editor.getTargetClass(), editor.getClass());
		}
	}
}