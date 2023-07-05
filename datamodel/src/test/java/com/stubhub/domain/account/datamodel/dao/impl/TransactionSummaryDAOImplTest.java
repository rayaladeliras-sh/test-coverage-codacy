package com.stubhub.domain.account.datamodel.dao.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TransactionSummaryDAOImplTest {

	private static final Logger log = LoggerFactory.getLogger(TransactionSummaryDAOImplTest.class);

	private final static String[] SIMPLE_STRING_PARAM_NAMES = new String[] { "PURCHASE_TOTAL",
			"OPEN_ORDER_PURCHASE_TOTAL", "PURCHASE_COUNT", "AVERAGE_ORDER_SIZE", "COMPLETED_SALES_COUNT",
			"CANCELLED_SALES_COUNT", "UNCONFIRMED_SALES_COUNT", "CANCELLED_BUYS_COUNT", "COMPLETED_BUYS_COUNT",
			"UNCONFIRMED_BUYS_COUNT", "TOP_BUYER", "ACTIVE_COUNT", "INACTIVE_COUNT", "PENDING_LOCK_COUNT",
			"DELETED_COUNT", "INCOMPLETE_COUNT", "PENDING_LMS_COUNT", "BUYER_FLIP_COUNT", "DROP_ORDER_RATE" };

	private static final String VALUE = "value";

	private TransactionSummaryDAOImpl transactionsummaryDAO;

	@Mock
	private HibernateTemplate hibernateTemplate;

	@Mock
	private SessionFactory sessionFactory;

	@Mock
	private Session session;

	@Mock
	private ResultSet rs;

	@Mock
	private CallableStatement callableStatement = null;

	@Mock
	private Connection conn = null;

	private long userId = 108256;

	@BeforeMethod
	public void setUp() {
		transactionsummaryDAO = new TransactionSummaryDAOImpl();
		hibernateTemplate = mock(HibernateTemplate.class);
		sessionFactory = mock(SessionFactory.class);
		session = mock(Session.class);
		conn = mock(Connection.class);
		callableStatement = mock(CallableStatement.class);
		rs = mock(ResultSet.class);

		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(session.connection()).thenReturn(conn);

		ReflectionTestUtils.setField(transactionsummaryDAO, "hibernateTemplate", hibernateTemplate);

	}

	@Test
	public void testGetSummaryDetailError() throws SQLException {
		ReflectionTestUtils.setField(transactionsummaryDAO, "hibernateTemplate", null);
		List<HashMap<String, String>> res = transactionsummaryDAO.getSummaryDetails(userId, "USD", true);

		assertNotNull(res);
		assertTrue(res.size() == 0);
	}

	@Test
	public void testGetSummaryDetailSuccess() throws SQLException {
		when(conn.prepareCall(any(String.class))).thenReturn(callableStatement);
		when(callableStatement.getObject(1)).thenReturn(rs);

		setRSStringExpectations(SIMPLE_STRING_PARAM_NAMES);
		when(rs.getString("EVENT_DATE_LOCAL")).thenReturn(VALUE);
		when(rs.getString("EVENT_DATE_UTC")).thenReturn("1990-01-01 01:01:01");
		when(rs.next()).thenReturn(true, true, false);

		List<HashMap<String, String>> res = transactionsummaryDAO.getSummaryDetails(userId, "USD",true);

		assertNotNull(res);
		assertTrue(res.size() == 4);
	}

	//@Test
	public void testGetSummaryDetailWrongDateFormatFormat() throws SQLException {
		when(conn.prepareCall(any(String.class))).thenReturn(callableStatement);
		when(callableStatement.getObject(1)).thenReturn(rs);

		when(rs.getString("EVENT_DATE_UTC")).thenReturn("wrongdate");
		when(rs.next()).thenReturn(true, true, false);

		List<HashMap<String, String>> res = transactionsummaryDAO.getSummaryDetails(userId, "USD", true);

		assertNotNull(res);
		assertTrue(res.size() == 0);
	}

	@Test
	public void testGetSummaryDetailSqlEx() throws SQLException {
		when(conn.prepareCall(any(String.class))).thenReturn(callableStatement);
		when(callableStatement.getObject(1)).thenReturn(rs);

		Mockito.doThrow(SQLException.class).when(rs).next();

		List<HashMap<String, String>> res = transactionsummaryDAO.getSummaryDetails(userId, "USD", true);

		assertNotNull(res);
		assertTrue(res.size() == 0);
	}

	private void setRSStringExpectations(String... params) throws SQLException {
		for (String param : params) {
			when(rs.getString(param)).thenReturn(VALUE);
		}
	}

	@Test
	private void testCloseRS() throws SQLException {

		ResultSet trs = null;
		transactionsummaryDAO.close(trs);

		trs = rs;
		transactionsummaryDAO.close(trs);

		Mockito.doThrow(SQLException.class).when(rs).close();

		transactionsummaryDAO.close(trs);

	}

	@Test
	private void testCloseCallableStatement() throws SQLException {

		CallableStatement tcs = null;
		transactionsummaryDAO.close(tcs);

		tcs = callableStatement;
		transactionsummaryDAO.close(tcs);

		Mockito.doThrow(SQLException.class).when(tcs).close();

		transactionsummaryDAO.close(tcs);

	}

	@Test
	private void testCloseConnection() throws SQLException {

		Connection tc = null;
		transactionsummaryDAO.close(tc);

		tc = conn;
		transactionsummaryDAO.close(tc);

		Mockito.doThrow(SQLException.class).when(conn).close();

		transactionsummaryDAO.close(tc);

	}

}
