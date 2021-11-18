/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

/**
 * Thrown when an encoded flow execution key is badly formatted and could not be parsed.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class BadlyFormattedFlowExecutionKeyException extends FlowExecutionRepositoryException {

	/**
	 * The string encoded flow execution key that was invalid.
	 */
	private String invalidKey;

	/**
	 * The format the string key should have been in. Could just be a description of that format.
	 */
	private String format;

	/**
	 * Creates a bad execution key format exception.
	 * @param invalidKey the invalid key
	 * @param format the format the key should have been in
	 */
	public BadlyFormattedFlowExecutionKeyException(String invalidKey, String format) {
		super("Badly formatted flow execution key '" + invalidKey + "', the expected format is '" + format + "'");
		this.invalidKey = invalidKey;
		this.format = format;
	}

	/**
	 * Creates a bad execution key format exception.
	 * @param invalidKey the invalid key
	 * @param format the format the key should have been in
	 * @param cause the cause
	 */
	public BadlyFormattedFlowExecutionKeyException(String invalidKey, String format, Throwable cause) {
		super("Badly formatted flow execution key '" + invalidKey + "', the expected format is '" + format + "'", cause);
		this.invalidKey = invalidKey;
		this.format = format;
	}

	/**
	 * Returns the string key of the flow execution that could not be parsed.
	 */
	public String getInvalidKey() {
		return invalidKey;
	}

	/**
	 * Returns the format the key should have been in.
	 */
	public String getFormat() {
		return format;
	}
}