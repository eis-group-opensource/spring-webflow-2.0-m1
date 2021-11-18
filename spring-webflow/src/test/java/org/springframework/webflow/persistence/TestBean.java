/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.persistence;

import java.util.HashSet;
import java.util.Set;

public class TestBean {

	private Long entityId;

	private String name;

	private Set addresses = new HashSet();

	public TestBean() {

	}

	public TestBean(String name) {
		this.name = name;
	}

	public TestBean(long id, String name) {
		this.entityId = new Long(id);
		this.name = name;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Set getAddresses() {
		return addresses;
	}

	public void setAddresses(Set addresses) {
		this.addresses = addresses;
	}
}
