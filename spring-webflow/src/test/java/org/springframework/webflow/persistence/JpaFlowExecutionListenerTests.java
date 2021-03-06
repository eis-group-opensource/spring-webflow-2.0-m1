/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.hibernate.Hibernate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.webflow.engine.EndState;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.test.MockFlowSession;
import org.springframework.webflow.test.MockRequestContext;

public class JpaFlowExecutionListenerTests extends TestCase {

	private EntityManagerFactory entityManagerFactory;

	private JpaFlowExecutionListener jpaListener;

	private JdbcTemplate jdbcTemplate;

	private JpaTemplate jpaTemplate;

	public void testTemp() {

	}

	protected void setUp() throws Exception {
		DataSource dataSource = getDataSource();
		populateDataBase(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
		entityManagerFactory = getEntityManagerFactory(dataSource);
		JpaTransactionManager tm = new JpaTransactionManager(entityManagerFactory);
		jpaListener = new JpaFlowExecutionListener(entityManagerFactory, tm);
		jpaTemplate = new JpaTemplate(entityManagerFactory);
	}

	public void testFlowNotAPersistenceContext() {
		MockRequestContext context = new MockRequestContext();
		MockFlowSession flowSession = new MockFlowSession();
		jpaListener.sessionCreated(context, flowSession);
		assertSessionNotBound();
	}

	public void testFlowCommitsInSingleRequest() {
		assertEquals("Table should only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		MockRequestContext context = new MockRequestContext();
		MockFlowSession flowSession = new MockFlowSession();
		flowSession.getDefinitionInternal().getAttributeMap().put("persistenceContext", "true");
		jpaListener.sessionCreated(context, flowSession);
		context.setActiveSession(flowSession);
		assertSessionBound();

		TestBean bean = new TestBean(1, "Keith Donald");
		jpaTemplate.persist(bean);
		assertEquals("Table should still only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));

		EndState endState = new EndState(flowSession.getDefinitionInternal(), "success");
		endState.getAttributeMap().put("commit", Boolean.TRUE);
		flowSession.setState(endState);

		jpaListener.sessionEnded(context, flowSession, null);
		assertEquals("Table should only have two rows", 2, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		assertSessionNotBound();
		assertFalse(flowSession.getScope().contains("hibernate.session"));
	}

	public void testFlowCommitsAfterMultipleRequests() {
		assertEquals("Table should only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		MockRequestContext context = new MockRequestContext();
		MockFlowSession flowSession = new MockFlowSession();
		flowSession.getDefinitionInternal().getAttributeMap().put("persistenceContext", "true");
		jpaListener.sessionCreated(context, flowSession);
		context.setActiveSession(flowSession);
		assertSessionBound();

		TestBean bean1 = new TestBean(1, "Keith Donald");
		jpaTemplate.persist(bean1);
		assertEquals("Table should still only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		jpaListener.paused(context, ViewSelection.NULL_VIEW);
		assertSessionNotBound();

		jpaListener.resumed(context);
		TestBean bean2 = new TestBean(2, "Keith Donald");
		jpaTemplate.persist(bean2);
		assertEquals("Table should still only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		assertSessionBound();

		EndState endState = new EndState(flowSession.getDefinitionInternal(), "success");
		endState.getAttributeMap().put("commit", Boolean.TRUE);
		flowSession.setState(endState);

		jpaListener.sessionEnded(context, flowSession, null);
		assertEquals("Table should only have three rows", 3, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		assertFalse(flowSession.getScope().contains("hibernate.session"));

		assertSessionNotBound();
		assertFalse(flowSession.getScope().contains("hibernate.session"));
	}

	public void testCancelEndState() {
		assertEquals("Table should only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		MockRequestContext context = new MockRequestContext();
		MockFlowSession flowSession = new MockFlowSession();
		flowSession.getDefinitionInternal().getAttributeMap().put("persistenceContext", "true");
		jpaListener.sessionCreated(context, flowSession);
		context.setActiveSession(flowSession);
		assertSessionBound();

		TestBean bean = new TestBean(1, "Keith Donald");
		jpaTemplate.persist(bean);
		assertEquals("Table should still only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));

		EndState endState = new EndState(flowSession.getDefinitionInternal(), "cancel");
		endState.getAttributeMap().put("commit", Boolean.FALSE);
		flowSession.setState(endState);
		jpaListener.sessionEnded(context, flowSession, null);
		assertEquals("Table should only have two rows", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		assertSessionNotBound();
		assertFalse(flowSession.getScope().contains("hibernate.session"));
	}

	public void testNoCommitAttributeSetOnEndState() {
		assertEquals("Table should only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		MockRequestContext context = new MockRequestContext();
		MockFlowSession flowSession = new MockFlowSession();
		flowSession.getDefinitionInternal().getAttributeMap().put("persistenceContext", "true");
		jpaListener.sessionCreated(context, flowSession);
		context.setActiveSession(flowSession);
		assertSessionBound();

		EndState endState = new EndState(flowSession.getDefinitionInternal(), "cancel");
		flowSession.setState(endState);

		jpaListener.sessionEnded(context, flowSession, null);
		assertEquals("Table should only have three rows", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		assertFalse(flowSession.getScope().contains("hibernate.session"));

		assertSessionNotBound();
		assertFalse(flowSession.getScope().contains("hibernate.session"));
	}

	public void testExceptionThrown() {
		assertEquals("Table should only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		MockRequestContext context = new MockRequestContext();
		MockFlowSession flowSession = new MockFlowSession();
		flowSession.getDefinitionInternal().getAttributeMap().put("persistenceContext", "true");
		jpaListener.sessionCreated(context, flowSession);
		context.setActiveSession(flowSession);
		assertSessionBound();

		TestBean bean = new TestBean(1, "Keith Donald");
		jpaTemplate.persist(bean);
		assertEquals("Table should still only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		jpaListener.exceptionThrown(context, new FlowExecutionException("bla", "bla", "bla"));
		assertEquals("Table should still only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		assertSessionNotBound();

	}

	public void testExceptionThrownWithNothingBound() {
		assertEquals("Table should only have one row", 1, jdbcTemplate.queryForInt("select count(*) from T_BEAN"));
		MockRequestContext context = new MockRequestContext();
		MockFlowSession flowSession = new MockFlowSession();
		flowSession.getDefinitionInternal().getAttributeMap().put("persistenceContext", "true");
		assertSessionNotBound();
		jpaListener.exceptionThrown(context, new FlowExecutionException("foo", "bar", "test"));
		assertSessionNotBound();
	}

	public void testLazilyInitalizedCollection() {
		MockRequestContext context = new MockRequestContext();
		MockFlowSession flowSession = new MockFlowSession();
		flowSession.getDefinitionInternal().getAttributeMap().put("persistenceContext", "true");
		jpaListener.sessionCreated(context, flowSession);
		context.setActiveSession(flowSession);
		assertSessionBound();

		TestBean bean = (TestBean) jpaTemplate.getReference(TestBean.class, Long.valueOf(0));
		assertFalse("addresses should not be initialized", Hibernate.isInitialized(bean.getAddresses()));
		jpaListener.paused(context, ViewSelection.NULL_VIEW);
		assertFalse("addresses should not be initialized", Hibernate.isInitialized(bean.getAddresses()));
		Hibernate.initialize(bean.getAddresses());
		assertTrue("addresses should be initialized", Hibernate.isInitialized(bean.getAddresses()));
	}

	private DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dataSource.setUrl("jdbc:hsqldb:mem:jpa");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}

	private void populateDataBase(DataSource dataSource) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.createStatement().execute("drop table T_ADDRESS if exists;");
			connection.createStatement().execute("drop table T_BEAN if exists;");
			connection.createStatement().execute(
					"create table T_BEAN (ID integer primary key, NAME varchar(50) not null);");
			connection.createStatement().execute(
					"create table T_ADDRESS (ID integer primary key, BEAN_ID integer, VALUE varchar(50) not null);");
			connection
					.createStatement()
					.execute(
							"alter table T_ADDRESS add constraint FK_BEAN_ADDRESS foreign key (BEAN_ID) references T_BEAN(ID) on delete cascade");
			connection.createStatement().execute("insert into T_BEAN (ID, NAME) values (0, 'Ben Hale');");
			connection.createStatement().execute(
					"insert into T_ADDRESS (ID, BEAN_ID, VALUE) values (0, 0, 'Melbourne')");
		} catch (SQLException e) {
			throw new RuntimeException("SQL exception occurred acquiring connection", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private EntityManagerFactory getEntityManagerFactory(DataSource dataSource) throws Exception {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setPersistenceXmlLocation("classpath:org/springframework/webflow/persistence/persistence.xml");
		HibernateJpaVendorAdapter hibernate = new HibernateJpaVendorAdapter();
		factory.setJpaVendorAdapter(hibernate);
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	private void assertSessionNotBound() {
		assertNull(TransactionSynchronizationManager.getResource(entityManagerFactory));
	}

	private void assertSessionBound() {
		assertNotNull(TransactionSynchronizationManager.getResource(entityManagerFactory));
	}

}
