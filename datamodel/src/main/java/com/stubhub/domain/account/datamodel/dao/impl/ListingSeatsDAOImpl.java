package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.ListingSeatsDAO;
import com.stubhub.domain.account.datamodel.entity.TicketSeat;

@Component("listingSeatsDAO")
public class ListingSeatsDAOImpl implements ListingSeatsDAO {

	private final static Logger log = LoggerFactory.getLogger(ListingSeatsDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public int updateTicketSeatStatus(List<TicketSeat> lst){
		int result = 0;
		Session session = this.hibernateTemplate.getSessionFactory().getCurrentSession();
//		Session session = this.hibernateTemplate.getSessionFactory().openSession();
//		Transaction tx = session.beginTransaction();
		for (TicketSeat seat : lst){
			SQLQuery query = (SQLQuery) session.getNamedQuery("TicketSeat.updateSeatStatus");
			query.setLong("ticketSeatId", seat.getTicketSeatId());
			query.setLong("seatStatusId", seat.getSeatStatusId());
			query.setBoolean("orderPlacedInd", seat.getOrderPlacedInd());
			query.setCalendar("lastUpdateDate", seat.getLastUpdatedDate());
			query.setString("lastUpdatedBy", seat.getLastUpdatedBy());
			int rowsUpdated = query.executeUpdate();
			result = result + rowsUpdated;
			log.debug("updated record for ticketSeatId=" + seat.getTicketSeatId());
		}
//		tx.commit();
//		session.close();	
		return result;
	}

    @Override
    @Transactional(readOnly = true)
    public List<TicketSeat> getTicketSeats(Long ticketId) {
        log.debug("get ticket seat by ticketId="+ticketId);
        Query query = this.hibernateTemplate.getSessionFactory()
                .getCurrentSession().getNamedQuery("TicketSeat.getSeatInfo");
        query.setLong("ticketId", ticketId);
        List<TicketSeat> result = query.list();
        if(result != null && result.size() > 0) {
            return result;
        }
        return null;
    }
}
