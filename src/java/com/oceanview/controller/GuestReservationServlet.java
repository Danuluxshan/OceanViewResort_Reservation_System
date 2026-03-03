package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.User;
import com.oceanview.util.ReservationNumberGenerator;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebServlet("/guestReservation")
public class GuestReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            !"GUEST".equals(session.getAttribute("role"))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User loggedUser = (User) session.getAttribute("user");

        ReservationDAO reservationDAO = new ReservationDAO();
        RoomDAO roomDAO = new RoomDAO();

        String action = request.getParameter("action");

        // ==========================
        // SEARCH AVAILABLE ROOMS
        // ==========================
        if ("searchRooms".equals(action)) {

            String checkIn = request.getParameter("checkIn");
            String checkOut = request.getParameter("checkOut");

            List<Room> rooms =
                    roomDAO.getAvailableRooms(checkIn, checkOut);

            request.setAttribute("availableRooms", rooms);
            request.setAttribute("checkIn", checkIn);
            request.setAttribute("checkOut", checkOut);

            request.setAttribute("contentPage",
                    "addReservation.jsp");

            request.getRequestDispatcher("jsp/guest/layout.jsp")
                    .forward(request, response);
            return;
        }

        // ==========================
        // VIEW OWN RESERVATIONS
        // ==========================
        if ("myReservations".equals(action)) {

            request.setAttribute("reservations",
                    reservationDAO.getReservationsByGuest(loggedUser.getId()));

            request.setAttribute("contentPage",
                    "myReservations.jsp");

            request.getRequestDispatcher("jsp/guest/layout.jsp")
                    .forward(request, response);
            return;
        }

        // ==========================
        // CANCEL RESERVATION
        // ==========================
        if ("cancel".equals(action)) {

            String reservationNo =
                    request.getParameter("reservationNo");

            reservationDAO.cancelGuestReservation(
                    reservationNo, loggedUser.getId());

            response.sendRedirect(
                    request.getContextPath()
                    + "/guestReservation?action=myReservations");

            return;
        }

        // ==========================
        // VIEW OWN BOOKINGS
        // ==========================
        if ("myBookings".equals(action)) {

            request.setAttribute("bookings",
                    reservationDAO.getBookingsByGuest(loggedUser.getId()));

            request.setAttribute("contentPage",
                    "myBookings.jsp");

            request.getRequestDispatcher("jsp/guest/layout.jsp")
                    .forward(request, response);
            return;
        }

        // DEFAULT
        request.setAttribute("contentPage",
                "addReservation.jsp");

        request.getRequestDispatcher("jsp/guest/layout.jsp")
                .forward(request, response);
    }

    // ==========================
    // ADD RESERVATION (POST)
    // ==========================
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            !"GUEST".equals(session.getAttribute("role"))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User loggedUser = (User) session.getAttribute("user");

        try {

            int roomId =
                    Integer.parseInt(request.getParameter("roomId"));

            LocalDate checkIn =
                    LocalDate.parse(request.getParameter("checkIn"));

            LocalDate checkOut =
                    LocalDate.parse(request.getParameter("checkOut"));

            RoomDAO roomDAO = new RoomDAO();
            ReservationDAO reservationDAO = new ReservationDAO();

            Room room = roomDAO.getRoomById(roomId);

            long days =
                    ChronoUnit.DAYS.between(checkIn, checkOut);

            if (days <= 0) days = 1;

            double totalAmount =
                    days * room.getPricePerNight();

            Reservation reservation =
                    new Reservation.Builder()
                    .setReservationNumber(
                            ReservationNumberGenerator
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
                    + "/guestReservation?action=myReservations");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}