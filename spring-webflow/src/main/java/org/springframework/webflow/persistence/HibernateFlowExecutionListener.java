/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.persistence;

import org.hibernate.FlushMode;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;

/**
 * A {@link FlowExecutionListener} that implements the Flow Managed Persistence Context (FMPC) pattern using the native
 * Hibernate API.
 * <p>
 * The general pattern is as follows:
 * <ul>
 * <li>When a flow execution starts, create a new Hibernate Session and bind it to flow scope.
 * <li>Before processing a flow execution request, expose the conversationally-bound session as the "current session"
 * for the current thread.
 * <li>When an existing flow pauses, unbind the session from the current thread.
 * <li>When an existing flow ends, commit the changes made to the session in a transaction if the ending state is a
 * commit state. Then, unbind the context and close it.
 * </ul>
 * 
 * The general data access pattern implemented here is:
 * <ul>
 * <li> Create a new persistence context when a new flow execution with the 'persistenceContext' attribute starts
 * <li> Load some objects into this persistence context
 * <li> Perform edits to those objects over a series of requests into the flow
 * <li> On successful flow completion, commit and flush those edits to the database, applying a version check if
 * necessary.
 * </ul>
 * 
 * <p>
 * Note: All data access except for the final commit will, by default, be non-transactional. However, a flow may call
 * into a transactional service layer to fetch objects during the conversation in the context of a read-only system
 * transaction. In that case, the session's flush mode will be set to Manual and no intermediate changes will be
 * flushed.
 * <p>
 * Care should be taken to prevent premature commits of flow data while the flow is in progress. You would generally not
 * want intermediate flushing to happen, as the nature of a flow implies a transient, isolated resource that can be
 * canceled before it ends. Generally, the only time a read-write transaction should be started is upon successful
 * completion of the conversation, triggered by reaching a 'commit' end state.
 * 
 * @author Ben Hale
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 1.1
 */
public class HibernateFlowExecutionListener extends FlowExecutionListenerAdapter {

	private static final String PERSISTENCE_CONTEXT_ATTRIBUTE = "persistenceContext";

	private static final String HIBERNATE_SESSION_ATTRIBUTE = "session";

	private SessionFactory sessionFactory;

	private TransactionTemplate transactionTemplate;

	private Interceptor entityInterceptor;

	/**
	 * Create a new Hibernate Flow Execution Listener using giving Hibernate session factory and transaction manager.
	 * @param sessionFactory the session factory to use
	 * @param transactionManager the transaction manager to drive transactions
	 */
	public HibernateFlowExecutionListener(SessionFactory sessionFactory, PlatformTransactionManager transactionManager) {
		this.sessionFactory = sessionFactory;
		this.transactionTemplate = new TransactionTemplate(transactionManager);
	}

	/**
	 * Sets the entity interceptor to attach to sessions opened by this listener.
	 * @param entityInterceptor the entity interceptor
	 */
	public void setEntityInterceptor(Interceptor entityInterceptor) {
		this.entityInterceptor = entityInterceptor;
	}

	public void sessionCreated(RequestContext context, FlowSession session) {
		if (isPersistenceContext(session.getDefinition())) {
			Session hibernateSession = createSession(context);
			session.getScope().put(HIBERNATE_SESSION_ATTRIBUTE, hibernateSession);
			bind(hibernateSession);
		}
	}

	public void resumed(RequestContext context) {
		if (isPersistenceContext(context.getActiveFlow())) {
			bind(getHibernateSession(context));
		}
	}

	public void paused(RequestContext context, ViewSelection selectedView) {
		if (isPersistenceContext(context.getActiveFlow())) {
			unbind(getHibernateSession(context));
		}
	}

	public void sessionEnded(RequestContext context, FlowSession session, AttributeMap output) {
		if (isPersistenceContext(session.getDefinition())) {
			final Session hibernateSession = (Session) session.getScope().remove(HIBERNATE_SESSION_ATTRIBUTE);
			Boolean commitStatus = session.getState().getAttributes().getBoolean("commit");
			if (Boolean.TRUE.equals(commitStatus)) {
				// this is a commit end state - start a new transaction that quickly commits
				transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						sessionFactory.getCurrentSession();
						// nothing to do; a flush will happen on commit automatically as this is a read-write
						// transaction
					}
				});
			}
			unbind(hibernateSession);
			hibernateSession.close();
		}
	}

	public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
		if (isPersistenceContext(context.getActiveFlow())) {
			unbind(getHibernateSession(context));
		}
	}

	// internal helpers

	private boolean isPersistenceContext(FlowDefinition flow) {
		return flow.getAttributes().contains(PERSISTENCE_CONTEXT_ATTRIBUTE);
	}

	private Session createSession(RequestContext context) {
		Session session = (entityInterceptor != null ? sessionFactory.openSession(entityInterceptor) : sessionFactory
				.openSession());
		session.setFlushMode(FlushMode.MANUAL);
		return session;
	}

	private Session getHibernateSession(RequestContext context) {
		return (Session) context.getFlowScope().get(HIBERNATE_SESSION_ATTRIBUTE);
	}

	private void bind(Session session) {
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}

	private void unbind(Session session) {
		if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
			TransactionSynchronizationManager.unbindResource(sessionFactory);
		}
	}
}