package com.oceanview.dao;

import com.oceanview.model.Room;
import com.oceanview.util.DatabaseConnectionManager;

import java.sql.*;
import java.util.*;

public class RoomDAO {

    private Connection connection;

    public RoomDAO() {
        connection = DatabaseConnectionManager.getInstance().getConnection();
    }

    public boolean addRoom(Room room) {
        try {
            String sql = "INSERT INTO rooms(room_number, room_type, capacity, price_per_night, description) VALUES(?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setInt(3, room.getCapacity());
            ps.setDouble(4, room.getPricePerNight());
            ps.setString(5, room.getDescription());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Room> getAllRooms() {

        List<Room> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM rooms";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setCapacity(rs.getInt("capacity"));
                room.setPricePerNight(rs.getDouble("price_per_night"));
                room.setDescription(rs.getString("description"));
                list.add(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteRoom(int id) {
        try {
            String sql = "DELETE FROM rooms WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Room getRoomById(int id) {

        try {
            String sql = "SELECT * FROM rooms WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setCapacity(rs.getInt("capacity"));
                room.setPricePerNight(rs.getDouble("price_per_night"));
                room.setDescription(rs.getString("description"));
                return room;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateRoom(Room room) {

        try {
            String sql = "UPDATE rooms SET room_number=?, room_type=?, capacity=?, price_per_night=?, description=? WHERE id=?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setInt(3, room.getCapacity());
            ps.setDouble(4, room.getPricePerNight());
            ps.setString(5, room.getDescription());
            ps.setInt(6, room.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public List<Room> getAvailableRooms(String checkIn, String checkOut) {

    List<Room> list = new ArrayList<>();

    try {
        String sql = """
        SELECT * FROM rooms r
        WHERE r.id NOT IN (
            SELECT room_id FROM reservations
            WHERE (? BETWEEN check_in AND check_out)
            OR (? BETWEEN check_in AND check_out)
        )
        """;

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, checkIn);
        ps.setString(2, checkOut);

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            Room room = new Room();
            room.setId(rs.getInt("id"));
            room.setRoomNumber(rs.getString("room_number"));
            room.setRoomType(rs.getString("room_type"));
            room.setPricePerNight(rs.getDouble("price_per_night"));
            list.add(room);
        }

    } catch(Exception e) { e.printStackTrace(); }

    return list;
}
}
