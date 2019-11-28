package com.example.jobproject;

public class Customer {
    String email;
    String phone;
    String name;
    String checkIn;
    String checkOut;

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", checkIn='" + checkIn + '\'' +
                ", checkOut='" + checkOut + '\'' +
                '}';
    }

    public Customer(String email, String phone, String name, String checkIn, String checkOut) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
}
