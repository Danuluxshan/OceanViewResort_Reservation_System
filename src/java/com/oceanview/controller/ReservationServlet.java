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

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        RoomDAO roomDAO = new RoomDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        UserDAO userDAO = new UserDAO();

        String checkIn = request.getParameter("checkIn");
        String checkOut = request.getParameter("checkOut");
        String searchValue = request.getParameter("searchValue");
        String action = request.getParameter("action");
        String reservationNo = request.getParameter("reservationNo");

        if (reservationNo != null && !reservationNo.trim().isEmpty()) {

            request.setAttribute("reservations",
                    reservationDAO.searchByReservationNumber(reservationNo.trim()));

        }

        if ("cancel".equals(action) && reservationNo != null) {
            reservationDAO.deleteReservation(reservationNo);
            response.sendRedirect(request.getContextPath() + "/manageReservation");
            return;
        }

// Step 1: Show confirmation preview only
        if ("confirm".equals(action) && reservationNo != null) {

            request.setAttribute("confirmationDetails",
                    reservationDAO.searchByReservationNumber(reservationNo));

            request.setAttribute("contentPage",
                    "confirmationScreen.jsp");

            request.getRequestDispatcher("jsp/admin/layout.jsp")
                    .forward(request, response);
            return;
        }
        if ("cancel".equals(action) && reservationNo != null) {
            reservationDAO.deleteReservation(reservationNo);
            response.sendRedirect(request.getContextPath() + "/manageReservation");
            return;
        }
        if ("details".equals(action) && reservationNo != null) {

            request.setAttribute("bookingDetails",
                    reservationDAO.searchBookingDetails(reservationNo));

            request.setAttribute("contentPage",
                    "bookingDetails.jsp");

            request.getRequestDispatcher("jsp/admin/layout.jsp")
                    .forward(request, response);
            return;
        }

        // 🔹 Check available rooms
        if (checkIn != null && checkOut != null) {

            List<Room> availableRooms
                    = roomDAO.getAvailableRooms(checkIn, checkOut);

            request.setAttribute("availableRooms", availableRooms);
        }

        // 🔹 Existing guest search
        if (searchValue != null && !searchValue.trim().isEmpty()) {
            User foundGuest
                    = userDAO.findGuestByEmailOrContact(searchValue.trim());

            request.setAttribute("foundGuest", foundGuest);
        }
        if ("manageBookings".equals(action)) {

            ReservationDAO dao = new ReservationDAO();

            request.setAttribute("bookings",
                    dao.getAllBookings());

            request.setAttribute("contentPage",
                    "manageBookings.jsp");

            request.getRequestDispatcher("jsp/admin/layout.jsp")
                    .forward(request, response);

            return;
        }
        // 🔹 Load all reservations
        request.setAttribute("reservations",
                reservationDAO.getAllReservations());

        request.setAttribute("contentPage",
                "manageReservationContent.jsp");

        request.getRequestDispatcher("jsp/admin/layout.jsp")
                .forward(request, response);

        System.out.println("Search Value: " + searchValue);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // Step 2: Final confirmation
        String finalConfirm = request.getParameter("finalConfirm");
        String reservationNo = request.getParameter("reservationNo");
        String paymentAction = request.getParameter("paymentAction");

        if ("pay".equals(paymentAction)) {

            double paidAmount = Double.parseDouble(
                    request.getParameter("paidAmount"));

            ReservationDAO dao = new ReservationDAO();

            // Get booking details
            List<String[]> booking
                    = dao.searchBookingDetails(reservationNo);

            if (booking == null || booking.isEmpty()) {
                response.getWriter().println("Booking not found!");
                return;
            }

            double totalAmount
                    = Double.parseDouble(booking.get(0)[5]);

            // 🔴 CASE 1: Paid < Total
            if (paidAmount < totalAmount) {

                request.setAttribute("paymentError",
                        "Payment amount cannot be less than Total Amount.");

                request.setAttribute("bookingDetails", booking);
                request.setAttribute("contentPage",
                        "bookingDetails.jsp");

                request.getRequestDispatcher("jsp/admin/layout.jsp")
                        .forward(request, response);
                return;
            }

            // 🟡 CASE 3: Paid > Total
            if (paidAmount > totalAmount) {

                double balance = paidAmount - totalAmount;

                request.setAttribute("overPayment", true);
                request.setAttribute("balanceAmount", balance);
                request.setAttribute("enteredAmount", paidAmount);
                request.setAttribute("bookingDetails", booking);

                request.setAttribute("contentPage",
                        "bookingDetails.jsp");

                request.getRequestDispatcher("jsp/admin/layout.jsp")
                        .forward(request, response);
                return;
            }

            // 🟢 CASE 2: Paid = Total
            dao.updatePayment(reservationNo, paidAmount);

            response.sendRedirect(
                    request.getContextPath()
                    + "/manageReservation?action=details&reservationNo=" + reservationNo);

            return;
        }
        String confirmOverPayment = request.getParameter("confirmOverPayment");

        if ("yes".equals(confirmOverPayment)) {
            double finalAmount
                    = Double.parseDouble(request.getParameter("finalAmount"));

            ReservationDAO dao = new ReservationDAO();
            dao.updatePayment(reservationNo, finalAmount);

            response.sendRedirect(
                    request.getContextPath()
                    + "/manageReservation?action=details&reservationNo=" + reservationNo);

            return;
        }

        if ("true".equals(finalConfirm) && reservationNo != null) {

            ReservationDAO reservationDAO = new ReservationDAO();

            // 1️⃣ Update reservation status
            boolean updated
                    = reservationDAO.confirmReservation(reservationNo);

            // 2️⃣ Insert into bookings table
            boolean inserted
                    = reservationDAO.createBookingFromReservation(reservationNo);

            System.out.println("Updated: " + updated);
            System.out.println("Inserted: " + inserted);

            response.sendRedirect(
                    request.getContextPath() + "/manageReservation");

            return;
        }
        try {

            RoomDAO roomDAO = new RoomDAO();
            ReservationDAO reservationDAO = new ReservationDAO();
            UserDAO userDAO = new UserDAO();

            String roomIdStr = request.getParameter("roomId");
            String guestType = request.getParameter("guestType");
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");
            String guestCountStr = request.getParameter("guestCount");

            int roomId = Integer.parseInt(roomIdStr);
            int guestCount = Integer.parseInt(guestCountStr);

            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);

            // 🔒 Validate dates
            if (!checkOut.isAfter(checkIn)) {
                response.getWriter().println("Invalid Date Range!");
                return;
            }

            int guestId = 0;

            // =========================
            // EXISTING GUEST
            // =========================
            if ("existing".equals(guestType)) {

                String guestIdStr = request.getParameter("guestId");

                if (guestIdStr == null || guestIdStr.isEmpty()) {
                    response.getWriter().println("Guest not selected!");
                    return;
                }

                guestId = Integer.parseInt(guestIdStr);
            }

            // =========================
            // NEW GUEST
            // =========================
            if ("new".equals(guestType)) {

                String username = request.getParameter("username");
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String contact = request.getParameter("contact");
                String address = request.getParameter("address");

                User newGuest = new User();
                newGuest.setUsername(username);
                newGuest.setFullName(fullName);
                newGuest.setEmail(email);
                newGuest.setContact(contact);
                newGuest.setAddress(address);
                newGuest.setPassword(
                        PasswordUtil.hashPassword("guest123"));
                newGuest.setRole("GUEST");

                userDAO.registerGuest(newGuest);

                User savedGuest
                        = userDAO.findGuestByEmailOrContact(email);

                guestId = savedGuest.getId();
            }

            // =========================
            // ROOM VALIDATION
            // =========================
            Room selectedRoom = roomDAO.getRoomById(roomId);

            if (guestCount > selectedRoom.getCapacity()) {
                response.getWriter().println(
                        "Guest count exceeds room capacity!");
                return;
            }

            // =========================
            // CALCULATE TOTAL AMOUNT
            // =========================
            long days = ChronoUnit.DAYS
                    .between(checkIn, checkOut);

            if (days <= 0) {
                days = 1;
            }

            double totalAmount
                    = days * selectedRoom.getPricePerNight();

            // =========================
            // BUILDER PATTERN
            // =========================
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
                    request.getContextPath()
                    + "/manageReservation");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
