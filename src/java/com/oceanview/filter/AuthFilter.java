package com.oceanview.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/jsp/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();

        // Allow login and register pages without session
        if (uri.contains("login.jsp") || uri.contains("register.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("loggedUser") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        // Role restrictions
        if (uri.contains("adminDashboard") && !role.equals("ADMIN")) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (uri.contains("receptionistDashboard") && !role.equals("RECEPTIONIST")) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (uri.contains("guestDashboard") && !role.equals("GUEST")) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}