/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.JdkVersion;

/**
 * Simple utility class for creating conversation lock instances based on the current execution environment.
 * 
 * @author Keith Donald
 * @author Rob Harrop
 */
public class ConversationLockFactory {

	private static final Log logger = LogFactory.getLog(ConversationLockFactory.class);

	private static boolean utilConcurrentPresent;

	static {
		try {
			Class.forName("EDU.oswego.cs.dl.util.concurrent.ReentrantLock");
			utilConcurrentPresent = true;
		} catch (ClassNotFoundException ex) {
			utilConcurrentPresent = false;
		}
	}

	/**
	 * When running on Java 1.5+, returns a jdk5 concurrent lock. When running on older JDKs with the 'util.concurrent'
	 * package available, returns a util concurrent lock. In all other cases a "no-op" lock is returned.
	 */
	public static ConversationLock createLock() {
		if (JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_15) {
			return new JdkConcurrentConversationLock();
		} else if (utilConcurrentPresent) {
			return new UtilConcurrentConversationLock();
		} else {
			logger.warn("Unable to enable conversation locking. Switch to Java 5 or above, "
					+ "or put the 'util.concurrent' package on the classpath "
					+ "to enable locking in your environment.");
			return NoOpConversationLock.INSTANCE;
		}
	}
}