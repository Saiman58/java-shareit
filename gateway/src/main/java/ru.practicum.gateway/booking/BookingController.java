package ru.practicum.gateway.booking;

import ru.practicum.dto.booking.BookingDto;
import ru.practicum.dto.exception.ShareitValidationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingClient bookingClient;
    private static final String userHeader = "X-Sharer-User-Id";

    // =============== Создание бронирования ===============
    @PostMapping
    public ResponseEntity<Object> createBooking(
            @Positive(message = "ID пользователя должен быть больше 0")
            @RequestHeader(userHeader) Long bookerId,
            @Valid @RequestBody BookingDto bookingDto) {

        log.info("POST /bookings - Запрос на создание бронирования пользователем {}: {}", bookerId, bookingDto);

        log.info("POST /bookings - Бронирование успешно создано");
        return bookingClient.createBooking(bookerId, bookingDto);
    }

    // =============== Обновление статуса бронирования ===============
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingStatusUpdate(
            @Positive(message = "ID бронирования должен быть больше 0")
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @Positive(message = "ID пользователя должен быть больше 0")
            @RequestHeader(userHeader) Long userId) {

        log.info("PATCH /bookings/{} - Запрос на изменение статуса бронирования: approved={}, userId={}",
                bookingId, approved, userId);

        if (approved == null) {
            throw new ShareitValidationException("Параметр 'approved' обязателен");
        }

        log.info("PATCH /bookings/{} - Статус бронирования успешно изменен", bookingId);
        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }

    // =============== Получение бронирования по ID ===============
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @Positive(message = "ID бронирования должен быть больше 0")
            @PathVariable Long bookingId,
            @Positive(message = "ID пользователя должен быть больше 0")
            @RequestHeader(userHeader) Long bookerId) {

        log.info("GET /bookings/{} - Запрос на получение бронирования пользователем {}", bookingId, bookerId);

        log.info("GET /bookings/{} - Бронирование получено", bookingId);
        return bookingClient.getBookingById(bookingId, bookerId);
    }

    // =============== Бронирования пользователя ===============
    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @Positive(message = "ID пользователя должен быть больше 0")
            @RequestHeader(userHeader) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("GET /bookings - Запрос списка бронирований пользователя: userId={}, state={}, from={}, size={}",
                userId, state, from, size);

        validatePaginationParams(from, size);

        // Без переменной result, сразу возвращаем
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    // =============== Бронирования владельца ===============
    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(
            @Positive(message = "ID владельца должен быть больше 0")
            @RequestHeader(userHeader) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("GET /bookings/owner - Запрос списка бронирований владельца: ownerId={}, state={}, from={}, size={}",
                userId, state, from, size);

        // Без переменной result
        return bookingClient.getOwnerBookings(userId, state, from, size);
    }

    // =============== Валидация пагинации ===============
    private void validatePaginationParams(Integer from, Integer size) {
        if (from < 0) {
            log.warn("Валидация пагинации: параметр 'from' не может быть отрицательным: from={}", from);
            throw new ShareitValidationException("Параметр 'from' не может быть отрицательным");
        }
        if (size <= 0) {
            log.warn("Валидация пагинации: параметр 'size' должен быть положительным: size={}", size);
            throw new ShareitValidationException("Параметр 'size' должен быть положительным");
        }
        log.debug("Валидация пагинации пройдена: from={}, size={}", from, size);
    }
}