package com.stubhub.domain.account.context;

import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

@Component("MCIHeaderFilter")
public class MCIHeaderFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(MCIHeaderFilter.class);
    private static String MCI_HEADERS="mci.headers";
    private static String MCI_QUERY_PARAMS="mci.query.params";

    @Autowired
    private MasterStubhubPropertiesWrapper masterStubhubProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            LOG.info("method=MCIHeaderFilter.doFilterInternal message=start uri="+httpServletRequest.getRequestURI());
            processMCIHeaders(httpServletRequest);
            processMCIQueryString(httpServletRequest);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }finally {
            MCIHeaderThreadLocal.removeAll();
            MCIQueryParamThreadLocal.removeAll();
            LOG.info("method=MCIHeaderFilter.doFilterInternal message=cleanedThreadLocal");
        }
    }

    private void processMCIQueryString(HttpServletRequest httpServletRequest) {
        Set<String> mciParams=getQueryParams();
        String mciParamValue;
        for(String mciParam:mciParams){
            mciParamValue=getParam(httpServletRequest,mciParam);
            if(null!=mciParamValue){
                LOG.info("method=MCIHeaderFilter.doFilterInternal uri={} message=detected mciQueryParam-{}",httpServletRequest.getRequestURI(),mciParam);
                try {
                    MCIQueryParamThreadLocal.set(mciParam, URLEncoder.encode(mciParamValue,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    LOG.error("method=MCIHeaderFilter.doFilterInternal uri={} message=UnsupportedEncodingException",httpServletRequest.getRequestURI());
                }
            }
        }
    }

    private String getParam(HttpServletRequest httpServletRequest,String mciParamName){
        Enumeration<String> paramNames= httpServletRequest.getParameterNames();
        String paramName;
        if(null!=paramNames) {
            while(paramNames.hasMoreElements()){
                paramName=paramNames.nextElement();
                if(mciParamName.equalsIgnoreCase(paramName)){
                    return httpServletRequest.getParameter(paramName);
                }
            }
        }
        return null;
    }

    private Set<String>  getQueryParams() {
        Set<String> mciParams=new HashSet<String>();
        String mciQueryParams=masterStubhubProperties.getProperty(MCI_QUERY_PARAMS);
        if(!StringUtils.isEmpty(mciQueryParams)){
            mciParams.addAll(Arrays.asList(mciQueryParams.split(",")));
        }
        return mciParams;
    }

    private void processMCIHeaders(HttpServletRequest httpServletRequest){
        String mciHeaders=masterStubhubProperties.getProperty(MCI_HEADERS);
        Enumeration<String> value;
        for(String header:mciHeaders.split(",")){
            value=httpServletRequest.getHeaders(header);
            if(null!=value&&value.hasMoreElements()){
                LOG.info("method=MCIHeaderFilter.doFilterInternal uri={} message=detected header-{}",httpServletRequest.getRequestURI(),header);
                MCIHeaderThreadLocal.set(header,value);
            }
        }
    }
}
