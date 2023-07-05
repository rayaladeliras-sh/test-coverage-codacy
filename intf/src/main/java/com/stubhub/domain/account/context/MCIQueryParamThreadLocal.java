package com.stubhub.domain.account.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MCIQueryParamThreadLocal {
    private static final Logger logger = LoggerFactory.getLogger(MCIQueryParamThreadLocal.class);
    private static final ThreadLocal<Map<String,String>> INSTANCE = new InheritableThreadLocal<Map<String,String>>() {
        protected Map<String, String> initialValue() {
            return new HashMap();
        }
    };

    public static void set(String key, String value) {
        if (null!=value) {
            ((Map)INSTANCE.get()).put(key, value);
        }
    }

    public static void removeAll(){
        INSTANCE.remove();
    }

    public static Map<String,String> get() {
        return INSTANCE.get();
    }
}
