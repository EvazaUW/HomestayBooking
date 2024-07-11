package com.evaza.homestaybooking.model;

public record ErrorResponse(
        String message,
        String error
) {
}
