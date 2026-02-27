/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.oceanview.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.oceanview.dao.ReceptionistDAO;
import com.oceanview.model.User;
import com.oceanview.pattern.creational.UserFactory;
import com.oceanview.pattern.behavioral.ReceptionistIterator;
import com.oceanview.util.PasswordUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/manageReceptionists")
public class ReceptionistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ReceptionistDAO dao = new ReceptionistDAO();

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            User editUser = dao.getReceptionistById(id);
            request.setAttribute("editUser", editUser);
        }

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteReceptionist(id);
            response.sendRedirect(request.getContextPath() + "/manageReceptionists");
            return;
        }

        List<User> list = dao.getAllReceptionists();
        ReceptionistIterator iterator = new ReceptionistIterator(list);

        request.setAttribute("iterator", iterator);
        request.setAttribute("contentPage", "manageReceptionistsContent.jsp");

        request.getRequestDispatcher("jsp/admin/layout.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ReceptionistDAO dao = new ReceptionistDAO();

        String idStr = request.getParameter("id");

        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String contact = request.getParameter("contact");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        if (idStr == null || idStr.isEmpty()) {

            // ADD MODE
            User user = UserFactory.createUser("RECEPTIONIST");
            user.setFullName(fullName);
            user.setUsername(username);
            user.setEmail(email);
            user.setContact(contact);
            user.setAddress(address);
            user.setPassword(PasswordUtil.hashPassword(password));

            dao.addReceptionist(user);

        } else {

            // UPDATE MODE
            int id = Integer.parseInt(idStr);

            User user = new User();
            user.setId(id);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setContact(contact);
            user.setAddress(address);
            user.setPassword(PasswordUtil.hashPassword(password));

            dao.updateReceptionist(user);
        }

        response.sendRedirect(request.getContextPath() + "/manageReceptionists");
    }
}   