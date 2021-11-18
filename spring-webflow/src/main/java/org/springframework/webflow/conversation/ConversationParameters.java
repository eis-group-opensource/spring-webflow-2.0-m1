/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;

/**
 * Simple parameter object for clumping together input needed to begin a new conversation.
 * 
 * @author Keith Donald
 */
public class ConversationParameters implements Serializable {

	/**
	 * The conversation name.
	 */
	private String name;

	/**
	 * The conversation caption.
	 */
	private String caption;

	/**
	 * The conversation description.
	 */
	private String description;

	/**
	 * Creates new conversation input parameters.
	 * @param name the name of the conversation
	 * @param caption a short description
	 * @param description a long description
	 */
	public ConversationParameters(String name, String caption, String description) {
		this.name = name;
		this.caption = caption;
		this.description = description;
	}

	/**
	 * Returns the name of the conversation.
	 * @return the conversation name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the short description.
	 * @return the conversation caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Returns the long description.
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	public String toString() {
		return new ToStringCreator(this).append("name", name).toString();
	}
}