package com.stubhub.domain.account.datamodel.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.datamodel.dao.OrderProcStatusAdapterDAO;
import com.stubhub.domain.account.datamodel.dao.OrderProcStatusDAO;
import com.stubhub.domain.account.datamodel.entity.OrderProcStatus;
import com.stubhub.domain.account.datamodel.entity.OrderProcStatusDO;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

@Component("orderProcStatusAdapterDAO")
public class OrderProcStatusAdapterDAOImpl implements OrderProcStatusAdapterDAO {

	private static final Logger log = LoggerFactory.getLogger(OrderProcStatusAdapterDAOImpl.class);

	@Autowired
	@Qualifier("orderProcStatusDAO")
	private OrderProcStatusDAO orderProcStatusDAO;

	@Override
    public List<OrderProcStatus> findOrderStatusByTransId(Long tid) {
		List<Object[]> orderStatusList=  orderProcStatusDAO.findOrderStatusByTransId(tid);
		if(orderStatusList == null){
			return null;
		}
		log.info("data found for the orderid=" + tid);
		List<OrderProcStatus> orderProcStatusList = new ArrayList<OrderProcStatus>();
		OrderProcStatus orderProcStatus;
		for (Object[] orderProcStatusDO : orderStatusList) {
			orderProcStatus = new OrderProcStatus();
			orderProcStatus.setStatusCode(Long.valueOf((orderProcStatusDO[1]).toString()));
			orderProcStatus.setStatusDescription((orderProcStatusDO[2]).toString());
			orderProcStatus.setSubStatusCode(((OrderProcStatusDO) orderProcStatusDO[0]).getOrderProcSubStatusCode());
			orderProcStatus.setSubStatusDescription((orderProcStatusDO[3]).toString());
			orderProcStatus.setStatusEffectiveDate(formatDate(((OrderProcStatusDO) orderProcStatusDO[0]).getOrderProcStatusEffDate(),((OrderProcStatusDO) orderProcStatusDO[0]).getOrderProcStatusEffDate().getTimeZone().getID()));
			orderProcStatusList.add(orderProcStatus);
		}
		return orderProcStatusList;
	}
	
	@Override
    public Long updateOrderStatusByTransId(Long orderId, String operatorId, Long newOrderProcSubStatusCode) {
		log.info("api_domain=account api_resource=orders api_method=updateOrderStatusByTransId message=updating order proc status for orderId=" + orderId);
		List<Object[]> orderProcStatusList=  orderProcStatusDAO.findOrderStatusByTransId(orderId);
		//Soft delete existing order proc status record if exist
		if (orderProcStatusList != null && orderProcStatusList.size() > 0) {
			OrderProcStatusDO orderProcStatusUpdate = null;
			for (Object[] orderProcStatusDO : orderProcStatusList) {
				if (((OrderProcStatusDO) orderProcStatusDO[0]).getOrderProcStatusEndDate() == null) {
					orderProcStatusUpdate = (OrderProcStatusDO) orderProcStatusDO[0];
					break;
				}
			}
			orderProcStatusUpdate.setOrderProcStatusEndDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
			orderProcStatusUpdate.setLastUpdatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
			orderProcStatusUpdate.setLastUpdatedBy(operatorId);
			orderProcStatusDAO.persist(orderProcStatusUpdate);
		}
		//Create new order proc status record
		OrderProcStatusDO newOrderProcStatus = new OrderProcStatusDO();
		newOrderProcStatus.setOrderProcSubStatusCode(newOrderProcSubStatusCode);
		newOrderProcStatus.setTid(orderId);
		newOrderProcStatus.setCreatedBy(operatorId);
		newOrderProcStatus.setOrderProcStatusEffDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		newOrderProcStatus.setCreatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		newOrderProcStatus.setLastUpdatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		newOrderProcStatus.setLastUpdatedBy(operatorId);
		orderProcStatusDAO.persist(newOrderProcStatus);
		return newOrderProcStatus.getOrderProcStatusId();
	}

	private static String formatDate(Calendar cal, String timeZoneStr) {
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
		Calendar newcal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND));
		newcal.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		sf.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
		return sf.format(newcal.getTime());
	}
}