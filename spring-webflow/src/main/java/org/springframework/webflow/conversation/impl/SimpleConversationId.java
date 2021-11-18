/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation.impl;

import java.io.Serializable;

import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationManager;

/**
 * An id that uniquely identifies a conversation managed by a {@link ConversationManager}.
 * <p>
 * This key consists of a unique string that is typically a GUID.
 * 
 * @author Ben Hale
 */
public class SimpleConversationId extends ConversationId {

	/**
	 * The id value.
	 */
	private Serializable id;

	/**
	 * Creates a new simple conversation id.
	 * @param id the id value
	 */
	public SimpleConversationId(Serializable id) {
		this.id = id;
	}

	public boolean equals(Object o) {
		if (!(o instanceof SimpleConversationId)) {
			return false;
		}
		return id.equals(((SimpleConversationId) o).id);
	}

	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
		return id.toString();
	}
}