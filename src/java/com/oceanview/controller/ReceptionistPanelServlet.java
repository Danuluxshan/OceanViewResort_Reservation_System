package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/receptionist")
public class ReceptionistPanelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            !"RECEPTIONIST".equals(session.getAttribute("role"))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        ReservationDAO reservationDAO = new ReservationDAO();
        UserDAO userDAO = new UserDAO();
        RoomDAO roomDAO = new RoomDAO();

        // ==========================
        // DASHBOARD
        // ==========================
        if ("dashboard".equals(action) || action == null) {

            request.setAttribute("todayReservations",
                    reservationDAO.getTodayReservationsCount());

            request.setAttribute("todayCheckins",
                    reservationDAO.getTodayCheckinCount());

            request.setAttribute("pendingPayments",
                    reservationDAO.getPendingPaymentsCount());

            request.setAttribute("totalBookings",
                    reservationDAO.getAllBookings().size());

            request.setAttribute("contentPage",
                    "receptionistDashboard.jsp");
        }

        // ==========================
        // MANAGE GUESTS
        // ==========================
        else if ("manageGuests".equals(action)) {

            request.setAttribute("guests",
                    userDAO.getTotalUsers());

            request.setAttribute("contentPage",
                    "manageGuests.jsp");
        }

        // ==========================
        // MANAGE RESERVATIONS
        // ==========================
        else if ("manageReservations".equals(action)) {

            request.setAttribute("reservations",
                    reservationDAO.getAllReservations());

            request.setAttribute("contentPage",
                    "manageReservationContent.jsp");
        }

        // ==========================
        // MANAGE BOOKINGS
        // ==========================
        else if ("manageBookings".equals(action)) {

            String bookingId = request.getParameter("bookingId");
            String statusFilter = request.getParameter("statusFilter");

            if (bookingId != null && !bookingId.isEmpty()) {

                request.setAttribute("bookings",
                        reservationDAO.searchBookingById(bookingId));

            } else if (statusFilter != null && !statusFilter.isEmpty()) {

                request.setAttribute("bookings",
                        reservationDAO.filterBookingsByStatus(statusFilter));

            } else {

                request.setAttribute("bookings",
                        reservationDAO.getAllBookings());
            }

            request.setAttribute("contentPage",
                    "manageBookings.jsp");
        }

        // ==========================
        // REPORTS
        // ==========================
        else if ("reports".equals(action)) {

            request.setAttribute("monthlyRevenue",
                    reservationDAO.getMonthlyRevenue());

            request.setAttribute("roomReport",
                    reservationDAO.getRoomOccupancyReport());

            request.setAttribute("contentPage",
                    "reports.jsp");
        }

        // ==========================
        // PROFILE
        // ==========================
        else if ("profile".equals(action)) {

            User loggedUser =
                    (User) session.getAttribute("loggedUser");

            request.setAttribute("user", loggedUser);

            request.setAttribute("contentPage",
                    "receptionistProfile.jsp");
        }

        // ==========================
        // DEFAULT
        // ==========================
        else {

            request.setAttribute("contentPage",
                    "receptionistDashboard.jsp");
        }

        request.getRequestDispatcher("jsp/receptionist/layout.jsp")
                .forward(request, response);
    }
}