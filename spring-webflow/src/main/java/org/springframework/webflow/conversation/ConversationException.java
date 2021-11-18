/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation;

import org.springframework.webflow.core.FlowException;

/**
 * The root of the conversation service exception hierarchy.
 * 
 * @author Keith Donald
 */
public abstract class ConversationException extends FlowException {

	/**
	 * Creates a conversation service exception.
	 * @param message a descriptive message
	 */
	public ConversationException(String message) {
		super(message);
	}

	/**
	 * Creates a conversation service exception.
	 * @param message a descriptive message
	 * @param cause the root cause of the problem
	 */
	public ConversationException(String message, Throwable cause) {
		super(message, cause);
	}
}