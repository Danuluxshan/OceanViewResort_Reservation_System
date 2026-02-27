package com.oceanview.controller;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("jsp/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean hasError = false;

        request.setAttribute("username", username);

        if (username == null || username.isEmpty()) {
            request.setAttribute("usernameError", "Username is required");
            hasError = true;
        }

        if (password == null || password.isEmpty()) {
            request.setAttribute("passwordError", "Password is required");
            hasError = true;
        }

        if (hasError) {
            request.getRequestDispatcher("jsp/login.jsp")
                    .forward(request, response);
            return;
        }

        String encryptedPassword = PasswordUtil.hashPassword(password);

        UserDAO dao = new UserDAO();
        User user = dao.login(username, encryptedPassword);

        if (user == null) {
            request.setAttribute("generalError", "Invalid Username or Password");
            request.getRequestDispatcher("jsp/login.jsp")
                    .forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("loggedUser", user);
        session.setAttribute("role", user.getRole());

        // Role-based redirect
        switch (user.getRole()) {
            case "ADMIN":
                response.sendRedirect("jsp/adminDashboard.jsp");
                break;
            case "RECEPTIONIST":
                response.sendRedirect("jsp/receptionistDashboard.jsp");
                break;
            case "GUEST":
                response.sendRedirect("jsp/guestDashboard.jsp");
                break;
        }
    }
}
