/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation;

import java.io.Serializable;

/**
 * An id that uniquely identifies a conversation managed by a {@link ConversationManager}.
 * 
 * @author Ben Hale
 * @author Keith Donald
 */
public abstract class ConversationId implements Serializable {

	/**
	 * Subclasses should override toString to return a parseable string form of the key.
	 * @see java.lang.Object#toString()
	 * @see ConversationManager#parseConversationId(String)
	 */
	public abstract String toString();
}