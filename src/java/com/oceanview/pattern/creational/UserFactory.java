package com.oceanview.pattern.creational;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.oceanview.model.User;

public class UserFactory {

    public static User createUser(String role) {

        User user = new User();

        switch(role) {
            case "ADMIN":
                user.setRole("ADMIN");
                break;
            case "RECEPTIONIST":
                user.setRole("RECEPTIONIST");
                break;
            case "GUEST":
                user.setRole("GUEST");
                break;
        }

        return user;
    }
}