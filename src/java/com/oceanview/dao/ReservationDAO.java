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
            String sql = "INSERT INTO reservations(reservation_number, room_id, guest_id, check_in, check_out, total_amount) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, reservation.getReservationNumber());
            ps.setInt(2, reservation.getRoomId());
            ps.setInt(3, reservation.getGuestId());
            ps.setDate(4, Date.valueOf(reservation.getCheckIn()));
            ps.setDate(5, Date.valueOf(reservation.getCheckOut()));
            ps.setDouble(6, reservation.getTotalAmount());

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
                       r.total_amount
                FROM reservations r
                JOIN users u ON r.guest_id = u.id
                JOIN rooms rm ON r.room_id = rm.id
                """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] row = new String[6];

                row[0] = rs.getString("reservation_number");
                row[1] = rs.getString("full_name");
                row[2] = rs.getString("room_number");
                row[3] = rs.getString("check_in");
                row[4] = rs.getString("check_out");
                row[5] = String.valueOf(rs.getDouble("total_amount"));

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
}