package com.stubhub.domain.account.datamodel.entity;

public enum PaymentType {
    Paypal(1L,"PayPal"),
    Check(2L,"Check"),
    SeasonTicketAccount(3L,"Credit My Season Ticket Account"),
    ACH(5L,"ACH"),
    CreditCard(521L,"Credit Card"),
    LargeSellerCheck(821L,"Check - Large Sellers"),
    Charity(41L, "Charity"),
    DoNotPay(21L, "Do Not Pay");
	
    private Long type;
    private String description;
    
    PaymentType( Long type,String description) {    
    	this.type=type;
    	this.description=description;
    }
       
    public final static PaymentType Default=Check;

	public Long getType() {
		return type;
	}
	public String getDescription() {
		return description;
	}
	public final static PaymentType getPaymentType(Long type){
		return getPaymentType(type,null);
	}
    
	public final static PaymentType getPaymentType(Long type,PaymentType defaultValue){
		PaymentType[] paymentTypes=PaymentType.values();
		for(PaymentType paymentType:paymentTypes){
			if(type!=null && paymentType.getType().longValue()==type.longValue())
				return paymentType;
		}
		return defaultValue;
	}
}