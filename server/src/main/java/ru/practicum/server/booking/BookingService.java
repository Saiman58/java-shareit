package ru.practicum.server.booking;

import ru.practicum.dto.booking.BookingDto;
import ru.practicum.dto.booking.BookingResponseDto;
import ru.practicum.dto.booking.BookingState;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(Long bookerId, BookingDto bookingDto);

    BookingResponseDto bookingStatusUpdate(Long bookingId, Boolean approved, Long bookerId);

    BookingResponseDto getBookingById(Long bookingId, Long bookerId);

    List<BookingResponseDto> getUserBookings(Long bookerId, BookingState state, int from, int size);

    List<BookingResponseDto> getOwnerBookings(Long ownerId, BookingState state, int from, int size);


}
