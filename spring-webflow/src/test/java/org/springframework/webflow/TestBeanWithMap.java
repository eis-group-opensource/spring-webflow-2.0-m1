/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple test bean with a Map property.
 */
public class TestBeanWithMap implements Serializable {

	private Map map = new HashMap();

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
}
