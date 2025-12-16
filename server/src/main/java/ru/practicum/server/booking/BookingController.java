package ru.practicum.server.booking;

import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.booking.BookingDto;

import ru.practicum.dto.booking.BookingResponseDto;
import ru.practicum.dto.booking.BookingState;
import ru.practicum.server.exception.ValidationException;

import java.util.List;

@Validated
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    //Создание бронирования POST /bookings
    @PostMapping
    public BookingResponseDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestBody BookingDto bookingDto) {
        log.info("POST /bookings - Запрос на создание бронирования пользователем {}: {}", bookerId, bookingDto);
        BookingResponseDto result = bookingService.createBooking(bookerId, bookingDto);
        log.info("POST /bookings - Бронирование успешно создано: {}", result);
        return result;
    }

    // подтверждение или отклонение запроса PATCH /bookings/{bookingId}?approved={approved}
    @PatchMapping("/{bookingId}")
    public BookingResponseDto bookingStatusUpdate(@PathVariable Long bookingId,
                                                  @RequestParam Boolean approved,
                                                  @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.info("PATCH /bookings/{} - Запрос на изменение статуса бронирования: approved={}, userId={}",
                bookingId, approved, bookerId);
        BookingResponseDto result = bookingService.bookingStatusUpdate(bookingId, approved, bookerId);
        log.info("PATCH /bookings/{} - Статус бронирования успешно изменен: {}", bookingId, result);
        return result;
    }

    //получение данных о конкретном бронировании GET /bookings/{bookingId}
    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@PathVariable Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.info("GET /bookings/{} - Запрос на получение бронирования пользователем {}", bookingId, bookerId);
        BookingResponseDto result = bookingService.getBookingById(bookingId, bookerId);
        log.info("GET /bookings/{} - Бронирование получено: {}", bookingId, result);

        return result;
    }

    // получение списка всех бронирований текущего пользователя GET /bookings?state={state}
    @GetMapping
    public List<BookingResponseDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                    @RequestParam(defaultValue = "ALL") BookingState  state,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        log.info("GET /bookings - Запрос списка бронирований пользователя: userId={}, state={}, from={}, size={}",
                bookerId, state, from, size);
        validatePaginationParams(from, size);
        List<BookingResponseDto> result = bookingService.getUserBookings(bookerId, state, from, size);
        log.info("GET /bookings - Найдено {} бронирований для пользователя {}", result.size(), bookerId);
        return result;
    }

    private void validatePaginationParams(int from, int size) {
        if (from < 0) {
            log.warn("Валидация пагинации: параметр 'from' не может быть отрицательным: from={}", from);
            throw new ValidationException("Параметр 'from' не может быть отрицательным");
        }
        if (size <= 0) {
            log.warn("Валидация пагинации: параметр 'size' должен быть положительным: size={}", size);
            throw new ValidationException("Параметр 'size' должен быть положительным");
        }
        log.debug("Валидация пагинации пройдена: from={}, size={}", from, size);
    }

    //получение списка бронирований для всех вещей текущего пользователя - GET /bookings/owner?state={state}
    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /bookings/owner - Запрос списка бронирований владельца: ownerId={}, state={}, from={}, size={}",
                userId, state, from, size);

        return bookingService.getOwnerBookings(userId, state, from, size);
    }
}