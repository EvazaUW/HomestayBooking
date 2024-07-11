package com.evaza.homestaybooking.location;

import com.evaza.homestaybooking.model.GeoPoint;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

// Given an address, return the (lat, lon)
@Service
public class GeocodingService {
//    public GeoPoint getGeoPoint(String address) {
//        // TODO: call Google Geocoding API to get geo point
//        return new GeoPoint(0, 0);  // a tmp fake (lat, lon)
//    }
    private final GeoApiContext context;
    public GeocodingService(GeoApiContext context) {
        this.context = context;
    }
    public GeoPoint getGeoPoint(String address) {
        try {
            GeocodingResult result = GeocodingApi.geocode(context, address).await()[0];
            if (result.partialMatch) {
                throw new InvalidAddressException();
            }
            return new GeoPoint(result.geometry.location.lat, result.geometry.location.lng);
        } catch (IOException | ApiException | InterruptedException e) {
            throw new GeocodingException();
        }
    }
}
