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

            // 🔹 Generate booking ID
            String bookingId
                    = com.oceanview.util.BookingIdGenerator
                            .generateBookingId(connection);

            String sql = """
            INSERT INTO bookings(
                booking_id,
                reservation_number,
                guest_id,
                room_id,
                check_in,
                check_out,
                total_amount,
                booking_status,
                payment_status,
                paid_amount
            )
            SELECT ?,
                   reservation_number,
                   guest_id,
                   room_id,
                   check_in,
                   check_out,
                   total_amount,
                   'CONFIRMED',
                   'PENDING',
                   NULL
            FROM reservations
            WHERE reservation_number = ?
        """;

            PreparedStatement ps
                    = connection.prepareStatement(sql);

            ps.setString(1, bookingId);
            ps.setString(2, reservationNo);

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

            // 1️⃣ Get total amount
            String getSql = "SELECT total_amount FROM bookings WHERE reservation_number=?";
            PreparedStatement ps1 = connection.prepareStatement(getSql);
            ps1.setString(1, reservationNo);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                return false;
            }

            double totalAmount = rs.getDouble("total_amount");

            String paymentStatus;
            String bookingStatus;

            // 2️⃣ Decide payment + booking status
            if (paidAmount >= totalAmount) {

                paymentStatus = "PAID";
                bookingStatus = "COMPLETED";   // 🔥 IMPORTANT

            } else if (paidAmount > 0) {

                paymentStatus = "PARTIAL";
                bookingStatus = "CONFIRMED";

            } else {

                paymentStatus = "PENDING";
                bookingStatus = "CONFIRMED";
            }

            // 3️⃣ Update bookings table
            String updateSql = """
            UPDATE bookings
            SET paid_amount=?,
                payment_status=?,
                booking_status=?
            WHERE reservation_number=?
        """;

            PreparedStatement ps2 = connection.prepareStatement(updateSql);

            ps2.setDouble(1, paidAmount);
            ps2.setString(2, paymentStatus);
            ps2.setString(3, bookingStatus);
            ps2.setString(4, reservationNo);

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
            SELECT b.booking_id,
                   b.reservation_number,
                   u.full_name,
                   rm.room_number,
                   b.total_amount,
                   b.paid_amount,
                   b.payment_status,
                   b.booking_status
            FROM bookings b
            JOIN users u ON b.guest_id = u.id
            JOIN rooms rm ON b.room_id = rm.id
            ORDER BY b.id DESC
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[8];

                row[0] = rs.getString("booking_id");
                row[1] = rs.getString("reservation_number");
                row[2] = rs.getString("full_name");
                row[3] = rs.getString("room_number");
                row[4] = rs.getString("total_amount");
                row[5] = rs.getString("paid_amount");
                row[6] = rs.getString("payment_status");
                row[7] = rs.getString("booking_status");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean cancelBooking(String bookingId) {

        try {

            String sql = """
            UPDATE bookings
            SET booking_status = 'CANCELLED'
            WHERE booking_id = ?
            AND booking_status != 'COMPLETED'
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, bookingId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String[]> searchBookingById(String bookingId) {

        List<String[]> list = new ArrayList<>();

        try {

            String sql = """
            SELECT b.booking_id,
                   b.reservation_number,
                   u.full_name,
                   rm.room_number,
                   b.total_amount,
                   b.paid_amount,
                   b.payment_status,
                   b.booking_status
            FROM bookings b
            JOIN users u ON b.guest_id = u.id
            JOIN rooms rm ON b.room_id = rm.id
            WHERE b.booking_id = ?
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, bookingId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[8];

                row[0] = rs.getString("booking_id");
                row[1] = rs.getString("reservation_number");
                row[2] = rs.getString("full_name");
                row[3] = rs.getString("room_number");
                row[4] = rs.getString("total_amount");
                row[5] = rs.getString("paid_amount");
                row[6] = rs.getString("payment_status");
                row[7] = rs.getString("booking_status");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String[]> filterBookingsByStatus(String status) {

        List<String[]> list = new ArrayList<>();

        try {

            String sql = """
            SELECT b.booking_id,
                   b.reservation_number,
                   u.full_name,
                   rm.room_number,
                   b.total_amount,
                   b.paid_amount,
                   b.payment_status,
                   b.booking_status
            FROM bookings b
            JOIN users u ON b.guest_id = u.id
            JOIN rooms rm ON b.room_id = rm.id
            WHERE b.booking_status = ?
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[8];

                row[0] = rs.getString("booking_id");
                row[1] = rs.getString("reservation_number");
                row[2] = rs.getString("full_name");
                row[3] = rs.getString("room_number");
                row[4] = rs.getString("total_amount");
                row[5] = rs.getString("paid_amount");
                row[6] = rs.getString("payment_status");
                row[7] = rs.getString("booking_status");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // 🔵 Total Reservations

    public int getTotalReservations() {

        try {
            String sql = "SELECT COUNT(*) FROM reservations";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

// 🔵 Total Bookings
    public int getTotalBookings() {

        try {
            String sql = "SELECT COUNT(*) FROM bookings";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

// 🔵 Completed Bookings
    public int getCompletedBookings() {

        try {
            String sql = "SELECT COUNT(*) FROM bookings WHERE booking_status='COMPLETED'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

// 🔵 Cancelled Bookings
    public int getCancelledBookings() {

        try {
            String sql = "SELECT COUNT(*) FROM bookings WHERE booking_status='CANCELLED'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

// 🔵 Total Revenue
    public double getTotalRevenue() {

        try {
            String sql = "SELECT SUM(paid_amount) FROM bookings WHERE payment_status='PAID'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

// 🔵 Pending Payments
    public int getPendingPayments() {

        try {
            String sql = "SELECT COUNT(*) FROM bookings WHERE payment_status='PENDING'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<String[]> getMonthlyRevenue() {

        List<String[]> list = new ArrayList<>();

        try {

            String sql = """
            SELECT DATE_FORMAT(check_in, '%Y-%m') AS month,
                   SUM(paid_amount) AS revenue
            FROM bookings
            WHERE payment_status='PAID'
            GROUP BY month
            ORDER BY month
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[2];
                row[0] = rs.getString("month");
                row[1] = rs.getString("revenue");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String[]> getRoomOccupancyReport() {

        List<String[]> list = new ArrayList<>();

        try {

            String sql = """
            SELECT rm.room_number,
                   COUNT(b.id) AS total_bookings
            FROM bookings b
            JOIN rooms rm ON b.room_id = rm.id
            WHERE b.booking_status='COMPLETED'
            GROUP BY rm.room_number
        """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[2];
                row[0] = rs.getString("room_number");
                row[1] = rs.getString("total_bookings");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getActiveReservations() {
        try {
            String sql = "SELECT COUNT(*) FROM reservations WHERE status='PENDING'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayReservationsCount() {
        try {
            String sql = "SELECT COUNT(*) FROM reservations WHERE check_in = CURDATE()";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayCheckinCount() {
        try {
            String sql = "SELECT COUNT(*) FROM bookings WHERE check_in = CURDATE() AND booking_status='CONFIRMED'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getPendingPaymentsCount() {
        try {
            String sql = "SELECT COUNT(*) FROM bookings WHERE payment_status='PENDING'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getGuestReservationCount(int guestId) {
        try {
            String sql = "SELECT COUNT(*) FROM reservations WHERE guest_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getGuestBookingCount(int guestId) {
        try {
            String sql = "SELECT COUNT(*) FROM bookings WHERE guest_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String[]> getBookingsByGuest(int guestId) {

        List<String[]> list = new ArrayList<>();

        try {

            String sql = """
            SELECT booking_id,
                   reservation_number,
                   total_amount,
                   paid_amount,
                   payment_status,
                   booking_status
            FROM bookings
            WHERE guest_id = ?
            ORDER BY id DESC
        """;

            PreparedStatement ps
                    = connection.prepareStatement(sql);

            ps.setInt(1, guestId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[6];

                row[0] = rs.getString("booking_id");
                row[1] = rs.getString("reservation_number");
                row[2] = rs.getString("total_amount");
                row[3] = rs.getString("paid_amount");
                row[4] = rs.getString("payment_status");
                row[5] = rs.getString("booking_status");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String[]> getReservationsByGuest(int guestId) {

        List<String[]> list = new ArrayList<>();

        try {
            String sql = """
            SELECT reservation_number,
                   check_in,
                   check_out,
                   total_amount,
                   status
            FROM reservations
            WHERE guest_id = ?
            ORDER BY id DESC
        """;

            PreparedStatement ps
                    = connection.prepareStatement(sql);

            ps.setInt(1, guestId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[5];

                row[0] = rs.getString("reservation_number");
                row[1] = rs.getString("check_in");
                row[2] = rs.getString("check_out");
                row[3] = rs.getString("total_amount");
                row[4] = rs.getString("status");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean cancelGuestReservation(String reservationNo, int guestId) {

        try {

            String sql = """
            UPDATE reservations
            SET status = 'CANCELLED'
            WHERE reservation_number = ?
            AND guest_id = ?
            AND status = 'PENDING'
        """;

            PreparedStatement ps
                    = connection.prepareStatement(sql);

            ps.setString(1, reservationNo);
            ps.setInt(2, guestId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
