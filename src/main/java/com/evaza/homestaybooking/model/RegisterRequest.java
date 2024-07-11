package com.evaza.homestaybooking.model;

public record RegisterRequest(
        String username,
        String password,
        UserRole role
) {
}
