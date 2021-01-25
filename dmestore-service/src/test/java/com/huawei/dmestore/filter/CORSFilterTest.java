package com.huawei.dmestore.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lianq
 * @className CORSFilterTest
 * @description TODO
 * @date 2020/12/1 19:28
 */
public class CORSFilterTest {
    @InjectMocks
    CorsFilter corsFilter = new CorsFilter();

    FilterConfig filterConfig;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        filterConfig = spy(FilterConfig.class);
    }

    @Test
    public void init() throws ServletException {
        corsFilter.init(filterConfig);
    }

    @Test
    public void doFilter() throws IOException, ServletException {
        HttpServletRequest request = spy(HttpServletRequest.class);
        HttpServletResponse response = spy(HttpServletResponse.class);
        FilterChain filterChain = spy(FilterChain.class);
        when(request.getMethod()).thenReturn("OPTIONS");
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        corsFilter.doFilter(request,response,filterChain);
    }

    @Test
    public void destroy() {
        corsFilter.destroy();
    }
}