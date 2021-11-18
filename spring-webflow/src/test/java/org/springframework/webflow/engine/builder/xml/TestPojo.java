/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import org.springframework.util.Assert;
import org.springframework.webflow.execution.FlowSessionStatus;

public class TestPojo {
	private boolean flag;

	public boolean booleanMethod() {
		return true;
	}

	public FlowSessionStatus enumMethod() {
		return FlowSessionStatus.CREATED;
	}

	public void methodWithVariableArgument(FlowSessionStatus status) {
		Assert.isTrue(status == FlowSessionStatus.CREATED);
	}

	public void methodWithConstantArgument(String constant) {
		Assert.isTrue(constant.equals("A constant"));
	}

	public void methodWithArgumentTypeConversion(FlowSessionStatus status) {
		Assert.isTrue(status == FlowSessionStatus.CREATED);
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}