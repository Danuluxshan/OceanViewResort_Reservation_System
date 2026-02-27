package com.oceanview.util;

import java.util.UUID;

public class ReservationNumberGenerator {

    public static String generateReservationNumber() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}