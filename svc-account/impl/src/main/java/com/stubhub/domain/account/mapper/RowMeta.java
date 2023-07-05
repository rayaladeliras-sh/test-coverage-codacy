package com.stubhub.domain.account.mapper;

import java.lang.reflect.Method;


public class RowMeta {
	private String[] fieldNames;
	
	private Class<?> fieldClass;
	
	private Method setMethod;	
	
	private DataMapper mapper;
	
	public RowMeta(String[] fieldNames, Class<?> fieldClass, Method setMethod, DataMapper mapper) {
		this.fieldNames = fieldNames;
		this.fieldClass = fieldClass;
		this.setMethod = setMethod;
		this.mapper = mapper;
	}

	public DataMapper getMapper() {
		return mapper;
	}

	public void setMapper(DataMapper mapper) {
		this.mapper = mapper;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public Class<?> getFieldClass() {
		return fieldClass;
	}

	public void setFieldClass(Class<?> fieldClass) {
		this.fieldClass = fieldClass;
	}

	public Method getSetMethod() {
		return setMethod;
	}

	public void setSetMethod(Method setMethod) {
		this.setMethod = setMethod;
	}	
}
