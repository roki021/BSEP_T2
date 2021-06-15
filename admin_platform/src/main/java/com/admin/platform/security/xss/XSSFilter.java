package com.admin.platform.security.xss;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class XSSFilter implements Filter {
    private static final ArrayList<String> permitted = new ArrayList<>() {{
        add("/ocsp");
    }};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        XSSRequestWrapper wrappedRequest =
               new XSSRequestWrapper((HttpServletRequest) request);
        String body = IOUtils.toString(wrappedRequest.getReader());
        String path = ((HttpServletRequest) request).getServletPath();
        if(!permitted.contains(path)) {
            if (!StringUtils.isBlank(body)) {
                body = XSSUtils.stripXSS(body);
                wrappedRequest.resetInputStream(body.getBytes());
            }
        }

        chain.doFilter(wrappedRequest, response);
    }

    // other methods
}