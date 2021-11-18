/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class MessageSourceAwareAction extends AbstractAction implements MessageSourceAware {

	private MessageSourceAccessor messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = new MessageSourceAccessor(messageSource);
	}

	protected Event doExecute(RequestContext context) throws Exception {
		Assert.notNull(messageSource.getMessage("foo"));
		Assert.isTrue(messageSource.getMessage("foo").equals("bar"));
		try {
			messageSource.getMessage("bar");
			throw new IllegalStateException();
		} catch (NoSuchMessageException e) {
			// expected
		}
		return success();
	}

}
