/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.oceanview.controller;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/manageRooms")
public class RoomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        RoomDAO dao = new RoomDAO();
        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Room editRoom = dao.getRoomById(id);
            request.setAttribute("editRoom", editRoom);
        }

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteRoom(id);
            response.sendRedirect(request.getContextPath() + "/manageRooms");
            return;
        }

        List<Room> list = dao.getAllRooms();
        request.setAttribute("rooms", list);
        request.setAttribute("contentPage", "manageRoomsContent.jsp");

        request.getRequestDispatcher("jsp/admin/layout.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        RoomDAO dao = new RoomDAO();

        String idStr = request.getParameter("id");

        Room room = new Room();
        room.setRoomNumber(request.getParameter("roomNumber"));
        room.setRoomType(request.getParameter("roomType"));
        room.setCapacity(Integer.parseInt(request.getParameter("capacity")));
        room.setPricePerNight(Double.parseDouble(request.getParameter("price")));
        room.setDescription(request.getParameter("description"));

        if (idStr == null || idStr.isEmpty()) {
            dao.addRoom(room);
        } else {
            room.setId(Integer.parseInt(idStr));
            dao.updateRoom(room);
        }

        response.sendRedirect(request.getContextPath() + "/manageRooms");
    }
}