package ru.practicum.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import ru.practicum.dto.item.ItemDto;
import ru.practicum.dto.user.UserDto;


import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    private BookingStatus status;
    private UserDto booker;
    private ItemDto item;
}
