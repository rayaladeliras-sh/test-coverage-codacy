package com.stubhub.domain.account.common;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.beanutils.PropertyUtils;
import org.testng.annotations.Test;

import com.stubhub.common.exception.ErrorType;
import com.stubhub.common.response.Response;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.LMSOption;
import com.stubhub.domain.account.common.enums.ListingFilterType;
import com.stubhub.domain.account.common.enums.ListingSortType;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.ListingType;
import com.stubhub.domain.account.common.enums.SaleMethod;
import com.stubhub.domain.account.common.enums.SaleStatus;
import com.stubhub.domain.account.common.enums.SortColumnType;
import com.stubhub.domain.account.common.enums.SortOrderType;
import com.stubhub.domain.account.common.enums.SplitOption;
import com.stubhub.domain.account.common.enums.TicketMedium;
import com.stubhub.domain.account.common.enums.TicketTrait;

public class GenericGetSetTest {
	@Test
	public void testGetterSetters()
	throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException {
		testGetSet(new com.stubhub.domain.account.common.Error());

		
		testGetSet(new ListingError(ErrorType.BUSINESSERROR, ErrorCode.INVALID_SELLER,"", ""));
	
		testGetSet(new ListingSearchCriteria());	
		testGetSet(new PaginationInput());
		testGetSet(new Response());
		testGetSet(new SortingDirective());
		testGetSet(new Error());		
		testGetSet(new TicketTrait());
		
		testGetSet(new SalesSearchCriteria());

		DeliveryOption.fromString(DeliveryOption.BARCODE.name());
		DeliveryOption.fromString("Barcode");
		DeliveryOption.fromString(DeliveryOption.LMS.name());
		DeliveryOption.fromString(DeliveryOption.PDF.name());
		DeliveryOption.fromString(DeliveryOption.UPS.name());
		
				
		com.stubhub.domain.account.common.enums.ErrorType.BUSINESSERROR.name();
		com.stubhub.domain.account.common.enums.ErrorType.SYSTEMERROR.name();
		com.stubhub.domain.account.common.enums.ErrorType.INPUTERROR.name();
		
		ListingSortType.fromString(ListingSortType.EVENT.name());
		ListingSortType.fromString(ListingSortType.EVENTDATE.name());
		ListingSortType.fromString(ListingSortType.INHANDDATE.name());
		ListingSortType.fromString(ListingSortType.PRICE.name());
		ListingSortType.fromString(ListingSortType.QUANTITY.name());
		ListingSortType.fromString(ListingSortType.QUANTITY_REMAIN.name());
		ListingSortType.fromString(ListingSortType.SECTION.name());
		ListingSortType.fromString(ListingSortType.STATUS.name());
		
		SortColumnType.EVENT_DATE_LOCAL.name();
		SortColumnType.EVENT_DESCRIPTION.name();
		SortColumnType.EXPECTED_INHAND_DATE.name();
		SortColumnType.QUANTITY.name();
		SortColumnType.QUANTITY_REMAIN.name();
		SortColumnType.SECTION.name();
		SortColumnType.TICKET_PRICE.name();
		SortColumnType.TICKET_SYSTEM_STATUS.name();
		
		SortOrderType.fromString(SortOrderType.ASCENDING.name());
		SortOrderType.fromString(SortOrderType.DESCENDING.name());
		
		SplitOption.fromString(SplitOption.NOSINGLES.name());
		SplitOption.fromString(SplitOption.MULTIPLES.name());
		SplitOption.fromString(SplitOption.NONE.name());
		SplitOption.fromString("0");
		SplitOption.fromString("1");
		SplitOption.fromString("2");
		
		TicketMedium.BARCODE.name();
		TicketMedium.PAPER.name();
		TicketMedium.PDF.name();
		TicketMedium.getTicketMedium(1).equals(TicketMedium.PAPER);
		
		SaleMethod.FixedPrice.name();
		SaleMethod.DECLINING.name();
		SaleMethod.getSaleMethod(1L).equals(SaleMethod.FixedPrice);
		
		LMSOption.APPROVED.name();
		LMSOption.NONE.name();
		LMSOption.PENDINGAPPROVAL.name();
		LMSOption.REJECTED.name();
		LMSOption.REMOVED.name();
		
		LMSOption.getLMSOption(2).equals(LMSOption.APPROVED);;
		
		ListingStatus.ACTIVE.name();
		ListingStatus.DELETED.name();
		ListingStatus.EXPIRED.name();
		ListingStatus.INACTIVE.name();
		ListingStatus.INCOMPLETE.name();
		ListingStatus.PENDING.name();
		
		ListingStatus.fromValue("ACTIVE").equals(ListingStatus.ACTIVE);
		
		ListingFilterType.DELIVERYOPTION.name();
		ListingFilterType.EVENT.name();
		ListingFilterType.EVENTDATE.name();
		ListingFilterType.GEOGRAPYID.name();
		ListingFilterType.INHANDDATE.name();
		ListingFilterType.PRICE.name();
		ListingFilterType.Q.name();
		ListingFilterType.QUANTITY.name();
		ListingFilterType.QUANTITY_REMAIN.name();
		ListingFilterType.SALEENDDATE.name();
		ListingFilterType.SECTION.name();
		ListingFilterType.STATUS.name();
		
		SaleStatus.CANCELLED.name();
		SaleStatus.CONFIRMED.name();
		SaleStatus.DELIVERED.name();
		SaleStatus.DELIVERYEXCEPTION.name();
		SaleStatus.ONHOLD.name();
		SaleStatus.PENDING.name();
		SaleStatus.PENDINGREVIEW.name();
		SaleStatus.SHIPPED.name();
		SaleStatus.SUBSOFFERED.name();	
		
		
		SaleStatus.fromString("CANCELLED");
		
	}
	
	private void testGetSet(Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Object> map = cast(PropertyUtils.describe(bean));
		Set<String> fields = map.keySet();
		for (Object o : fields) {
			String key = (String) o;
			Object value = map.get(o);
			String message = key + " = " + value;
			if (!key.equals("class")) {
				PropertyUtils.setSimpleProperty(bean, key, value);
				Object vvalue = PropertyUtils.getSimpleProperty(bean, key);
				message += " (" + vvalue + ")";
			}

		}
		
	}

	@SuppressWarnings("unchecked")
	private static final < X > X cast( Object o )
	{
		return ( X ) o;
	}
	
	@Test
	public void testListingType(){
		Assert.assertEquals(ListingType.PARKING_PASS.name(), ListingType.getEnum("PARKING_PASS").name());
		Assert.assertEquals(ListingType.PARKING_PASS.getId(), ListingType.getEnum("PARKING_PASS").getId());
	}
}
