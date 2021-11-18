/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.mapping;

/**
 * A context object with two main responsibities:
 * <ol>
 * <li>Exposing information to a mapper to influence a mapping attempt.
 * <li>Providing operations for recording progress or errors during the mapping process.
 * </ol>
 * Empty for now; subclasses may define their own custom context behavior accessible by a mapper with a downcast.
 * 
 * @author Keith Donald
 */
public interface MappingContext {

}
