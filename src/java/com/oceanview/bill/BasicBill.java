package com.oceanview.bill;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;

public class BasicBill implements Bill {

    private String[] booking;

    public BasicBill(String[] booking) {
        this.booking = booking;
    }

    @Override
    public void generate(Document document) throws Exception {

        document.add(new Paragraph("================================="));
        document.add(new Paragraph("        Ocean View Resort        "));
        document.add(new Paragraph("================================="));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Reservation No : " + booking[0]));
        document.add(new Paragraph("Guest Name     : " + booking[1]));
        document.add(new Paragraph("Room           : " + booking[2]));
        document.add(new Paragraph("Check In       : " + booking[3]));
        document.add(new Paragraph("Check Out      : " + booking[4]));
        document.add(new Paragraph("Total Amount   : Rs. " + booking[5]));
        document.add(new Paragraph("Payment Status : " + booking[6]));
        document.add(new Paragraph("Paid Amount    : Rs. " + booking[7]));
        document.add(new Paragraph(" "));
    }
}