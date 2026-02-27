package com.oceanview.dao;

import com.oceanview.model.User;
import com.oceanview.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    private Connection connection;

    public UserDAO() {
        connection = DatabaseConnectionManager.getInstance().getConnection();
    }

    // 🔹 Register Guest
    public boolean registerGuest(User user) {

        try {

            if (isUsernameExists(user.getUsername())) {
                return false;  // duplicate
            }

            String sql = "INSERT INTO users(username,password,role,full_name,email,contact,address) VALUES(?,?,?,?,?,?,?)";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, user.getUsername());

            // 🔐 Encrypt password before saving
            ps.setString(2, user.getPassword());

            ps.setString(3, "GUEST");
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getContact());
            ps.setString(7, user.getAddress());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 🔹 Login
    public User login(String username, String encryptedPassword) {

        try {
            String sql = "SELECT * FROM users WHERE username=? AND password=? AND status='ACTIVE'";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, encryptedPassword);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isUsernameExists(String username) {

        try {
            String sql = "SELECT id FROM users WHERE username=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            return rs.next(); // if record exists → true

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
