/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.oceanview.dao;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.oceanview.model.User;
import com.oceanview.util.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceptionistDAO {

    private Connection connection;

    public ReceptionistDAO() {
        connection = DatabaseConnectionManager.getInstance().getConnection();
    }

    public boolean addReceptionist(User user) {

        try {
            String sql = "INSERT INTO users(username,password,role,full_name,email,contact,address) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, "RECEPTIONIST");
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

    public List<User> getAllReceptionists() {

        List<User> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM users WHERE role='RECEPTIONIST'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setContact(rs.getString("contact"));
                user.setStatus(rs.getString("status"));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateReceptionist(User user) {

        try {
            String sql = "UPDATE users SET full_name=?, email=?, contact=?, address=?, password=? WHERE id=?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getContact());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getPassword());
            ps.setInt(6, user.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteReceptionist(int id) {

        try {
            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getReceptionistById(int id) {

        try {
            String sql = "SELECT * FROM users WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
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
}
