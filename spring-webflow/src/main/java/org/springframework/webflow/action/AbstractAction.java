/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.support.EventFactorySupport;

/**
 * Base action that provides assistance commonly needed by action implementations. This includes:
 * <ul>
 * <li>Implementing {@link InitializingBean} to receive an init callback when deployed within a Spring bean factory.
 * <li>Exposing convenient event factory methods to create common result {@link Event} objects such as "success" and
 * "error".
 * <li>A hook for inserting action pre and post execution logic.
 * </ul>
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class AbstractAction implements Action, InitializingBean {

	/**
	 * Logger, usable in subclasses.
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Returns the helper delegate for creating action execution result events.
	 * @return the event factory support
	 */
	public EventFactorySupport getEventFactorySupport() {
		return new EventFactorySupport();
	}

	public void afterPropertiesSet() throws Exception {
		try {
			initAction();
		} catch (Exception ex) {
			throw new BeanInitializationException("Initialization of this Action failed: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Action initializing callback, may be overriden by subclasses to perform custom initialization logic.
	 * <p>
	 * Keep in mind that this hook will only be invoked when this action is deployed in a Spring application context
	 * since it uses the Spring {@link InitializingBean} mechanism to trigger action initialisation.
	 */
	protected void initAction() throws Exception {
	}

	/**
	 * Returns a "success" result event.
	 */
	protected Event success() {
		return getEventFactorySupport().success(this);
	}

	/**
	 * Returns a "success" result event with the provided result object as a parameter.
	 * @param result the action success result
	 */
	protected Event success(Object result) {
		return getEventFactorySupport().success(this, result);
	}

	/**
	 * Returns an "error" result event.
	 */
	protected Event error() {
		return getEventFactorySupport().error(this);
	}

	/**
	 * Returns an "error" result event caused by the provided exception.
	 * @param e the exception that caused the error event, to be configured as an event attribute
	 */
	protected Event error(Exception e) {
		return getEventFactorySupport().error(this, e);
	}

	/**
	 * Returns a "yes" result event.
	 */
	protected Event yes() {
		return getEventFactorySupport().yes(this);
	}

	/**
	 * Returns a "no" result event.
	 */
	protected Event no() {
		return getEventFactorySupport().no(this);
	}

	/**
	 * Returns yes() if the boolean result is true, no() if false.
	 * @param booleanResult the boolean
	 * @return yes or no
	 */
	protected Event result(boolean booleanResult) {
		return getEventFactorySupport().event(this, booleanResult);
	}

	/**
	 * Returns a result event for this action with the specified identifier. Typically called as part of return, for
	 * example:
	 * 
	 * <pre>
	 *     protected Event doExecute(RequestContext context) {
	 *         // do some work
	 *         if (some condition) {
	 *             return result(&quot;success&quot;);
	 *         } else {
	 *             return result(&quot;error&quot;);
	 *         }
	 *     }
	 * </pre>
	 * 
	 * Consider calling the error() or success() factory methods for returning common results.
	 * @param eventId the result event identifier
	 * @return the action result event
	 */
	protected Event result(String eventId) {
		return getEventFactorySupport().event(this, eventId);
	}

	/**
	 * Returns a result event for this action with the specified identifier and the specified set of attributes.
	 * Typically called as part of return, for example:
	 * 
	 * <pre>
	 *     protected Event doExecute(RequestContext context) {
	 *         // do some work
	 *         AttributeMap resultAttributes = new AttributeMap();
	 *         resultAttributes.put(&quot;name&quot;, &quot;value&quot;);
	 *         if (some condition) {
	 *             return result(&quot;success&quot;, resultAttributes);
	 *         } else {
	 *             return result(&quot;error&quot;, resultAttributes);
	 *         }
	 *     }
	 * </pre>
	 * 
	 * Consider calling the error() or success() factory methods for returning common results.
	 * @param eventId the result event identifier
	 * @param resultAttributes the event attributes
	 * @return the action result event
	 */
	protected Event result(String eventId, AttributeMap resultAttributes) {
		return getEventFactorySupport().event(this, eventId, resultAttributes);
	}

	/**
	 * Returns a result event for this action with the specified identifier and a single attribute.
	 * @param eventId the result id
	 * @param resultAttributeName the attribute name
	 * @param resultAttributeValue the attribute value
	 * @return the action result event
	 */
	protected Event result(String eventId, String resultAttributeName, Object resultAttributeValue) {
		return getEventFactorySupport().event(this, eventId, resultAttributeName, resultAttributeValue);
	}

	public final Event execute(RequestContext context) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Action '" + getActionNameForLogging() + "' beginning execution");
		}
		Event result = doPreExecute(context);
		if (result == null) {
			result = doExecute(context);
			if (logger.isDebugEnabled()) {
				if (result != null) {
					logger.debug("Action '" + getActionNameForLogging() + "' completed execution; result is '"
							+ result.getId() + "'");
				} else {
					logger.debug("Action '" + getActionNameForLogging() + "' completed execution; result is [null]");
				}
			}
			doPostExecute(context);
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("Action execution disallowed; pre-execution result is '" + result.getId() + "'");
			}
		}
		return result;
	}

	// subclassing hooks

	/**
	 * Internal helper to return the name of this action for logging purposes. Defaults to the short class name.
	 * @see ClassUtils#getShortName(java.lang.Class)
	 */
	protected String getActionNameForLogging() {
		return ClassUtils.getShortName(getClass());
	}

	/**
	 * Pre-action-execution hook, subclasses may override. If this method returns a non-<code>null</code> event, the
	 * <code>doExecute()</code> method will <b>not</b> be called and the returned event will be used to select a
	 * transition to trigger in the calling action state. If this method returns <code>null</code>,
	 * <code>doExecute()</code> will be called to obtain an action result event.
	 * <p>
	 * This implementation just returns <code>null</code>.
	 * @param context the action execution context, for accessing and setting data in "flow scope" or "request scope"
	 * @return the non-<code>null</code> action result, in which case the <code>doExecute()</code> will not be
	 * called, or <code>null</code> if the <code>doExecute()</code> method should be called to obtain the action
	 * result
	 * @throws Exception an <b>unrecoverable</b> exception occured, either checked or unchecked
	 */
	protected Event doPreExecute(RequestContext context) throws Exception {
		return null;
	}

	/**
	 * Template hook method subclasses should override to encapsulate their specific action execution logic.
	 * @param context the action execution context, for accessing and setting data in "flow scope" or "request scope"
	 * @return the action result event
	 * @throws Exception an <b>unrecoverable</b> exception occured, either checked or unchecked
	 */
	protected abstract Event doExecute(RequestContext context) throws Exception;

	/**
	 * Post-action execution hook, subclasses may override. Will only be called if <code>doExecute()</code> was
	 * called, e.g. when <code>doPreExecute()</code> returned <code>null</code>.
	 * <p>
	 * This implementation does nothing.
	 * @param context the action execution context, for accessing and setting data in "flow scope" or "request scope"
	 * @throws Exception an <b>unrecoverable</b> exception occured, either checked or unchecked
	 */
	protected void doPostExecute(RequestContext context) throws Exception {
	}
}