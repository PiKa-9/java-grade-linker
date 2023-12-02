package com.gradeLinker.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class UserFilter implements Filter {

    /* Filter for checking if user session is valid */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/error");
            return;
        }

        String username = (String) session.getAttribute("username");
        if (username == null) {
            ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/error");
            return;
        }

        chain.doFilter(request, response);
    }
}
