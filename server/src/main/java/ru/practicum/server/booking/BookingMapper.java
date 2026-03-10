package ru.practicum.server.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.booking.BookingDto;
import ru.practicum.dto.booking.BookingResponseDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // для ответов
    @Mapping(source = "booker.id", target = "booker.id")
    @Mapping(source = "booker.name", target = "booker.name")
    @Mapping(source = "item.id", target = "item.id")
    @Mapping(source = "item.name", target = "item.name")
    @Mapping(source = "item.description", target = "item.description")
    @Mapping(source = "created", target = "created")
    BookingResponseDto toBookingResponseDto(Booking booking);

    // при создании
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created", ignore = true)
    Booking toBooking(BookingDto bookingDto);
}
