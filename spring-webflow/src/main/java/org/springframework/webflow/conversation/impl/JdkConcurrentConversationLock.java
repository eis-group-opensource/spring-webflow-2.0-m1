/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation.impl;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A conversation lock that relies on a {@link ReentrantLock} within Java 5's <code>util.concurrent.locks</code>
 * package.
 * 
 * @author Keith Donald
 */
class JdkConcurrentConversationLock implements ConversationLock, Serializable {

	/**
	 * The lock.
	 */
	private Lock lock = new ReentrantLock();

	public void lock() {
		lock.lock();
	}

	public void unlock() {
		lock.unlock();
	}
}