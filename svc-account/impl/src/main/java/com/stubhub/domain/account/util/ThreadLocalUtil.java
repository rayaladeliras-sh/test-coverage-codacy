package com.stubhub.domain.account.util;

public class ThreadLocalUtil {
    private static final ThreadLocal <Boolean> inventoryThreadLocal=new ThreadLocal<Boolean>();

    public static Boolean isCallerTypeInternal () {
        return null != inventoryThreadLocal.get() && inventoryThreadLocal.get();
    }

    public static void setCallerTypeToInternal (boolean flag) {
        inventoryThreadLocal.set(flag);
    }
}
