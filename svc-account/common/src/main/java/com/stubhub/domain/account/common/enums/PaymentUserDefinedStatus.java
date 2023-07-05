/**
 * 
 */
package com.stubhub.domain.account.common.enums;


public enum PaymentUserDefinedStatus {
    PENDING(1L, "Pending"),
    CANCELLED(2L, "Cancelled"),
    SENT(3L, "Sent"),
    PROCESSING(4L,"Processing"),
    AVAILABLE(5L,"Available"),
    UNCLAIMED(6L,"Unclaimed"),
    APPLIED(7L,"Applied"),
    CHARGED(8L,"Charged"),
    REJECTED(9L,"Rejected");

    private Long id;
    private String name;

    PaymentUserDefinedStatus(Long id, String name) {
    	this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    /**
     * 
     * @param id
     * @return MyaSellerPaymentStatusEnum
     */
    public static PaymentUserDefinedStatus getEnum(Long id) {
    	if (id == null) {
            return null;
        }
    	
        PaymentUserDefinedStatus list[] = PaymentUserDefinedStatus.values();
		for (int i = 0; i < list.length; i++) {
			PaymentUserDefinedStatus obj = list[i];
			if (obj.getId().equals(id)) {
				return obj;
			}
		}
		return null;
    }
    
    /**
     * 
     * @param name
     * @return MyaSellerPaymentStatusEnum
     */
    public static PaymentUserDefinedStatus getEnum(String name) {
        if (name == null) {
            return null;
        }

        PaymentUserDefinedStatus list[] = PaymentUserDefinedStatus.values();
		for (int i = 0; i < list.length; i++) {
			PaymentUserDefinedStatus obj = list[i];
			if (obj.getName().equalsIgnoreCase(name)) {
				return obj;
			}
		}
		return null;
    }
    
}
