/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.util.Assert;

/**
 * A conversion service that delegates to an ordered chain of other conversion services. The first correct reply
 * received from a conversion service in the chain is returned to the caller.
 * 
 * @author Erwin Vervaet
 */
public class CompositeConversionService implements ConversionService {

	private ConversionService[] chain;

	/**
	 * Create a new composite conversion service.
	 * @param conversionServices the conversion services in the chain
	 */
	public CompositeConversionService(ConversionService[] conversionServices) {
		Assert.notNull(conversionServices, "The conversion services chain is required");
		this.chain = conversionServices;
	}

	/**
	 * Returns the conversion services in the chain managed by this composite conversion service.
	 */
	public ConversionService[] getConversionServices() {
		return chain;
	}

	public ConversionExecutor getConversionExecutor(Class sourceClass, Class targetClass) throws ConversionException {
		for (int i = 0; i < chain.length; i++) {
			try {
				return chain[i].getConversionExecutor(sourceClass, targetClass);
			} catch (ConversionException e) {
				// ignore and try the next conversion service in the chain
			}
		}
		throw new ConversionException(sourceClass, targetClass, "No converter registered to convert from sourceClass '"
				+ sourceClass + "' to target class '" + targetClass + "'");
	}

	public ConversionExecutor getConversionExecutorByTargetAlias(Class sourceClass, String targetAlias)
			throws ConversionException {
		boolean exceptionThrown = false;
		for (int i = 0; i < chain.length; i++) {
			try {
				ConversionExecutor res = chain[i].getConversionExecutorByTargetAlias(sourceClass, targetAlias);
				if (res != null) {
					return res;
				}
			} catch (ConversionException e) {
				exceptionThrown = true;
			}
		}
		if (exceptionThrown) {
			throw new ConversionException(sourceClass, "No converter registered to convert from sourceClass '"
					+ sourceClass + "' to aliased target type '" + targetAlias + "'");
		} else {
			// alias was not recognized by any conversion service in the chain
			return null;
		}
	}

	public ConversionExecutor[] getConversionExecutorsForSource(Class sourceClass) throws ConversionException {
		Set executors = new HashSet();
		for (int i = 0; i < chain.length; i++) {
			executors.addAll(Arrays.asList(chain[i].getConversionExecutorsForSource(sourceClass)));
		}
		return (ConversionExecutor[]) executors.toArray(new ConversionExecutor[executors.size()]);
	}

	public Class getClassByAlias(String alias) throws ConversionException {
		for (int i = 0; i < chain.length; i++) {
			try {
				Class res = chain[i].getClassByAlias(alias);
				if (res != null) {
					return res;
				}
			} catch (ConversionException e) {
				// ignore and try the next conversion service in the chain
			}
		}
		return null;
	}
}