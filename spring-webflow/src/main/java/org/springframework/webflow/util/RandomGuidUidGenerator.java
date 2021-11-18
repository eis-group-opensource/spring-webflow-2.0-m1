/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.util;

import java.io.Serializable;

/**
 * A key generator that uses the RandomGuid support class. The default implementation used by the webflow system.
 * 
 * @author Keith Donald
 */
public class RandomGuidUidGenerator implements UidGenerator, Serializable {

	/**
	 * Should the random GUID generated be secure?
	 */
	private boolean secure;

	/**
	 * Returns whether or not the generated random numbers are <i>secure</i>, meaning cryptographically strong.
	 */
	public boolean isSecure() {
		return secure;
	}

	/**
	 * Sets whether or not the generated random numbers should be <i>secure</i>. If set to true, generated GUIDs are
	 * cryptographically strong.
	 */
	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public Serializable generateUid() {
		return new RandomGuid(secure).toString();
	}

	public Serializable parseUid(String encodedUid) {
		return encodedUid;
	}
}