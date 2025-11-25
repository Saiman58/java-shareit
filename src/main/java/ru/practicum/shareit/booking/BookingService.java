package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(Long bookerId, BookingDto bookingDto);

    BookingDto bookingStatusUpdate(Long bookingId, Boolean approved, Long bookerId);

    BookingDto getBookingById(Long bookingId, Long bookerId);

    List<BookingDto> getUserBookings(Long bookerId, String state, int from, int size);

    List<BookingDto> getOwnerBookings(Long ownerId, String state, int from, int size);


}
