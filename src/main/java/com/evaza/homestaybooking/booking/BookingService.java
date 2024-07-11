package com.evaza.homestaybooking.booking;

import com.evaza.homestaybooking.model.BookingDto;
import com.evaza.homestaybooking.model.BookingEntity;
import com.evaza.homestaybooking.model.ListingEntity;
import com.evaza.homestaybooking.repository.BookingRepository;
import com.evaza.homestaybooking.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;
    public BookingService(
            BookingRepository bookingRepository,
            ListingRepository listingRepository
    ){
        this.bookingRepository = bookingRepository;
        this.listingRepository = listingRepository;
    }

    public List<BookingDto> findBookingsByGuestId (long guestId) {
        return bookingRepository.findAllByGuestId(guestId)
                .stream()               // can use parallelStream() for higher efficiency
                .map(BookingDto::new)
                .toList();              // can toSet(), and can to immutable collections
    }

    public List<BookingDto> findBookingsByListingId(long hostId, long listingId) {
        ListingEntity listing = listingRepository.getReferenceById(listingId);
        if (listing.getHostId() != hostId) {
            throw new ListingBookingsNotAllowedException(hostId, listingId);
        }
        return bookingRepository.findAllByListingId(listingId)
                .stream()
                .map(BookingDto::new)
                .toList();
    }

    public void createBooking(long guestId, long listingId, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter (checkOut)) {
            throw new InvalidBookingException("Check-in date must be before check-out date.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Check-in date must be in the future.");
        }
        List<BookingEntity> overlappedBookings = bookingRepository.findOverlappedBookings(listingId, checkIn, checkOut);
        if (!overlappedBookings.isEmpty()) {
            throw new InvalidBookingException("Booking dates conflict, please select different dates.");
        }
        bookingRepository.save(new BookingEntity(null, guestId, listingId, checkIn, checkOut));
    }

    public void deleteBooking (long guestId, long bookingId) {
        BookingEntity booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getGuestId() != guestId) {
            throw new DeleteBookingNotAllowedException(guestId, bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }

    public boolean existsActiveBookings(long listingId) {
        return bookingRepository.existsByListingIdAndCheckOutDateAfter(listingId, LocalDate.now());
    }
}
