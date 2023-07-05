/**
 * Copyright 2016 StubHub, Inc.  All rights reserved.
 */
package com.stubhub.domain.account.biz.impl;

import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * MCIRequestUtil
 * 
 * @author runiu
 *
 */
public abstract class MCIRequestUtil {
	public static final String RIGHT_BRACKET = ")";
	public static final String LEFT_BRACKET = "(";
	public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String DEFAULT_SEARCH_MCI_API_V1 = "http://api-int.stubprod.com/search/mci/v1";
	public static final String SEARCH_MCI_API_URL_PROP_NAME = "account.v1.search.mci.api.url";
	public static final String SEARCH_MCI_API_TIME_OUT_PROP_NAME = "account.v1.search.mci.api.timeout";
	public static final String DEFAULT_SEARCH_MCI_API_TIME_OUT = "12000";
	public static final int DEFAULT_SEARCH_MCI_API_TIME_OUT_INT = 12000;
	public static final String NON_ALPHABET_RGEX = "[^a-zA-Z0-9]";
	public static final String BLANK = "";
	public static final String COMMA = ",";
	public static final String DATE_FORMAT_WITH_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

	/* keyword for mci api request */
	public static final String EXIST = "exist";
	public static final String MUST = "must";
	public static final String SHOULD = "should";
	public static final String FILTER = "filter";
	private static final String ORDER = "order";
	public static final String SORT = "sort";
	public static final String AGGREGATIONS = "aggregations";
	private static final String FACET = "facet";
	private static final String STATS = "stats";
	private static final String JSON_FACET = "json.facet";
	private static final String FIELD = "field";
	public static final String ROWS = "rows";
	public static final String START = "start";
	private static final String QUERY = "query";
	private static final String TERM = "term";
	private static final String VALUE = "value";
	private static final String TO = "to";
	private static final String FROM = "from";
	private static final String RANGE = "range";
	private static final String NOT = "not";
	private static final String MATCH = "match";
	private static final String IN = "in";
	private static final String VALUES = "values";
	private static ObjectMapper om = new ObjectMapper();

	public static String toCamelCase(String columnName) {
		boolean afterUnderscore = false;
		StringBuilder sb = new StringBuilder(columnName.length());
		for (int i = 0; i < columnName.length(); i++) {
			char ch = columnName.charAt(i);
			if (ch == '_') {
				afterUnderscore = true;
			} else if (afterUnderscore) {
				sb.append(Character.toUpperCase(ch));
				afterUnderscore = false;
			} else {
				sb.append(Character.toLowerCase(ch));
				afterUnderscore = false;
			}
		}

		return sb.toString();
	}

	public static ObjectNode sort(String fieldName, SolrQuery.ORDER order) {
		ObjectNode v = om.createObjectNode();
		ObjectNode k = om.createObjectNode();
		k.put(ORDER, order.name());
		v.put(fieldName, k);
		return v;
	}

	public static ObjectNode facet(String name) {
		ObjectNode facet = om.createObjectNode();
		ObjectNode v = om.createObjectNode();
		ObjectNode k = om.createObjectNode();
		k.put(FIELD, name);
		v.put(FACET, k);
		facet.put(name, v);
		return facet;
	}

	public static ObjectNode stats(String name) {
		ObjectNode stats = om.createObjectNode();
		ObjectNode v = om.createObjectNode();
		ObjectNode k = om.createObjectNode();
		k.put(FIELD, "{!percentiles=50 mean=true min=true max=true}"+name);
		v.put(STATS, k);
		stats.put(name, v);
		return stats;
	}

	public static ObjectNode stats(String field, String facet) {
		ObjectNode stats = om.createObjectNode();
		ObjectNode v = om.createObjectNode();
		ObjectNode k = om.createObjectNode();
		k.put(FIELD, field);
		k.put(FACET, facet);
		v.put(STATS, k);
		stats.put(field, v);
		return stats;
	}
	
	public static ObjectNode jsonFacet(ObjectNode jsonFacetNode) {
		ObjectNode facet = om.createObjectNode();
		facet.put(JSON_FACET, jsonFacetNode);
		return facet;
	}


	public static void termQuery(String q, ObjectNode rootNode) {
		if (StringUtils.isNotEmpty(q)) {
			ObjectNode queryNode = om.createObjectNode();
			ObjectNode termNode = om.createObjectNode();
			termNode.put(VALUE, q);
			queryNode.put(TERM, termNode);
			rootNode.put(QUERY, queryNode);
		}
	}

	public static void pagination(int start, int rows, ObjectNode rootNode) {
		if (start > -1) {
			rootNode.put(START, start);
		}
		if (rows > -1) {
			rootNode.put(ROWS, rows);
		}
	}

	public static ObjectNode not(ObjectNode valueNode) {
		ObjectNode node = om.createObjectNode();
		node.put(NOT, valueNode);
		return node;
	}

	public static ObjectNode range(String fieldName, String fromValue, String toValue) {
		ObjectNode node = om.createObjectNode();
		ObjectNode rangeNode = om.createObjectNode();
		ObjectNode valueNode = om.createObjectNode();
		node.put(RANGE, rangeNode);
		rangeNode.put(fieldName, valueNode);
		if (fromValue != null && !"*".equals(fromValue)) {
			valueNode.put(FROM, fromValue);
		}
		if (toValue != null && !"*".equals(toValue)) {
			valueNode.put(TO, toValue);
		}
		return node;
	}

	public static ObjectNode createNode(String fieldName, ArrayNode arrayNode) {
		ObjectNode node = om.createObjectNode();
		node.put(fieldName, arrayNode);
		return node;
	}

	public static ObjectNode match(String fieldName, String fieldValue) {
		ObjectNode node = om.createObjectNode();
		ObjectNode valueNode = om.createObjectNode();
		valueNode.put(fieldName, fieldValue);
		node.put(MATCH, valueNode);
		return node;
	}

	public static ObjectNode exist(String fieldName) {
		ObjectNode node = om.createObjectNode();
		node.put(EXIST, fieldName);
		return node;
	}

	public static ObjectNode in(String fieldName, String... fieldValues) {
		ObjectNode node = om.createObjectNode();
		ObjectNode inNode = om.createObjectNode();
		ObjectNode valueNode = om.createObjectNode();
		node.put(IN, inNode);
		inNode.put(fieldName, valueNode);
		ArrayNode valuesNode = om.createArrayNode();
		valueNode.put(VALUES, valuesNode);
		for (String fieldValue : fieldValues) {
			valuesNode.add(fieldValue);
		}
		return node;
	}

	public static ObjectNode in(String fieldName, Iterable<String> fieldValues) {
		ObjectNode node = om.createObjectNode();
		ObjectNode inNode = om.createObjectNode();
		ObjectNode valueNode = om.createObjectNode();
		node.put(IN, inNode);
		inNode.put(fieldName, valueNode);
		ArrayNode valuesNode = om.createArrayNode();
		valueNode.put(VALUES, valuesNode);
		for (String fieldValue : fieldValues) {
			valuesNode.add(fieldValue);
		}
		return node;
	}
}
