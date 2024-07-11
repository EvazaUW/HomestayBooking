package com.evaza.homestaybooking.location;

public class GeocodingException extends RuntimeException {
    public GeocodingException() {
        super("Failed to look up address");
    }
}
