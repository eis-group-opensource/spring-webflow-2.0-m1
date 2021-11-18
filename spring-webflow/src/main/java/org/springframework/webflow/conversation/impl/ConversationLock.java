/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation.impl;

/**
 * A normalized interface for conversation locks, used to obtain exclusive access to a conversation.
 * 
 * @author Keith Donald
 */
public interface ConversationLock {

	/**
	 * Acquire the conversation lock.
	 */
	public void lock();

	/**
	 * Release the conversation lock.
	 */
	public void unlock();
}