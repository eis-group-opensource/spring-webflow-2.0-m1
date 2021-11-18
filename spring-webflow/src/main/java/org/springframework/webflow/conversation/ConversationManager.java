/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.conversation;

/**
 * A service for managing conversations. This interface is the entry point into the conversation subsystem.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface ConversationManager {

	/**
	 * Begin a new conversation.
	 * @param conversationParameters descriptive conversation parameters
	 * @return a service interface allowing access to the conversation context
	 * @throws ConversationException an exception occured
	 */
	public Conversation beginConversation(ConversationParameters conversationParameters) throws ConversationException;

	/**
	 * Get the conversation with the provided id.
	 * <p>
	 * Implementors should take care to manage conversation identity correctly. Although it is not strictly required to
	 * return the same (==) Conversation object every time this method is called with a particular conversation id in a
	 * single execution thread, callers will expect to recieve an object that allows them to manipulate the identified
	 * conversation. In other words, the following is legal ConversationManager client code:
	 * 
	 * <pre>
	 * 	ConversationManager manager = ...;
	 * 	ConversationId id = ...;
	 * 	Conversation conv = manager.getConversation(id);
	 *  conv.lock();
	 *  try {
	 *  	Conversation localReference = manager.getConversation(id);
	 *  	// no need to lock since conversation 'id' is already locked
	 *  	// even though possibly conv != localReference
	 *  	localReference.putAttribute(&quot;foo&quot;, &quot;bar&quot;);
	 *  	Object foo = conv.getAttribute(&quot;foo&quot;);
	 * 	}
	 * 	finally {
	 * 		conv.unlock();
	 * 	}
	 * </pre>
	 * 
	 * @param id the conversation id
	 * @return the conversation
	 * @throws NoSuchConversationException the id provided was invalid
	 */
	public Conversation getConversation(ConversationId id) throws ConversationException;

	/**
	 * Parse the string-encoded conversationId into its object form. Essentially, the reverse of
	 * {@link ConversationId#toString()}.
	 * @param encodedId the encoded id
	 * @return the parsed conversation id
	 * @throws ConversationException an exception occured parsing the id
	 */
	public ConversationId parseConversationId(String encodedId) throws ConversationException;
}