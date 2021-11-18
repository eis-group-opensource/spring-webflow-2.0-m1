/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.mapping;

/**
 * Thrown when a required mapping could not be performed.
 * 
 * @author Keith Donald
 */
public class RequiredMappingException extends IllegalStateException {

	/**
	 * Create a new required mapping exception.
	 * @param message a descriptive message
	 */
	public RequiredMappingException(String message) {
		super(message);
	}
}
