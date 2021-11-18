/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation.impl;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * A singleton lock that doesn't do anything. For use when conversations don't require or choose not to implement
 * locking.
 * 
 * @author Keith Donald
 */
class NoOpConversationLock implements ConversationLock, Serializable {

	/**
	 * The singleton instance.
	 */
	public static final NoOpConversationLock INSTANCE = new NoOpConversationLock();

	/**
	 * Private constructor to avoid instantiation.
	 */
	private NoOpConversationLock() {
	}

	public void lock() {
		// no-op
	}

	public void unlock() {
		// no-op
	}

	// resolve the singleton instance
	private Object readResolve() throws ObjectStreamException {
		return INSTANCE;
	}
}