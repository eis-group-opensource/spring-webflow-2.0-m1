/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import org.springframework.webflow.core.FlowException;

/**
 * Thrown when there is a configuration error with SWF within a JSF environment.
 * 
 * @author Keith Donald
 */
public class JsfFlowConfigurationException extends FlowException {

	public JsfFlowConfigurationException(String msg) {
		super(msg);
	}

	public JsfFlowConfigurationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
