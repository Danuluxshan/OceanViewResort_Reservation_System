package com.oceanview.controller;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    // 🔥 VERY IMPORTANT – ADD THIS
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("jsp/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String contact = request.getParameter("contact");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        boolean hasError = false;

        // Preserve entered values
        request.setAttribute("fullName", fullName);
        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("contact", contact);
        request.setAttribute("address", address);

        // Validation checks
        if (fullName == null || fullName.isEmpty()) {
            request.setAttribute("fullNameError", "Full Name is required");
            hasError = true;
        }

        if (username == null || username.isEmpty()) {
            request.setAttribute("usernameError", "Username is required");
            hasError = true;
        }

        if (email == null || !email.contains("@")) {
            request.setAttribute("emailError", "Enter valid email");
            hasError = true;
        }

        if (contact == null || !contact.matches("\\d{10}")) {
            request.setAttribute("contactError", "Contact must be 10 digits");
            hasError = true;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("confirmPasswordError", "Passwords do not match");
            hasError = true;
        }

        UserDAO dao = new UserDAO();

        if (dao.isUsernameExists(username)) {
            request.setAttribute("usernameError", "Username already exists");
            hasError = true;
        }

        if (hasError) {
            request.getRequestDispatcher("jsp/register.jsp")
                    .forward(request, response);
            return;
        }

        String encryptedPassword = PasswordUtil.hashPassword(password);

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setContact(contact);
        user.setAddress(address);
        user.setPassword(encryptedPassword);
        user.setRole("GUEST");

        if (dao.registerGuest(user)) {
            request.setAttribute("successMessage", "Registration Successful! Please Login.");
            request.getRequestDispatcher("jsp/login.jsp")
                    .forward(request, response);
        } else {
            request.setAttribute("generalError", "Registration Failed!");
            request.getRequestDispatcher("jsp/register.jsp")
                    .forward(request, response);
        }
    }
}
