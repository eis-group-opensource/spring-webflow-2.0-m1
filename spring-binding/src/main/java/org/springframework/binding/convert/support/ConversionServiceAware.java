/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.convert.ConversionService;

/**
 * Marker interface that denotes an object has a dependency on a conversion service that is expected to be fulfilled.
 * 
 * @author Keith Donald
 */
public interface ConversionServiceAware {

	/**
	 * Set the conversion service this object should be made aware of (as it presumably depends on it).
	 * 
	 * @param conversionService the conversion service
	 */
	public void setConversionService(ConversionService conversionService);
}