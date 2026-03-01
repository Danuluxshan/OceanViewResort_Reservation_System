package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.bill.Bill;
import com.oceanview.bill.BasicBill;
import com.oceanview.bill.QRCodeDecorator;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet("/downloadBill")
public class BillServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String reservationNo =
                request.getParameter("reservationNo");

        if (reservationNo == null || reservationNo.trim().isEmpty()) {
            response.getWriter().println("Invalid Reservation Number!");
            return;
        }

        ReservationDAO dao = new ReservationDAO();

        List<String[]> booking =
                dao.searchBookingDetails(reservationNo);

        if (booking == null || booking.isEmpty()) {
            response.getWriter().println("Booking not found!");
            return;
        }

        String[] b = booking.get(0);

        // Only allow bill download if payment completed
        if (!"PAID".equalsIgnoreCase(b[6])) {
            response.getWriter().println("Payment not completed yet!");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=Bill_" + reservationNo + ".pdf");

        OutputStream out = response.getOutputStream();

        Document document = new Document();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            // 🔹 Step 1 – Create Basic Bill
            Bill basicBill = new BasicBill(b);

            // 🔹 Step 2 – Decorate with QR Code
            String qrContent =
                    "Ocean View Resort\n"
                    + "Reservation: " + b[0] + "\n"
                    + "Guest: " + b[1] + "\n"
                    + "Room: " + b[2] + "\n"
                    + "Total: Rs." + b[5] + "\n"
                    + "Paid: Rs." + b[7];

            Bill decoratedBill =
                    new QRCodeDecorator(basicBill, qrContent);

            // 🔹 Step 3 – Generate Final Bill
            decoratedBill.generate(document);

            document.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error generating bill.");
        }
    }
}