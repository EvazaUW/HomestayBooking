package com.evaza.homestaybooking.location;

public class InvalidAddressException extends RuntimeException{
    public InvalidAddressException() {
        super("Invalid address");
    }
}
