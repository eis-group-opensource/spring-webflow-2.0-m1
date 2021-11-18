/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation;

/**
 * Thrown when no logical conversation exists with the specified <code>conversationId</code>. This might occur if the
 * conversation ended, expired, or was otherwise invalidated, but a client view still references it.
 * 
 * @author Keith Donald
 */
public class NoSuchConversationException extends ConversationException {

	/**
	 * The unique conversation identifier that was invalid.
	 */
	private ConversationId conversationId;

	/**
	 * Create a new conversation lookup exception.
	 * @param conversationId the conversation id
	 */
	public NoSuchConversationException(ConversationId conversationId) {
		super("No conversation could be found with id '" + conversationId
				+ "' -- perhaps this conversation has ended? ");
		this.conversationId = conversationId;
	}

	/**
	 * Returns the conversation id that was not found.
	 */
	public ConversationId getConversationId() {
		return conversationId;
	}
}