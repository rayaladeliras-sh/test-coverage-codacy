package com.stubhub.domain.account.datamodel.entity;



import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.testng.annotations.Test;

public class GenericGetSetTest {
	@Test
	public void testGetterSetters()
	throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException {
		
	
		DeliveryOption.getDeliveryOption(0).equals(DeliveryOption.NONE);
		DeliveryOption.getDeliveryOption(1).equals(DeliveryOption.PREDELIVERY);
		DeliveryOption.getDeliveryOption(2).equals(DeliveryOption.MANUAL);
		
		FulfillmentMethod.getFulfillmentMethodEnumByName("ups").equals(FulfillmentMethod.UPS);
		FulfillmentMethod.getFulfillmentMethodEnumByName("FedEx").equals(FulfillmentMethod.FedEx);
		FulfillmentMethod.getFulfillmentMethodEnumByName("Barcode").equals(FulfillmentMethod.Barcode);
		FulfillmentMethod.getFulfillmentMethodEnumByName("PDF").equals(FulfillmentMethod.PDF);
		FulfillmentMethod.getFulfillmentMethodEnumByName("LMS").equals(FulfillmentMethod.LMS);
		
		
				
		
		TicketMedium.BARCODE.name();
		TicketMedium.PAPER.name();
		TicketMedium.PDF.name();
		TicketMedium.getTicketMedium(1).equals(TicketMedium.PAPER);
		
		
		ListingStatus.ACTIVE.name();
		ListingStatus.DELETED.name();
		ListingStatus.PENDING_PDF_REVIEW.name();
		ListingStatus.INACTIVE.name();
		ListingStatus.INCOMPLETE.name();
		ListingStatus.PENDING_LOCK.name();
		
		PaymentType.Charity.getDescription();
		PaymentType.Check.getDescription();
		PaymentType.LargeSellerCheck.getDescription();
		PaymentType.CreditCard.getDescription();
		PaymentType.Paypal.getDescription();
		PaymentType.SeasonTicketAccount.getDescription();
		
		
		
		
		
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
}
