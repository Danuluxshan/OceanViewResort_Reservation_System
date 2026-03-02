package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.UserDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.User;
import com.oceanview.util.ReservationNumberGenerator;
import com.oceanview.util.PasswordUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebServlet("/manageReservation")
public class ReservationServlet extends HttpServlet {

    // ============================
    // GET METHOD
    // ============================
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || (!"ADMIN".equals(session.getAttribute("role"))
                && !"RECEPTIONIST".equals(session.getAttribute("role")))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        RoomDAO roomDAO = new RoomDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        UserDAO userDAO = new UserDAO();

        String action = request.getParameter("action");
        String reservationNo = request.getParameter("reservationNo");

        if ("manageReservations".equals(action)) {

            request.setAttribute("reservations",
                    reservationDAO.getAllReservations());

            request.setAttribute("contentPage",
                    "manageReservationContent.jsp");

            forwardByRole(request, response, role);
            return;
        }

        // ============================
        // DASHBOARD
        // ============================
        if ("dashboard".equals(action)) {

            request.setAttribute("totalUsers", userDAO.getTotalUsers());
            request.setAttribute("totalGuests", userDAO.getTotalGuests());
            request.setAttribute("totalReceptionists", userDAO.getTotalReceptionists());
            request.setAttribute("activeReservations", reservationDAO.getActiveReservations());

            request.setAttribute("contentPage", "adminDashboardContent.jsp");

            forwardByRole(request, response, role);
            return;
        }

        // ============================
        // REPORTS
        // ============================
        if ("reports".equals(action)) {

            request.setAttribute("monthlyRevenue",
                    reservationDAO.getMonthlyRevenue());

            request.setAttribute("roomReport",
                    reservationDAO.getRoomOccupancyReport());

            request.setAttribute("contentPage", "reports.jsp");

            forwardByRole(request, response, role);
            return;
        }

        // ============================
        // CONFIRM PREVIEW
        // ============================
        if ("confirm".equals(action) && reservationNo != null) {

            request.setAttribute("confirmationDetails",
                    reservationDAO.searchByReservationNumber(reservationNo));

            request.setAttribute("contentPage", "confirmationScreen.jsp");

            forwardByRole(request, response, role);
            return;
        }

        // ============================
        // CANCEL RESERVATION
        // ============================
        if ("cancel".equals(action) && reservationNo != null) {

            reservationDAO.deleteReservation(reservationNo);

            response.sendRedirect(request.getContextPath() + "/manageReservation");
            return;
        }

        // ============================
        // BOOKING DETAILS
        // ============================
        if ("details".equals(action) && reservationNo != null) {

            request.setAttribute("bookingDetails",
                    reservationDAO.searchBookingDetails(reservationNo));

            request.setAttribute("contentPage", "bookingDetails.jsp");

            forwardByRole(request, response, role);
            return;
        }
        // ============================
// CANCEL BOOKING
// ============================
        if ("cancelBooking".equals(action)) {

            String bookingId = request.getParameter("bookingId");

            boolean result = reservationDAO.cancelBooking(bookingId);

            System.out.println("Cancel Booking Result: " + result);

            response.sendRedirect(
                    request.getContextPath()
                    + "/manageReservation?action=manageBookings");

            return;
        }

        // ============================
        // MANAGE BOOKINGS
        // ============================
        if ("manageBookings".equals(action)) {

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

            request.setAttribute("contentPage", "manageBookings.jsp");

            forwardByRole(request, response, role);
            return;
        }

        // ============================
        // CHECK AVAILABLE ROOMS
        // ============================
        String checkIn = request.getParameter("checkIn");
        String checkOut = request.getParameter("checkOut");

        if (checkIn != null && checkOut != null) {
            List<Room> availableRooms
                    = roomDAO.getAvailableRooms(checkIn, checkOut);

            request.setAttribute("availableRooms", availableRooms);
        }

        // ============================
        // GUEST SEARCH
        // ============================
        String searchValue = request.getParameter("searchValue");

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            User foundGuest
                    = userDAO.findGuestByEmailOrContact(searchValue.trim());

            request.setAttribute("foundGuest", foundGuest);
        }

        request.setAttribute("reservations",
                reservationDAO.getAllReservations());

        request.setAttribute("contentPage",
                "manageReservationContent.jsp");

        forwardByRole(request, response, role);
    }

    // ============================
    // POST METHOD
    // ============================
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ReservationDAO reservationDAO = new ReservationDAO();

        String reservationNo = request.getParameter("reservationNo");
        String paymentAction = request.getParameter("paymentAction");
        String finalConfirm = request.getParameter("finalConfirm");
        String confirmOverPayment = request.getParameter("confirmOverPayment");
        if ("pay".equals(paymentAction)) {

    double paidAmount = Double.parseDouble(
            request.getParameter("paidAmount"));

    List<String[]> booking
            = reservationDAO.searchBookingDetails(reservationNo);

    if (booking == null || booking.isEmpty()) {
        response.getWriter().println("Booking not found!");
        return;
    }

    double totalAmount
            = Double.parseDouble(booking.get(0)[5]);

    HttpSession session = request.getSession(false);
    String role = (String) session.getAttribute("role");

    // 🔴 LESS THAN TOTAL
    if (paidAmount < totalAmount) {

        request.setAttribute("paymentError",
                "Payment amount cannot be less than Total Amount.");

        request.setAttribute("bookingDetails", booking);
        request.setAttribute("contentPage",
                "/jsp/admin/bookingDetails.jsp");

        forwardByRole(request, response, role);
        return;
    }

    // 🟡 OVERPAYMENT
    if (paidAmount > totalAmount) {

        double balance = paidAmount - totalAmount;

        request.setAttribute("overPayment", true);
        request.setAttribute("balanceAmount", balance);
        request.setAttribute("enteredAmount", paidAmount);
        request.setAttribute("bookingDetails", booking);
        request.setAttribute("contentPage",
                "/jsp/admin/bookingDetails.jsp");

        forwardByRole(request, response, role);
        return;
    }

    // 🟢 EXACT PAYMENT
    reservationDAO.updatePayment(reservationNo, paidAmount);

    response.sendRedirect(
            request.getContextPath()
            + "/manageReservation?action=details&reservationNo="
            + reservationNo);

    return;
}

//        if ("pay".equals(paymentAction)) {
//
//            double paidAmount = Double.parseDouble(
//                    request.getParameter("paidAmount"));
//
//            ReservationDAO dao = new ReservationDAO();
//
//            // Get booking details
//            List<String[]> booking
//                    = dao.searchBookingDetails(reservationNo);
//
//            if (booking == null || booking.isEmpty()) {
//                response.getWriter().println("Booking not found!");
//                return;
//            }
//
//            double totalAmount
//                    = Double.parseDouble(booking.get(0)[5]);
//
//            // 🔴 CASE 1: Paid < Total
//            if (paidAmount < totalAmount) {
//
//                request.setAttribute("paymentError",
//                        "Payment amount cannot be less than Total Amount.");
//
//                request.setAttribute("bookingDetails", booking);
//                request.setAttribute("contentPage",
//                        "bookingDetails.jsp");
//
//                HttpSession session = request.getSession(false);
//                String role = (String) session.getAttribute("role");
//
//                request.setAttribute("contentPage", "/jsp/admin/bookingDetails.jsp");
//
//                forwardByRole(request, response, role);
//                return;
//            }
//
//            // 🟡 CASE 3: Paid > Total
//            if (paidAmount > totalAmount) {
//
//                double balance = paidAmount - totalAmount;
//
//                request.setAttribute("overPayment", true);
//                request.setAttribute("balanceAmount", balance);
//                request.setAttribute("enteredAmount", paidAmount);
//                request.setAttribute("bookingDetails", booking);
//
//                request.setAttribute("contentPage",
//                        "bookingDetails.jsp");
//
//                HttpSession session = request.getSession(false);
//                String role = (String) session.getAttribute("role");
//
//                request.setAttribute("contentPage", "/jsp/admin/bookingDetails.jsp");
//
//                forwardByRole(request, response, role);
//                return;
//            }
//
//            // 🟢 CASE 2: Paid = Total
//            dao.updatePayment(reservationNo, paidAmount);
//
//            response.sendRedirect(
//                    request.getContextPath()
//                    + "/manageReservation?action=details&reservationNo=" + reservationNo);
//
//            return;
//        }

if ("yes".equals(confirmOverPayment)) {

    double finalAmount =
            Double.parseDouble(request.getParameter("finalAmount"));

    reservationDAO.updatePayment(reservationNo, finalAmount);

    response.sendRedirect(
            request.getContextPath()
            + "/manageReservation?action=details&reservationNo="
            + reservationNo);

    return;
}

        // ============================
        // FINAL CONFIRM
        // ============================
        if ("true".equals(finalConfirm) && reservationNo != null) {

            reservationDAO.confirmReservation(reservationNo);
            reservationDAO.createBookingFromReservation(reservationNo);

            response.sendRedirect(request.getContextPath() + "/manageReservation");
            return;
        }

        try {

            RoomDAO roomDAO = new RoomDAO();
            UserDAO userDAO = new UserDAO();

            int roomId = Integer.parseInt(request.getParameter("roomId"));
            int guestCount = Integer.parseInt(request.getParameter("guestCount"));

            LocalDate checkIn = LocalDate.parse(request.getParameter("checkIn"));
            LocalDate checkOut = LocalDate.parse(request.getParameter("checkOut"));

            if (!checkOut.isAfter(checkIn)) {
                response.getWriter().println("Invalid Date Range!");
                return;
            }

            int guestId = 0;
            String guestType = request.getParameter("guestType");

            // EXISTING GUEST
            if ("existing".equals(guestType)) {
                guestId = Integer.parseInt(request.getParameter("guestId"));
            }

            // NEW GUEST
            if ("new".equals(guestType)) {

                User newGuest = new User();
                newGuest.setUsername(request.getParameter("username"));
                newGuest.setFullName(request.getParameter("fullName"));
                newGuest.setEmail(request.getParameter("email"));
                newGuest.setContact(request.getParameter("contact"));
                newGuest.setAddress(request.getParameter("address"));
                newGuest.setPassword(
                        PasswordUtil.hashPassword("guest123"));
                newGuest.setRole("GUEST");

                userDAO.registerGuest(newGuest);

                User savedGuest
                        = userDAO.findGuestByEmailOrContact(newGuest.getEmail());

                guestId = savedGuest.getId();
            }

            Room selectedRoom = roomDAO.getRoomById(roomId);

            if (guestCount > selectedRoom.getCapacity()) {
                response.getWriter().println("Guest count exceeds room capacity!");
                return;
            }

            long days = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (days <= 0) {
                days = 1;
            }

            double totalAmount = days * selectedRoom.getPricePerNight();

            Reservation reservation
                    = new Reservation.Builder()
                            .setReservationNumber(
                                    ReservationNumberGenerator
                                            .generateReservationNumber())
                            .setRoomId(roomId)
                            .setGuestId(guestId)
                            .setCheckIn(checkIn)
                            .setCheckOut(checkOut)
                            .setTotalAmount(totalAmount)
                            .build();

            reservationDAO.addReservation(reservation);

            response.sendRedirect(
                    request.getContextPath() + "/manageReservation");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================
    // ROLE BASED LAYOUT FORWARD
    // ============================
    private void forwardByRole(HttpServletRequest request,
            HttpServletResponse response,
            String role)
            throws ServletException, IOException {

        String layout = "ADMIN".equals(role)
                ? "jsp/admin/layout.jsp"
                : "jsp/receptionist/layout.jsp";

        request.getRequestDispatcher(layout)
                .forward(request, response);
    }
}
