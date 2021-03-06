/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

/**
 * Convenience helper that encapsulates logic on how to retrieve and expose form objects and associated errors to and
 * from a flow execution request context.
 * <p>
 * <b>Note</b>: The form object available under the well known attribute name {@link #CURRENT_FORM_OBJECT_ATTRIBUTE}
 * will be the last ("current") form object set in the request context. The same is true for the associated errors
 * object. This implies that special care should be taken when accessing the form object using this alias if there are
 * multiple form objects available in the flow execution request context!
 * 
 * @see org.springframework.webflow.execution.RequestContext
 * @see org.springframework.validation.Errors
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FormObjectAccessor {

	/**
	 * The form object instance is aliased under this attribute name in the flow context by the default form setup and
	 * bind and validate actions.
	 * <p>
	 * Note that if you would have multiple form objects in the request context, the last one that was used would be
	 * available using this alias!
	 * <p>
	 * We need to keep track of the 'current form object' using this attribute to be able to deal with the limitations
	 * of some clients that can only deal with a single form backing object, e.g. Struts when using the Struts
	 * FlowAction.
	 */
	private static final String CURRENT_FORM_OBJECT_ATTRIBUTE = "currentFormObject";

	/**
	 * The errors prefix.
	 */
	// use deprecated API to remain compatible with Spring 1.2.x
	private static final String ERRORS_PREFIX = BindException.class.getName() + ".";

	/**
	 * The wrapped request context.
	 */
	private RequestContext context;

	/**
	 * Creates a form object accessor that wraps the given context.
	 * @param context the flow execution request context
	 */
	public FormObjectAccessor(RequestContext context) {
		this.context = context;
	}

	/**
	 * Returns the current form object name.
	 * @return the current form object name
	 */
	public static String getCurrentFormObjectName() {
		return CURRENT_FORM_OBJECT_ATTRIBUTE;
	}

	/**
	 * Returns the current form object errors attribute name.
	 * @return the current form object errors attribute name
	 */
	public static String getCurrentFormErrorsName() {
		return ERRORS_PREFIX + getCurrentFormObjectName();
	}

	/**
	 * Gets the form object from the context, using the well-known attribute name {@link #CURRENT_FORM_OBJECT_ATTRIBUTE}
	 * . Will try all scopes.
	 * @return the form object, or null if not found
	 */
	public Object getCurrentFormObject() {
		Object formObject = getCurrentFormObject(ScopeType.REQUEST);
		if (formObject != null) {
			return formObject;
		}
		formObject = getCurrentFormObject(ScopeType.FLASH);
		if (formObject != null) {
			return formObject;
		}
		formObject = getCurrentFormObject(ScopeType.FLOW);
		if (formObject != null) {
			return formObject;
		}
		return getCurrentFormObject(ScopeType.CONVERSATION);
	}

	/**
	 * Gets the form object from the context, using the well-known attribute name {@link #CURRENT_FORM_OBJECT_ATTRIBUTE}
	 * .
	 * @param scopeType the scope to obtain the form object from
	 * @return the form object, or null if not found
	 */
	public Object getCurrentFormObject(ScopeType scopeType) {
		return getFormObject(getCurrentFormObjectName(), scopeType);
	}

	/**
	 * Expose given form object using the well known alias {@link #CURRENT_FORM_OBJECT_ATTRIBUTE} in the specified
	 * scope.
	 * @param formObject the form object
	 * @param scopeType the scope in which to expose the form object
	 */
	public void setCurrentFormObject(Object formObject, ScopeType scopeType) {
		// don't call setFormObject since that would cause infinite recursion!
		scopeType.getScope(context).put(getCurrentFormObjectName(), formObject);
	}

	/**
	 * Gets the form object from the context, using the specified name.
	 * @param formObjectName the name of the form object in the context
	 * @param scopeType the scope to obtain the form object from
	 * @return the form object, or null if not found
	 */
	public Object getFormObject(String formObjectName, ScopeType scopeType) {
		return scopeType.getScope(context).get(formObjectName);
	}

	/**
	 * Gets the form object from the context, using the specified name.
	 * @param formObjectName the name of the form in the context
	 * @param formObjectClass the class of the form object, which will be verified
	 * @param scopeType the scope to obtain the form object from
	 * @return the form object, or null if not found
	 */
	public Object getFormObject(String formObjectName, Class formObjectClass, ScopeType scopeType) {
		return scopeType.getScope(context).get(formObjectName, formObjectClass);
	}

	/**
	 * Expose given form object using given name in specified scope. Given object will become the <i>current</i> form
	 * object.
	 * @param formObject the form object
	 * @param formObjectName the name of the form object
	 * @param scopeType the scope in which to expose the form object
	 */
	public void putFormObject(Object formObject, String formObjectName, ScopeType scopeType) {
		scopeType.getScope(context).put(formObjectName, formObject);
		setCurrentFormObject(formObject, scopeType);
	}

	/**
	 * Gets the form object <code>Errors</code> tracker from the context, using the form object name
	 * {@link #CURRENT_FORM_OBJECT_ATTRIBUTE}. This method will search all scopes.
	 * @return the form object Errors tracker, or null if not found
	 */
	public Errors getCurrentFormErrors() {
		Errors errors = getCurrentFormErrors(ScopeType.REQUEST);
		if (errors != null) {
			return errors;
		}
		errors = getCurrentFormErrors(ScopeType.FLASH);
		if (errors != null) {
			return errors;
		}
		errors = getCurrentFormErrors(ScopeType.FLOW);
		if (errors != null) {
			return errors;
		}
		return getCurrentFormErrors(ScopeType.CONVERSATION);
	}

	/**
	 * Gets the form object <code>Errors</code> tracker from the context, using the form object name
	 * {@link #CURRENT_FORM_OBJECT_ATTRIBUTE}.
	 * @param scopeType the scope to obtain the errors from
	 * @return the form object Errors tracker, or null if not found
	 */
	public Errors getCurrentFormErrors(ScopeType scopeType) {
		return getFormErrors(getCurrentFormObjectName(), scopeType);
	}

	/**
	 * Expose given errors instance using the well known alias {@link #CURRENT_FORM_OBJECT_ATTRIBUTE} in the specified
	 * scope.
	 * @param errors the errors instance
	 * @param scopeType the scope in which to expose the errors instance
	 */
	public void setCurrentFormErrors(Errors errors, ScopeType scopeType) {
		scopeType.getScope(context).put(getCurrentFormErrorsName(), errors);
	}

	/**
	 * Gets the form object <code>Errors</code> tracker from the context, using the specified form object name.
	 * @param formObjectName the name of the Errors object, which will be prefixed with
	 * {@link BindException#ERROR_KEY_PREFIX}
	 * @param scopeType the scope to obtain the errors from
	 * @return the form object errors instance, or null if not found
	 */
	public Errors getFormErrors(String formObjectName, ScopeType scopeType) {
		return (Errors) scopeType.getScope(context).get(ERRORS_PREFIX + formObjectName, Errors.class);
	}

	/**
	 * Expose given errors instance in the specified scope. Given errors instance will become the <i>current</i> form
	 * errors instance.
	 * @param errors the errors object
	 * @param scopeType the scope to expose the errors in
	 */
	public void putFormErrors(Errors errors, ScopeType scopeType) {
		scopeType.getScope(context).put(ERRORS_PREFIX + errors.getObjectName(), errors);
		setCurrentFormErrors(errors, scopeType);
	}
}