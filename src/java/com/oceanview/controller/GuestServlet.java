package com.oceanview.controller;

import com.oceanview.dao.GuestDAO;
import com.oceanview.model.User;
import com.oceanview.pattern.behavioral.ReceptionistIterator;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/manageGuests")
public class GuestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        GuestDAO dao = new GuestDAO();

        String action = request.getParameter("action");

        if ("toggle".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");

            dao.toggleStatus(id, status);
            response.sendRedirect(request.getContextPath() + "/manageGuests");
            return;
        }

        List<User> list = dao.getAllGuests();

        ReceptionistIterator iterator =
                new ReceptionistIterator(list);

        request.setAttribute("iterator", iterator);
        request.setAttribute("contentPage",
                "manageGuestsContent.jsp");

        request.getRequestDispatcher("jsp/admin/layout.jsp")
                .forward(request, response);
    }
}