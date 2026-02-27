/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.oceanview.pattern.behavioral;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.oceanview.model.User;
import java.util.Iterator;
import java.util.List;

public class ReceptionistIterator implements Iterator<User> {

    private List<User> receptionistList;
    private int position = 0;

    public ReceptionistIterator(List<User> receptionistList) {
        this.receptionistList = receptionistList;
    }

    @Override
    public boolean hasNext() {
        return position < receptionistList.size();
    }

    @Override
    public User next() {
        return receptionistList.get(position++);
    }
}