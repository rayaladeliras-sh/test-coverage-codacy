package com.stubhub.domain.account.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.stubhub.domain.account.mapper.DataMapper;
import com.stubhub.domain.account.mapper.DefaultDataMapper;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface ResponseField {
	String[] fieldName();

	String defaultValue() default "";
	
	Class<? extends DataMapper> mapperClass() default DefaultDataMapper.class;
}
