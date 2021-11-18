/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.support;

public interface TestMethods {

	public void doSomethingWithInt(int arg);

	public String returnStringFromInt(int arg);

	public String returnStringFromIntAndObject(int arg, TestBean bean);
}
