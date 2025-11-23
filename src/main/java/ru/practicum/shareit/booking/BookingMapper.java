package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());

        if (booking.getItem() != null) {
            bookingDto.setItemId(booking.getItem().getId());
        }

        if (booking.getBooker() != null) {
            bookingDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        }

        if (booking.getItem() != null) {
            bookingDto.setItem(ItemMapper.toItemDto(booking.getItem()));
        }


        return bookingDto;
    }

    public static Booking tobooking(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(bookingDto.getStatus() != null ? bookingDto.getStatus() : BookingStatus.WAITING);

        return booking;
    }
}
