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
    public User login(String username, String password) {

        User user = null;

        try {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                user = new User();

                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));  // 🔥 IMPORTANT
                user.setEmail(rs.getString("email"));
                user.setContact(rs.getString("contact"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
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

    public User findGuestByEmailOrContact(String value) {

        try {
            String sql = "SELECT * FROM users WHERE role='GUEST' AND (email=? OR contact=?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, value);
            ps.setString(2, value);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setContact(rs.getString("contact"));
                user.setAddress(rs.getString("address"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getTotalUsers() {
        try {
            String sql = "SELECT COUNT(*) FROM users";
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

    public int getTotalGuests() {
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE role='GUEST'";
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

    public int getTotalReceptionists() {
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE role='RECEPTIONIST'";
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

    public boolean updateGuestProfile(User user) {

        try {

            String sql = """
            UPDATE users
            SET full_name=?,
                email=?,
                contact=?,
                address=?
            WHERE id=?
        """;

            PreparedStatement ps
                    = connection.prepareStatement(sql);

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getContact());
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {

        try {

            String sql
                    = "UPDATE users SET password=? WHERE id=?";

            PreparedStatement ps
                    = connection.prepareStatement(sql);

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deactivateAccount(int userId) {

        try {

            String sql
                    = "UPDATE users SET status='INACTIVE' WHERE id=?";

            PreparedStatement ps
                    = connection.prepareStatement(sql);

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getPasswordByUserId(int userId) {

        try {

            String sql = "SELECT password FROM users WHERE id=?";

            PreparedStatement ps
                    = connection.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
