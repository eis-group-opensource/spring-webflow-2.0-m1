/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationParameters;
import org.springframework.webflow.conversation.NoSuchConversationException;

/**
 * Container for conversations that is stored in the session. When the session expires this container will go with it,
 * implicitly expiring all contained conversations.
 * <p>
 * This is an internal helper class of the {@link SessionBindingConversationManager}.
 * 
 * @author Erwin Vervaet
 */
class ConversationContainer implements Serializable {

	/**
	 * Maximum number of conversations in this container. -1 for unlimited.
	 */
	private int maxConversations;

	/**
	 * The key of this conversation container in the session.
	 */
	private String sessionKey;

	/**
	 * The contained conversations. A list of {@link ContainedConversation} objects.
	 */
	private List conversations;

	/**
	 * Create a new conversation container.
	 * @param maxConversations the maximum number of allowed concurrent conversations, -1 for unlimited
	 * @param sessionKey the key of this conversation container in the session
	 */
	public ConversationContainer(int maxConversations, String sessionKey) {
		this.maxConversations = maxConversations;
		this.sessionKey = sessionKey;
		this.conversations = new ArrayList();
	}

	/**
	 * Returns the key of this conversation container in the session. For package level use only.
	 */
	String getSessionKey() {
		return sessionKey;
	}

	/**
	 * Returns the current size of the conversation container: the number of conversations contained within it.
	 */
	public int size() {
		return conversations.size();
	}

	/**
	 * Create a new conversation based on given parameters and add it to the container.
	 * @param id the unique id of the conversation
	 * @param parameters descriptive parameters
	 * @return the created conversation
	 */
	public synchronized Conversation createAndAddConversation(ConversationId id, ConversationParameters parameters) {
		ContainedConversation conversation = new ContainedConversation(this, id);
		conversations.add(conversation);
		if (maxExceeded()) {
			// end oldest conversation
			((Conversation) conversations.get(0)).end();
		}
		return conversation;
	}

	/**
	 * Return the identified conversation.
	 * @param id the id to lookup
	 * @return the conversation
	 * @throws NoSuchConversationException if the conversation cannot be found
	 */
	public synchronized Conversation getConversation(ConversationId id) throws NoSuchConversationException {
		for (Iterator it = conversations.iterator(); it.hasNext();) {
			ContainedConversation conversation = (ContainedConversation) it.next();
			if (conversation.getId().equals(id)) {
				return conversation;
			}
		}
		throw new NoSuchConversationException(id);
	}

	/**
	 * Remove identified conversation from this container.
	 */
	public synchronized void removeConversation(ConversationId id) {
		for (Iterator it = conversations.iterator(); it.hasNext();) {
			ContainedConversation conversation = (ContainedConversation) it.next();
			if (conversation.getId().equals(id)) {
				it.remove();
				break;
			}
		}
	}

	/**
	 * Has the maximum number of allowed concurrent conversations in the session been exceeded?
	 */
	private boolean maxExceeded() {
		return maxConversations > 0 && conversations.size() > maxConversations;
	}
}