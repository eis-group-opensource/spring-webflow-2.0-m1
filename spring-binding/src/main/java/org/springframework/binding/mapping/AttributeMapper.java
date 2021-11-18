/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.mapping;

/**
 * A lightweight service interface for mapping between two attribute sources.
 * <p>
 * Implementations of this interface are expected to encapsulate the mapping configuration information as well as the
 * logic to act on it to perform mapping between a given source and target attribute source.
 * 
 * @author Keith Donald
 */
public interface AttributeMapper {

	/**
	 * Map data from a source object to a target object.
	 * @param source the source
	 * @param target the target
	 * @param context the mapping context
	 */
	public void map(Object source, Object target, MappingContext context);
}