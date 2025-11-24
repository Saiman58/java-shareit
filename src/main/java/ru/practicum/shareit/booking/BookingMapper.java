package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "item.id", target = "itemId")
    BookingDto toBookingDto(Booking booking);


    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Booking tobooking(BookingDto bookingDto);

}
