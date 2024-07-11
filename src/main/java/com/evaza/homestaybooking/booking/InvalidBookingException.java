package com.evaza.homestaybooking.booking;

public class InvalidBookingException extends RuntimeException{
    public InvalidBookingException(String message) {
        super(message);
    }
}
