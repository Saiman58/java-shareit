package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Validated
@RestController
@RequestMapping("/requests")
public class ItemRequestController {

    private static final Logger log = LoggerFactory.getLogger(ItemRequestController.class);

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    //создать запрос
    @PostMapping
    public ItemRequestResponseDto createRequest(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST /requests - Создание запроса пользователем {}: {}", userId, itemRequestDto.getDescription());

        return itemRequestService.createRequest(userId, itemRequestDto);
    }

    //получить все запросы текущего пользователя
    @GetMapping
    public List<ItemRequestResponseDto> getMyRequests(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /requests - Получение запросов пользователя {}", userId);

        return itemRequestService.getUserRequests(userId);
    }

    //получение запросов других пользователей
    @GetMapping("/all")
    List<ItemRequestResponseDto> getAllOtherRequests(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("GET /requests/all - Чужие запросы для пользователя {}, from={}, size={}", userId, from, size);

        return itemRequestService.getAllRequests(userId, from, size);
    }

    //получение конкретного запроса по ID
    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestById(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Positive @PathVariable Long requestId) {

        log.info("GET /requests/{} - Просмотр запроса пользователем {}", requestId, userId);

        return itemRequestService.getRequestById(userId, requestId);
    }

}