package com.oceanview.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingIdGenerator {

    public static String generateBookingId(Connection connection) {

        try {
            String sql = "SELECT COUNT(*) FROM bookings";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return "QB" + (count + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "QB1";
    }
}