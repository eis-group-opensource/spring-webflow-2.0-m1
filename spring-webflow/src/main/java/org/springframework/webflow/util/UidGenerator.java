/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.util;

import java.io.Serializable;

/**
 * A strategy for generating ids for uniquely identifying execution artifacts such as FlowExecutions and any other
 * uniquely identified flow artifact.
 * 
 * @author Keith Donald
 */
public interface UidGenerator {

	/**
	 * Generate a new unique id.
	 * @return a serializable id, guaranteed to be unique in some context
	 */
	public Serializable generateUid();

	/**
	 * Convert the string-encoded uid into its original object form.
	 * @param encodedUid the string encoded uid
	 * @return the converted uid
	 */
	public Serializable parseUid(String encodedUid);
}
