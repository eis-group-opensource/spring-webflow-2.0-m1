/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.mapping;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.style.ToStringCreator;

/**
 * Generic attributes mapper implementation that allows mappings to be configured programatically.
 * 
 * @author Erwin Vervaet
 * @author Keith Donald
 * @author Colin Sampaleanu
 */
public class DefaultAttributeMapper implements AttributeMapper {

	/**
	 * The ordered list of mappings to apply.
	 */
	private List mappings = new LinkedList();

	/**
	 * Add a mapping to this mapper.
	 * @param mapping the mapping to add (as an AttributeMapper)
	 * @return this, to support convenient call chaining
	 */
	public DefaultAttributeMapper addMapping(AttributeMapper mapping) {
		mappings.add(mapping);
		return this;
	}

	/**
	 * Add a set of mappings.
	 * @param mappings the mappings
	 */
	public void addMappings(AttributeMapper[] mappings) {
		if (mappings == null) {
			return;
		}
		this.mappings.addAll(Arrays.asList(mappings));
	}

	/**
	 * Returns this mapper's list of mappings.
	 * @return the list of mappings
	 */
	public AttributeMapper[] getMappings() {
		return (AttributeMapper[]) mappings.toArray(new AttributeMapper[mappings.size()]);
	}

	public void map(Object source, Object target, MappingContext context) {
		if (mappings != null) {
			Iterator it = mappings.iterator();
			while (it.hasNext()) {
				AttributeMapper mapping = (AttributeMapper) it.next();
				mapping.map(source, target, context);
			}
		}
	}

	public String toString() {
		return new ToStringCreator(this).append("mappings", mappings).toString();
	}
}