package com.stubhub.domain.account.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MCIHeaderThreadLocal {
    private static final Logger logger = LoggerFactory.getLogger(MCIHeaderThreadLocal.class);
    private static final ThreadLocal<Map<String,Enumeration<String>>> INSTANCE = new InheritableThreadLocal<Map<String,Enumeration<String>>>() {
        protected Map<String, Enumeration<String>> initialValue() {
            return new HashMap();
        }
    };

    public static void set(String key, Enumeration<String> value) {
        if (null!=value) {
            ((Map)INSTANCE.get()).put(key, value);
        }

    }

    public static void removeAll(){
        INSTANCE.remove();
    }

    public static Map<String,Enumeration<String>> get() {
        return INSTANCE.get();
    }
}
