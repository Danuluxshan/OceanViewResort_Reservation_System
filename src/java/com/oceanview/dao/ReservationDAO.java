package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private Connection connection;

    public ReservationDAO() {
        connection = DatabaseConnectionManager.getInstance().getConnection();
    }

    // 🔹 Add Reservation
    public boolean addReservation(Reservation reservation) {

        try {

            String sql = """
            INSERT INTO reservations(
                reservation_number,
                room_id,
                guest_id,
                check_in,
                check_out,
                total_amount,
                status
            )
            VALUES(?,?,?,?,?,?,?)
        """;

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, reservation.getReservationNumber());
            ps.setInt(2, reservation.getRoomId());
            ps.setInt(3, reservation.getGuestId());
            ps.setDate(4, Date.valueOf(reservation.getCheckIn()));
            ps.setDate(5, Date.valueOf(reservation.getCheckOut()));
            ps.setDouble(6, reservation.getTotalAmount());
            ps.setString(7, "PENDING");  // 🔥 IMPORTANT

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 🔹 View All Reservations
    public List<String[]> getAllReservations() {

        List<String[]> list = new ArrayList<>();

        try {

            String sql = """
            SELECT r.reservation_number,
                   u.full_name,
                   rm.room_number,
                   r.check_in,
                   r.check_out,
                   r.total_amount,
                   r.status
            FROM reservations r
            JOIN users u ON r.guest_id = u.id
            JOIN rooms rm ON r.room_id = rm.id
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[7]; // 🔥 NOW 7

                row[0] = rs.getString("reservation_number");
                row[1] = rs.getString("full_name");
                row[2] = rs.getString("room_number");
                row[3] = rs.getString("check_in");
                row[4] = rs.getString("check_out");
                row[5] = String.valueOf(rs.getDouble("total_amount"));
                row[6] = rs.getString("status");  // 🔥 IMPORTANT

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String[]> searchByReservationNumber(String reservationNo) {

        List<String[]> list = new ArrayList<>();

        try {
            String sql = """
            SELECT r.reservation_number,
                   u.full_name,
                   rm.room_number,
                   r.check_in,
                   r.check_out,
                   r.total_amount,
                   r.status
            FROM reservations r
            JOIN users u ON r.guest_id = u.id
            JOIN rooms rm ON r.room_id = rm.id
            WHERE r.reservation_number = ?
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, reservationNo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[7];

                row[0] = rs.getString("reservation_number");
                row[1] = rs.getString("full_name");
                row[2] = rs.getString("room_number");
                row[3] = rs.getString("check_in");
                row[4] = rs.getString("check_out");
                row[5] = String.valueOf(rs.getDouble("total_amount"));
                row[6] = rs.getString("status");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteReservation(String reservationNo) {

        try {
            String sql = "DELETE FROM reservations WHERE reservation_number=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, reservationNo);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createBookingFromReservation(String reservationNo) {

        try {

            String sql = """
            INSERT INTO bookings(
                reservation_number,
                guest_id,
                room_id,
                check_in,
                check_out,
                total_amount
            )
            SELECT reservation_number,
                   guest_id,
                   room_id,
                   check_in,
                   check_out,
                   total_amount
            FROM reservations
            WHERE reservation_number = ?
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, reservationNo);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String[]> searchBookingDetails(String reservationNo) {

        List<String[]> list = new ArrayList<>();

        try {

            String sql = """
            SELECT b.reservation_number,
                   u.full_name,
                   rm.room_number,
                   b.check_in,
                   b.check_out,
                   b.total_amount,
                   b.payment_status,
                   b.paid_amount
            FROM bookings b
            JOIN users u ON b.guest_id = u.id
            JOIN rooms rm ON b.room_id = rm.id
            WHERE b.reservation_number = ?
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, reservationNo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[8];

                row[0] = rs.getString("reservation_number");
                row[1] = rs.getString("full_name");
                row[2] = rs.getString("room_number");
                row[3] = rs.getString("check_in");
                row[4] = rs.getString("check_out");
                row[5] = rs.getString("total_amount");
                row[6] = rs.getString("payment_status");
                row[7] = rs.getString("paid_amount");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean confirmReservation(String reservationNo) {

        try {

            String sql = "UPDATE reservations SET status='CONFIRMED' WHERE LOWER(reservation_number)=LOWER(?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, reservationNo);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updatePayment(String reservationNo, double paidAmount) {

        try {

            // 1️⃣ Get total amount first
            String getSql = "SELECT total_amount FROM bookings WHERE reservation_number=?";
            PreparedStatement ps1 = connection.prepareStatement(getSql);
            ps1.setString(1, reservationNo);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                return false;
            }

            double totalAmount = rs.getDouble("total_amount");

            String paymentStatus;

            if (paidAmount >= totalAmount) {
                paymentStatus = "PAID";
            } else if (paidAmount > 0) {
                paymentStatus = "PARTIAL";
            } else {
                paymentStatus = "PENDING";
            }

            // 2️⃣ Update booking
            String updateSql = """
            UPDATE bookings
            SET paid_amount=?,
                payment_status=?
            WHERE reservation_number=?
        """;

            PreparedStatement ps2 = connection.prepareStatement(updateSql);
            ps2.setDouble(1, paidAmount);
            ps2.setString(2, paymentStatus);
            ps2.setString(3, reservationNo);

            return ps2.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String[]> getAllBookings() {

        List<String[]> list = new ArrayList<>();

        try {

            String sql = """
            SELECT b.reservation_number,
                   u.full_name,
                   rm.room_number,
                   b.total_amount,
                   b.paid_amount,
                   b.payment_status
            FROM bookings b
            JOIN users u ON b.guest_id = u.id
            JOIN rooms rm ON b.room_id = rm.id
            ORDER BY b.id DESC
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[6];

                row[0] = rs.getString("reservation_number");
                row[1] = rs.getString("full_name");
                row[2] = rs.getString("room_number");
                row[3] = rs.getString("total_amount");
                row[4] = rs.getString("paid_amount");
                row[5] = rs.getString("payment_status");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
