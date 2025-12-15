package ru.practicum.gateway.request;

import ru.practicum.dto.request.ItemRequestDto;
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
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String userHeader = "X-Sharer-User-Id";

    // =============== Создание запроса ===============
    @PostMapping
    public ResponseEntity<Object> createRequest(
            @Positive(message = "ID пользователя должен быть больше 0")
            @RequestHeader(userHeader) Long userId,
            @Valid @RequestBody ItemRequestDto itemRequestDto) {

        log.info("POST /requests - Создание запроса пользователем {}: {}",
                userId, itemRequestDto.getDescription());

        return itemRequestClient.createRequest(userId, itemRequestDto);
    }

    // =============== Получение моих запросов ===============
    @GetMapping
    public ResponseEntity<Object> getMyRequests(
            @Positive(message = "ID пользователя должен быть больше 0")
            @RequestHeader(userHeader) Long userId) {

        log.info("GET /requests - Получение запросов пользователя {}", userId);

        return itemRequestClient.getMyRequests(userId);
    }

    // =============== Получение чужих запросов ===============
    @GetMapping("/all")
    public ResponseEntity<Object> getAllOtherRequests(
            @Positive(message = "ID пользователя должен быть больше 0")
            @RequestHeader(userHeader) Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("GET /requests/all - Чужие запросы для пользователя {}, from={}, size={}",
                userId, from, size);

        return itemRequestClient.getAllOtherRequests(userId, from, size);
    }

    // =============== Получение запроса по ID ===============
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @Positive(message = "ID пользователя должен быть больше 0")
            @RequestHeader(userHeader) Long userId,
            @Positive(message = "ID запроса должен быть больше 0")
            @PathVariable Long requestId) {

        log.info("GET /requests/{} - Просмотр запроса пользователем {}", requestId, userId);

        return itemRequestClient.getRequestById(requestId, userId);
    }
}