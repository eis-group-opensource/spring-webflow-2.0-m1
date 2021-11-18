/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.support;

import java.io.Serializable;

import org.springframework.util.Assert;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.execution.repository.BadlyFormattedFlowExecutionKeyException;
import org.springframework.webflow.execution.repository.FlowExecutionKey;
import org.springframework.webflow.execution.repository.continuation.FlowExecutionContinuation;

/**
 * A flow execution key consisting of two parts:
 * <ol>
 * <li>A <i>conversationId</i>, identifying an active conversation managed by a {@link ConversationManager}.
 * <li>A <i>continuationId</i>, identifying a restorable {@link FlowExecutionContinuation} within a continuation group
 * governed by that conversation.
 * </ol>
 * <p>
 * This key is used to restore a FlowExecution from a conversation-service backed store.
 * 
 * @see ConversationManager
 * @see FlowExecutionContinuation
 * 
 * @author Keith Donald
 */
class CompositeFlowExecutionKey extends FlowExecutionKey {

	/**
	 * The default conversation id prefix delimiter ("_c").
	 */
	private static final String CONVERSATION_ID_PREFIX = "_c";

	/**
	 * The default continuation id prefix delimiter ("_k").
	 */
	private static final String CONTINUATION_ID_PREFIX = "_k";

	/**
	 * The format of the default string-encoded form, as returned by toString().
	 */
	private static final String FORMAT = CONVERSATION_ID_PREFIX + "<conversationId>" + CONTINUATION_ID_PREFIX
			+ "<continuationId>";

	/**
	 * The conversation id.
	 */
	private ConversationId conversationId;

	/**
	 * The continuation id.
	 */
	private Serializable continuationId;

	/**
	 * Create a new composite flow execution key given the composing parts.
	 * @param conversationId the conversation id
	 * @param continuationId the continuation id
	 */
	public CompositeFlowExecutionKey(ConversationId conversationId, Serializable continuationId) {
		Assert.notNull(conversationId, "The conversation id is required");
		Assert.notNull(continuationId, "The continuation id is required");
		this.conversationId = conversationId;
		this.continuationId = continuationId;
	}

	/**
	 * Returns the conversation id.
	 */
	public ConversationId getConversationId() {
		return conversationId;
	}

	/**
	 * Returns the continuation id.
	 */
	public Serializable getContinuationId() {
		return continuationId;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof CompositeFlowExecutionKey)) {
			return false;
		}
		CompositeFlowExecutionKey other = (CompositeFlowExecutionKey) obj;
		return conversationId.equals(other.conversationId) && continuationId.equals(other.continuationId);
	}

	public int hashCode() {
		return conversationId.hashCode() + continuationId.hashCode();
	}

	public String toString() {
		return new StringBuffer().append(CONVERSATION_ID_PREFIX).append(getConversationId()).append(
				CONTINUATION_ID_PREFIX).append(getContinuationId()).toString();
	}

	// static helpers

	/**
	 * Helper that splits the string-form of an instance of this class into its "parts" so the parts can be easily
	 * parsed.
	 * @param encodedKey the string-encoded composite flow execution key
	 * @return the composite key parts as a String array (conversationId = 0, continuationId = 1)
	 */
	public static String[] keyParts(String encodedKey) throws BadlyFormattedFlowExecutionKeyException {
		if (!encodedKey.startsWith(CONVERSATION_ID_PREFIX)) {
			throw new BadlyFormattedFlowExecutionKeyException(encodedKey, FORMAT);
		}
		int continuationStart = encodedKey.indexOf(CONTINUATION_ID_PREFIX, CONVERSATION_ID_PREFIX.length());
		if (continuationStart == -1) {
			throw new BadlyFormattedFlowExecutionKeyException(encodedKey, FORMAT);
		}
		String conversationId = encodedKey.substring(CONVERSATION_ID_PREFIX.length(), continuationStart);
		String continuationId = encodedKey.substring(continuationStart + CONTINUATION_ID_PREFIX.length());
		return new String[] { conversationId, continuationId };
	}
}