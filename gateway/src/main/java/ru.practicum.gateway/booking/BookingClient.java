package ru.practicum.gateway.booking;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import ru.practicum.gateway.client.ClientRestFactory;
import ru.practicum.dto.booking.BookingDto;
import ru.practicum.gateway.client.BaseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.Map;


@Slf4j
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
                         RestTemplateBuilder builder) {
        super(ClientRestFactory.build(serverUrl + API_PREFIX, builder));
    }

    // =============== Методы для работы с бронированиями ===============

    public ResponseEntity<Object> createBooking(Long bookerId, BookingDto bookingDto) {
        log.debug("BookingClient: создание бронирования пользователем {}", bookerId);
        return post("", bookerId, bookingDto);
    }

    public ResponseEntity<Object> updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        log.debug("BookingClient: обновление статуса бронирования {}, approved={}, userId={}",
                bookingId, approved, userId);

        // Создаем путь с параметром approved
        String path = "/{bookingId}?approved={approved}";
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );

        return patch(path, userId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(Long bookingId, Long userId) {
        log.debug("BookingClient: получение бронирования {}, userId={}", bookingId, userId);
        return get("/{bookingId}", userId, Map.of("bookingId", bookingId));
    }

    public ResponseEntity<Object> getUserBookings(Long userId, String state, Integer from, Integer size) {
        log.debug("BookingClient: получение бронирований пользователя {}, state={}, from={}, size={}",
                userId, state, from, size);

        String path = "?state={state}&from={from}&size={size}";
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );

        return get(path, userId, parameters);
    }

    public ResponseEntity<Object> getOwnerBookings(Long ownerId, String state, Integer from, Integer size) {
        log.debug("BookingClient: получение бронирований владельца {}, state={}, from={}, size={}",
                ownerId, state, from, size);

        String path = "/owner?state={state}&from={from}&size={size}";
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );

        return get(path, ownerId, parameters);
    }
}