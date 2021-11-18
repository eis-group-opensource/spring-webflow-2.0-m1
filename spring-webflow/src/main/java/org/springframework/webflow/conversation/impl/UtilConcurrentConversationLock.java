/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation.impl;

import org.springframework.core.NestedRuntimeException;

import EDU.oswego.cs.dl.util.concurrent.ReentrantLock;

/**
 * A conversation lock that relies on a {@link ReentrantLock} within Doug Lea's <a
 * href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html">util.concurrent</a> package.
 * For use on JDK 1.3 and 1.4.
 * 
 * @author Keith Donald
 * @author Rob Harrop
 */
class UtilConcurrentConversationLock implements ConversationLock {

	/**
	 * The {@link ReentrantLock} instance.
	 */
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * Acquires the lock.
	 * @throws SystemInterruptedException if the lock cannot be acquired due to interruption
	 */
	public void lock() {
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			throw new SystemInterruptedException("Unable to acquire lock.", e);
		}
	}

	/**
	 * Releases the lock.
	 */
	public void unlock() {
		lock.release();
	}

	/**
	 * <code>Exception</code> indicating that some {@link Thread} was {@link Thread#interrupt() interrupted} during
	 * processing and as such processing was halted.
	 * <p>
	 * Only used to wrap the checked {@link InterruptedException java.lang.InterruptedException}.
	 */
	public static class SystemInterruptedException extends NestedRuntimeException {

		/**
		 * Creates a new <code>SystemInterruptedException</code>.
		 * @param msg the <code>Exception</code> message
		 */
		public SystemInterruptedException(String msg) {
			super(msg);
		}

		/**
		 * Creates a new <code>SystemInterruptedException</code>.
		 * @param msg the <code>Exception</code> message
		 * @param cause the root cause of this <code>Exception</code>
		 */
		public SystemInterruptedException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
}