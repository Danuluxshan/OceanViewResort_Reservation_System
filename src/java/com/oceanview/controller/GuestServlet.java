package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/guest")
public class GuestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !"GUEST".equals(session.getAttribute("role"))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        ReservationDAO reservationDAO = new ReservationDAO();

        // ======================
        // DASHBOARD
        // ======================
        if ("dashboard".equals(action) || action == null) {

            request.setAttribute("totalReservations",
                    reservationDAO.getGuestReservationCount(loggedUser.getId()));

            request.setAttribute("totalBookings",
                    reservationDAO.getGuestBookingCount(loggedUser.getId()));

            request.setAttribute("contentPage",
                    "guestDashboard.jsp");
        } // ======================
        // MY RESERVATIONS
        // ======================
        else if ("reservations".equals(action)) {

            request.setAttribute("reservations",
                    reservationDAO.getReservationsByGuest(loggedUser.getId()));

            request.setAttribute("contentPage", "myReservations.jsp");
        } // ======================
        // MY BOOKINGS
        // ======================
        else if ("bookings".equals(action)) {

            request.setAttribute("bookings",
                    reservationDAO.getBookingsByGuest(loggedUser.getId()));

            request.setAttribute("contentPage", "myBookings.jsp");
        } // ======================
        // PROFILE
        // ======================
        else if ("profile".equals(action)) {
            request.setAttribute("user", loggedUser);
            request.setAttribute("contentPage",
                    "guestProfile.jsp");

            request.getRequestDispatcher("jsp/guest/layout.jsp")
                    .forward(request, response);
            return;
        } // ======================
        // ADD RESERVATION SCREEN
        // ======================
        else if ("addReservation".equals(action)) {

            request.setAttribute("contentPage", "addReservation.jsp");
        } else if ("help".equals(action)) {

            request.setAttribute("contentPage", "help.jsp");

            request.getRequestDispatcher("jsp/guest/layout.jsp")
                    .forward(request, response);
            return;
        }

        request.getRequestDispatcher("jsp/guest/layout.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !"GUEST".equals(session.getAttribute("role"))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User loggedUser = (User) session.getAttribute("user");

        try {

            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");

            java.time.LocalDate checkIn
                    = java.time.LocalDate.parse(checkInStr);

            java.time.LocalDate checkOut
                    = java.time.LocalDate.parse(checkOutStr);

            com.oceanview.dao.RoomDAO roomDAO
                    = new com.oceanview.dao.RoomDAO();

            com.oceanview.dao.ReservationDAO reservationDAO
                    = new com.oceanview.dao.ReservationDAO();

            com.oceanview.model.Room room
                    = roomDAO.getRoomById(roomId);

            long days = java.time.temporal.ChronoUnit.DAYS
                    .between(checkIn, checkOut);

            if (days <= 0) {
                days = 1;
            }

            double totalAmount
                    = days * room.getPricePerNight();

            com.oceanview.model.Reservation reservation
                    = new com.oceanview.model.Reservation.Builder()
                            .setReservationNumber(
                                    com.oceanview.util.ReservationNumberGenerator
                                            .generateReservationNumber())
                            .setRoomId(roomId)
                            .setGuestId(loggedUser.getId())
                            .setCheckIn(checkIn)
                            .setCheckOut(checkOut)
                            .setTotalAmount(totalAmount)
                            .build();

            reservationDAO.addReservation(reservation);

            response.sendRedirect(
                    request.getContextPath()
                    + "/guest?action=reservations");

        } catch (Exception e) {
            e.printStackTrace();
        }

        String profileAction = request.getParameter("profileAction");

        UserDAO userDAO = new UserDAO();

// =====================
// UPDATE PROFILE
// =====================
        if ("updateProfile".equals(profileAction)) {

            loggedUser.setFullName(request.getParameter("fullName"));
            loggedUser.setEmail(request.getParameter("email"));
            loggedUser.setContact(request.getParameter("contact"));
            loggedUser.setAddress(request.getParameter("address"));

            userDAO.updateGuestProfile(loggedUser);

            session.setAttribute("user", loggedUser);

            request.setAttribute("success",
                    "Profile updated successfully!");

            request.setAttribute("contentPage",
                    "guestProfile.jsp");

            request.getRequestDispatcher("jsp/guest/layout.jsp")
                    .forward(request, response);
            return;
        }

// =====================
// CHANGE PASSWORD
// =====================
        if ("changePassword".equals(profileAction)) {

            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            // 🔥 Get fresh password from DB
            String dbPassword = userDAO.getPasswordByUserId(loggedUser.getId());

            String hashedCurrent
                    = PasswordUtil.hashPassword(currentPassword);

            if (!hashedCurrent.equals(dbPassword)) {

                request.setAttribute("error",
                        "Current password incorrect");

            } else if (!newPassword.equals(confirmPassword)) {

                request.setAttribute("error",
                        "New passwords do not match");

            } else {

                String hashedNew
                        = PasswordUtil.hashPassword(newPassword);

                userDAO.updatePassword(loggedUser.getId(), hashedNew);

                request.setAttribute("success",
                        "Password changed successfully");
            }

            request.setAttribute("contentPage", "guestProfile.jsp");
            request.getRequestDispatcher("jsp/guest/layout.jsp")
                    .forward(request, response);
            return;
        }
// =====================
// DEACTIVATE ACCOUNT
// =====================
        if ("deactivate".equals(profileAction)) {

            userDAO.deactivateAccount(loggedUser.getId());

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }
    }
}
