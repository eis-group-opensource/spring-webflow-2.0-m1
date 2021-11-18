/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

import java.io.Serializable;

/**
 * Test bean used in unit tests.
 */
public class TestBean implements Serializable {

	private int amount = 0;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean equals(Object o) {
		if (!(o instanceof TestBean)) {
			return false;
		}
		return amount == ((TestBean) o).amount;
	}

	public int hashCode() {
		return amount * 29;
	}

	public String toString() {
		return "[TestBean amount = " + amount + "]";
	}
}