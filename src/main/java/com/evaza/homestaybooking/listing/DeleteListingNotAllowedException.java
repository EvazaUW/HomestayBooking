package com.evaza.homestaybooking.listing;

public class DeleteListingNotAllowedException extends RuntimeException {

    public DeleteListingNotAllowedException(String message) {
        super(message);
    }
}
