/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.core.JdkVersion;
import org.springframework.core.enums.LabeledEnum;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.support.EventFactorySupport;

/**
 * Result object-to-event adapter interface that tries to do a sensible conversion of the result object into a web flow
 * event. It uses the following conversion table: <table border="1">
 * <tr>
 * <th>Result object type</th>
 * <th>Event id</th>
 * <th>Remarks</th>
 * </tr>
 * <tr>
 * <td>null</td>
 * <td>{@link org.springframework.webflow.execution.support.EventFactorySupport#getNullEventId()}</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>{@link java.lang.Boolean} or boolean</td>
 * <td>{@link org.springframework.webflow.execution.support.EventFactorySupport#getYesEventId()}/
 * {@link org.springframework.webflow.execution.support.EventFactorySupport#getNoEventId()}</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>{@link org.springframework.core.enums.LabeledEnum}</td>
 * <td>{@link org.springframework.core.enums.LabeledEnum#getLabel()}</td>
 * <td>The result object will included in the event as an attribute named "result".</td>
 * </tr>
 * <tr>
 * <td>{@link java.lang.Enum}</td>
 * <td>{@link java.lang.Enum#name()}</td>
 * <td>The result object will included in the event as an attribute named "result".</td>
 * </tr>
 * <tr>
 * <td>{@link java.lang.String}</td>
 * <td>The string.</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>{@link org.springframework.webflow.execution.Event}</td>
 * <td>The resulting event object.</td>
 * <td>&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class ResultObjectBasedEventFactory extends EventFactorySupport implements ResultEventFactory {

	public Event createResultEvent(Object source, Object resultObject, RequestContext context) {
		if (resultObject == null) {
			// this handles the case where the declared result return type is mapped
			// by this class but the value is null
			return event(source, getNullEventId());
		} else if (isBoolean(resultObject.getClass())) {
			return event(source, ((Boolean) resultObject).booleanValue());
		} else if (isLabeledEnum(resultObject.getClass())) {
			String resultId = ((LabeledEnum) resultObject).getLabel();
			return event(source, resultId, getResultAttributeName(), resultObject);
		} else if (isJdk5Enum(resultObject.getClass())) {
			String eventId = EnumNameResolver.getEnumName(resultObject);
			return event(source, eventId, getResultAttributeName(), resultObject);
		} else if (isString(resultObject.getClass())) {
			return event(source, (String) resultObject);
		} else if (isEvent(resultObject.getClass())) {
			return (Event) resultObject;
		} else {
			throw new IllegalArgumentException("Cannot deal with result object '" + resultObject + "' of type '"
					+ resultObject.getClass() + "'");
		}
	}

	/**
	 * Check whether or not given type is mapped to a corresponding event using special mapping rules.
	 */
	public boolean isMappedValueType(Class type) {
		return isBoolean(type) || isLabeledEnum(type) || isJdk5Enum(type) || isString(type) || isEvent(type);
	}

	// internal helpers to determine the 'type' of a class

	private boolean isBoolean(Class type) {
		return Boolean.class.equals(type) || boolean.class.equals(type);
	}

	private boolean isLabeledEnum(Class type) {
		return LabeledEnum.class.isAssignableFrom(type);
	}

	private boolean isJdk5Enum(Class type) {
		if (JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_15) {
			return type.isEnum();
		} else {
			return false;
		}
	}

	private boolean isString(Class type) {
		return String.class.equals(type);
	}

	private boolean isEvent(Class type) {
		return Event.class.isAssignableFrom(type);
	}

	/**
	 * Simple helper class with Java 5 specific code factored out to keep the containing class JDK 1.3 compatible.
	 */
	private static class EnumNameResolver {
		public static String getEnumName(Object enumValue) {
			return ((java.lang.Enum) enumValue).name();
		}
	}
}