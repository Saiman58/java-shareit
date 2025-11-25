package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;

    @NotNull(message = "Дата начала бронирования должна быть указана")
    @FutureOrPresent(message = "Дата начала должна быть в настоящем или будущем")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования должна быть указана")
    @Future(message = "Дата окончания должна быть в будущем")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    @NotNull(message = "ID вещи обязателен")
    private Long itemId;

    //статус брони
    private BookingStatus status;

    //пользователь
    private UserDto booker;

    //вещь
    private ItemDto item;

    @AssertTrue(message = "Дата окончания должна быть после даты начала")
    public boolean isEndAfterStart() {
        return end != null && start != null && end.isAfter(start);
    }

}
