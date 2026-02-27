package com.oceanview.model;

import java.time.LocalDate;

public class Reservation {

    private String reservationNumber;
    private int roomId;
    private int guestId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalAmount;

    private Reservation(Builder builder) {
        this.reservationNumber = builder.reservationNumber;
        this.roomId = builder.roomId;
        this.guestId = builder.guestId;
        this.checkIn = builder.checkIn;
        this.checkOut = builder.checkOut;
        this.totalAmount = builder.totalAmount;
    }

    public static class Builder {

        private String reservationNumber;
        private int roomId;
        private int guestId;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private double totalAmount;

        public Builder setReservationNumber(String reservationNumber) {
            this.reservationNumber = reservationNumber;
            return this;
        }

        public Builder setRoomId(int roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setGuestId(int guestId) {
            this.guestId = guestId;
            return this;
        }

        public Builder setCheckIn(LocalDate checkIn) {
            this.checkIn = checkIn;
            return this;
        }

        public Builder setCheckOut(LocalDate checkOut) {
            this.checkOut = checkOut;
            return this;
        }

        public Builder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }

    // getters
    public String getReservationNumber() { return reservationNumber; }
    public int getRoomId() { return roomId; }
    public int getGuestId() { return guestId; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getTotalAmount() { return totalAmount; }
}