package com.evaza.homestaybooking.model;

public record LoginRequest(
        String username,
        String password
) {
}
