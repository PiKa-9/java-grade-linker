package com.gradeLinker.web.filter;

import com.gradeLinker.domain.user.User;
import com.gradeLinker.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserFilter implements Filter {
    private final UserService userService;

    @Autowired
    public UserFilter(UserService userService) {
        this.userService = userService;
    }

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

        User user = userService.getUserByUsername(username);
        request.setAttribute("user", user);
        chain.doFilter(request, response);
    }
}
