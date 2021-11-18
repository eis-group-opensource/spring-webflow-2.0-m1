/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.core.collection.SharedAttributeMap;

/**
 * Internal {@link Conversation} implementation used by the conversation container.
 * <p>
 * This is an internal helper class of the {@link SessionBindingConversationManager}.
 * 
 * @author Erwin Vervaet
 */
class ContainedConversation implements Conversation, Serializable {

	private static final Log logger = LogFactory.getLog(SessionBindingConversationManager.class);

	private ConversationContainer container;

	private ConversationId id;

	private transient ConversationLock lock;

	private Map attributes;

	/**
	 * Create a new contained conversation.
	 * @param container the container containing the conversation
	 * @param id the unique id assigned to the conversation
	 */
	public ContainedConversation(ConversationContainer container, ConversationId id) {
		this.container = container;
		this.id = id;
		this.lock = ConversationLockFactory.createLock();
		this.attributes = new HashMap();
	}

	public ConversationId getId() {
		return id;
	}

	public void lock() {
		if (logger.isDebugEnabled()) {
			logger.debug("Locking conversation " + id);
		}
		lock.lock();
	}

	public Object getAttribute(Object name) {
		return attributes.get(name);
	}

	public void putAttribute(Object name, Object value) {
		if (logger.isDebugEnabled()) {
			logger.debug("Putting conversation attribute '" + name + "' with value " + value);
		}
		attributes.put(name, value);
	}

	public void removeAttribute(Object name) {
		if (logger.isDebugEnabled()) {
			logger.debug("Removing conversation attribute '" + name + "'");
		}
		attributes.remove(name);
	}

	public void end() {
		if (logger.isDebugEnabled()) {
			logger.debug("Ending conversation " + id);
		}
		container.removeConversation(getId());
	}

	public void unlock() {
		if (logger.isDebugEnabled()) {
			logger.debug("Unlocking conversation " + id);
		}
		lock.unlock();

		// re-bind the conversation container in the session
		// this is required to make session replication work correctly in
		// a clustered environment
		// we do this after releasing the lock since we're no longer
		// manipulating the contents of the conversation
		SharedAttributeMap sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();
		synchronized (sessionMap.getMutex()) {
			sessionMap.put(container.getSessionKey(), container);
		}
	}

	public String toString() {
		return getId().toString();
	}

	// id based equality

	public boolean equals(Object obj) {
		if (!(obj instanceof ContainedConversation)) {
			return false;
		}
		return id.equals(((ContainedConversation) obj).id);
	}

	public int hashCode() {
		return id.hashCode();
	}

	// custom serialisation

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		lock = ConversationLockFactory.createLock();
	}
}