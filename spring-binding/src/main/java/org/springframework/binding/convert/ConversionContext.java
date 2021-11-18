/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert;

/**
 * A context object with two main responsibities:
 * <ol>
 * <li>Exposing information to a converter to influence a type conversion attempt.
 * <li>Providing operations for recording progress or errors during the type conversion process.
 * </ol>
 * Empty for now; subclasses may define their own custom context behavior accessible by a converter with a downcast.
 * 
 * @author Keith Donald
 */
public interface ConversionContext {

}
