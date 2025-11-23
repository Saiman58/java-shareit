package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.BookingDto;

import java.util.List;

@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    //описание
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Статус должен быть указан")
    private Boolean available;

    //владелец вещи
    private Long ownerId;

    //запрос
    private Long requestId;

    //комментарии
    private List<CommentDto> comments;

    // последнее бронирование
    private BookingDto lastBooking;

    // ближайшее будущее бронирование
    private BookingDto nextBooking;
}


