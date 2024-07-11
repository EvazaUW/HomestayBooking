package com.evaza.homestaybooking.listing;

import com.evaza.homestaybooking.booking.BookingService;
import com.evaza.homestaybooking.location.GeocodingService;
import com.evaza.homestaybooking.model.GeoPoint;
import com.evaza.homestaybooking.model.ListingDto;
import com.evaza.homestaybooking.model.ListingEntity;
import com.evaza.homestaybooking.repository.ListingRepository;
import com.evaza.homestaybooking.storage.ImageStorageService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class ListingService {
    private final BookingService bookingService;
    private final GeocodingService geocodingService;
    private final ImageStorageService imageStorageService;
    private final ListingRepository listingRepository;

    public ListingService(
            BookingService bookingService,
            GeocodingService geocodingService,
            ImageStorageService imageStorageService,
            ListingRepository listingRepository
    ) {
        this.bookingService = bookingService;
        this.geocodingService = geocodingService;
        this.imageStorageService = imageStorageService;
        this.listingRepository = listingRepository;
    }

    public List<ListingDto> getListings(Long hostId) {
        return listingRepository.findAllByHostId(hostId)
                .stream()
                .map(ListingDto::new)
                .toList();
    }

    public void createListing(
            long hostId,
            String name,
            String address,
            String description,
            int guestNumber,
            List<MultipartFile> images
    ){
        List<String> uploadedUrls = images.parallelStream()
                .filter(image -> !image.isEmpty())
                .map(imageStorageService::upload)
                .toList();

        GeoPoint geoPoint = geocodingService.getGeoPoint(address);

        GeometryFactory geometryFactory = new GeometryFactory();

        listingRepository.save(new ListingEntity(
                null,
                hostId,
                name,
                address,
                description,
                guestNumber,
                uploadedUrls,
                geometryFactory.createPoint(new Coordinate(geoPoint.lon(), geoPoint.lat()))
        ));
    }

    public void deleteListing(long hostId, long listingId) {
        ListingEntity listing = listingRepository.getReferenceById(listingId);
        if (listing.getHostId() != hostId) {
            throw new DeleteListingNotAllowedException("Host " + hostId + " not allowed to delete listing " + listingId);
        }
        if (bookingService.existsActiveBookings(listingId)) {
            throw new DeleteListingNotAllowedException("Active bookings exist, not allowed to delete listing " + listingId);
        }
        listingRepository.deleteById(listingId);    // hard delete 删得无影无踪 delete totally
        // normally, we should use soft delete; in all columns, we should add one column to mark, without mark without delete
    }

    public List<ListingDto> search(
            double lat,
            double lon,
            int distance,
            LocalDate checkIn,
            LocalDate checkOut,
            int guestNum
    ) {
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            throw new InvalidListingSearchException("Invalid latitude or longitude.");
        }
        if (distance <= 0) {
            throw new InvalidListingSearchException("Distance must be positive.");
        }
        if (checkIn.isAfter(checkOut)) {
            throw new InvalidListingSearchException("Check-in date must be before check-out date.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidListingSearchException("Check-in date must be in the future.");
        }
        return listingRepository.searchListings(
                        lat,
                        lon,
                        distance,
                        checkIn,
                        checkOut,
                        guestNum
                )
                .stream()
                .map(ListingDto::new)
                .collect(Collectors.toList());          // .toList(); is OK.
    }
}
