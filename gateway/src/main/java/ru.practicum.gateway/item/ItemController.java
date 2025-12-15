package ru.practicum.gateway.item;


import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import ru.practicum.dto.item.CommentDto;
import ru.practicum.dto.item.ItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    private static final String userHeader = "X-Sharer-User-Id";

    // создание вещи
    @PostMapping
    public ResponseEntity<Object> createItemDto(
            @Positive(message = "ID должен быть больше 0") @RequestHeader(userHeader) Long ownerId,
            @Valid @RequestBody ItemDto itemDto) {
        log.info("POST /items - Запрос на создание вещи пользователем {}: {}", ownerId, itemDto);
        log.info("POST /items - Вещь успешно создана: {}", itemDto.getId());
        return itemClient.createItem(ownerId, itemDto);
    }

    // просмотр вещи по id
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @Positive @PathVariable Long itemId,
            @Valid @RequestHeader(value = userHeader, required = false) Long ownerId) {
        log.info("GET /items/{} - Запрос на просмотр информации вещи с id: {}", itemId, itemId);
        return itemClient.getItemById(itemId, ownerId);
    }

    // просмотр вещей владельца
    @GetMapping()
    public ResponseEntity<Object> getMyItems(
            @Positive @RequestHeader(userHeader) Long ownerId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /items - Вещи владельца {}, from={}, size={}", ownerId, from, size);
        return itemClient.getMyItems(ownerId);
    }

    // поиск вещи по названию или описанию
    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(required = false) String text) {
        log.info("GET /items/search - Запрос на поиск вещей по тексту: '{}'", text);

        if (text == null || text.isBlank()) {
            log.info("GET /items/search - Пустой поисковый запрос");
            return ResponseEntity.ok(List.of());
        }

        return itemClient.searchAvailableItems(text);
    }

    // обновление вещи пользователем
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @Positive(message = "ID должен быть больше 0") @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @Positive(message = "ID должен быть больше 0") @RequestHeader(userHeader) Long userId) {
        log.info("PATCH /items/{} - Запрос на обновление вещи пользователем {}", itemId, userId);

        log.info("PATCH /items/{} - Вещь успешно обновлена: {}", itemId, itemDto.getId());
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    // вывод всех вещей
    @GetMapping("/allTeam")
    public ResponseEntity<Object> getAllItems() {
        log.info("GET /items/allTeam - Запрос на вывод всех вещей");
        return itemClient.getAllItems();
    }

    // добавление комментария
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @Positive(message = "ID должен быть больше 0") @PathVariable Long itemId,
            @Positive(message = "ID пользователя должен быть больше 0") @RequestHeader(userHeader) Long ownerId,
            @Valid @RequestBody CommentDto commentDto) {
        log.info("POST /items/{}/comment - Запрос на добавление комментария пользователем {}", itemId, ownerId);
        log.info("POST /items/{}/comment - Комментарий успешно добавлен", itemId);
        return itemClient.addComment(ownerId, itemId, commentDto);
    }

}