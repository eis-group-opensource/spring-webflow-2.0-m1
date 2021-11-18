/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow;

import java.io.Serializable;

/**
 * Simple test bean used by some test cases. Note that this bean has value semantics.
 */
public class TestBean implements Serializable {

	public String datum1 = "";

	public int datum2;

	public boolean executed;

	public void execute() {
		this.executed = true;
	}

	public String getDatum1() {
		return datum1;
	}

	public int getDatum2() {
		return datum2;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void execute(String parameter) {
		this.executed = true;
		this.datum1 = parameter;
	}

	public int execute(String parameter, int parameter2) {
		this.executed = true;
		this.datum1 = parameter;
		this.datum2 = parameter2;
		return datum2;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof TestBean)) {
			return false;
		}
		TestBean other = (TestBean) obj;
		return datum1.equals(other.datum1) && datum2 == other.datum2 && executed == other.executed;
	}

	public int hashCode() {
		return (datum1.hashCode() + datum2 + (executed ? 1 : 0)) * 29;
	}
}