package com.oceanview.dao;

import com.oceanview.model.User;
import com.oceanview.util.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    private Connection connection;

    public GuestDAO() {
        connection = DatabaseConnectionManager.getInstance().getConnection();
    }

    public List<User> getAllGuests() {

        List<User> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM users WHERE role='GUEST'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setContact(rs.getString("contact"));
                user.setAddress(rs.getString("address"));
                user.setStatus(rs.getString("status"));

                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean toggleStatus(int id, String status) {

        try {
            String sql = "UPDATE users SET status=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}