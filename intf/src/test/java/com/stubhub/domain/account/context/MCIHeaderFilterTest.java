package com.stubhub.domain.account.context;

import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import static org.mockito.Mockito.when;

public class MCIHeaderFilterTest {
    private MCIHeaderFilter filter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private MasterStubhubPropertiesWrapper masterStubhubProperties;


    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filter = new MCIHeaderFilter();
        masterStubhubProperties = Mockito.mock(MasterStubhubPropertiesWrapper.class);
        ReflectionTestUtils.setField(filter, "masterStubhubProperties", masterStubhubProperties);
    }

    @Test
    public void testDoFilterInternal() throws ServletException, IOException {
        when(masterStubhubProperties.getProperty("mci.headers")).thenReturn("Prefer");
        Enumeration<String> enumeration = Collections.enumeration(Arrays.asList("test"));
        when(request.getHeaders("Prefer")).thenReturn(enumeration);
        filter.doFilterInternal(request, response, filterChain);
    }

    @Test
    public void testDoFilterInternalWithOutMCIHeacder() throws ServletException, IOException {
        when(masterStubhubProperties.getProperty("mci.headers")).thenReturn("Prefer");
        Enumeration<String> enumeration = Collections.enumeration(Collections.EMPTY_LIST);
        when(request.getHeaders("Prefer")).thenReturn(enumeration);
        filter.doFilterInternal(request, response, filterChain);
    }

    @Test
    public void testMciQueryParams() throws ServletException, IOException {
        when(masterStubhubProperties.getProperty("mci.query.params")).thenReturn("shStore");
        Enumeration<String> enumeration = Collections.enumeration(Arrays.asList("shstore"));
        when(request.getParameterNames()).thenReturn(enumeration);
        when(request.getParameter("shstore")).thenReturn("123");
        filter.doFilterInternal(request, response, filterChain);

        enumeration = Collections.enumeration(Arrays.asList("abc"));
        when(request.getParameterNames()).thenReturn(enumeration);
        filter.doFilterInternal(request, response, filterChain);
    }

    @Test
    public void testMciQueryParamsWithoutMCIQuery() throws ServletException, IOException {
        when(masterStubhubProperties.getProperty("mci.query.params")).thenReturn("shStore");
        Enumeration<String> enumeration = Collections.enumeration(Collections.EMPTY_LIST);
        when(request.getParameterNames()).thenReturn(enumeration);
        when(request.getParameter("shstore")).thenReturn("123");
        filter.doFilterInternal(request, response, filterChain);
    }
}
