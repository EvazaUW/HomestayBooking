package com.evaza.homestaybooking;

import com.evaza.homestaybooking.booking.DeleteBookingNotAllowedException;
import com.evaza.homestaybooking.booking.InvalidBookingException;
import com.evaza.homestaybooking.booking.ListingBookingsNotAllowedException;
import com.evaza.homestaybooking.listing.DeleteListingNotAllowedException;
import com.evaza.homestaybooking.listing.InvalidListingSearchException;
import com.evaza.homestaybooking.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(
                "Resource not found",
                "resource_not_found"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ListingBookingsNotAllowedException.class)
    public final ResponseEntity<ErrorResponse> handleException(ListingBookingsNotAllowedException e) {
        return new ResponseEntity<>(new ErrorResponse(
                "Cannot get listing bookings",
                "listing_bookings_not_allowed"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DeleteBookingNotAllowedException.class)
    public final ResponseEntity<ErrorResponse> handleException(DeleteBookingNotAllowedException e) {
        return new ResponseEntity<>(new ErrorResponse(
                "Cannot delete booking",
                "delete_booking_not_allowed"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DeleteListingNotAllowedException.class)
    public final ResponseEntity<ErrorResponse> handleException(DeleteListingNotAllowedException e) {
        return new ResponseEntity<>(new ErrorResponse(
                e.getMessage(),
                "delete_listing_not_allowed"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidListingSearchException.class)
    public final ResponseEntity<ErrorResponse> handleException(InvalidListingSearchException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                ex.getMessage(),
                "invalid_search_request"),
                HttpStatus.BAD_REQUEST);
    }
}
