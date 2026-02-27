package com.oceanview.model;

public class Room {

    private int id;
    private String roomNumber;
    private String roomType;
    private int capacity;
    private double pricePerNight;
    private String description;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}