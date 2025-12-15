package ru.practicum.gateway.item;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import ru.practicum.gateway.client.ClientRestFactory;
import ru.practicum.dto.item.CommentDto;
import ru.practicum.dto.item.ItemDto;
import ru.practicum.gateway.client.BaseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Slf4j
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder builder) {
        super(ClientRestFactory.build(serverUrl + API_PREFIX, builder));
    }


    // =============== Методы для работы с вещами ===============

    public ResponseEntity<Object> createItem(Long ownerId, ItemDto itemDto) {
        log.debug("ItemClient: создание вещи владельцем {}", ownerId);
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long ownerId) {
        log.debug("ItemClient: получение вещи {}, userId={}", itemId, ownerId);
        return get("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> updateItem(Long itemId, ItemDto itemDto, Long userId) {
        log.debug("ItemClient: обновление вещи {}, userId={}", itemId, userId);
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getMyItems(Long ownerId) {
        log.debug("ItemClient: получение вещей владельца {}", ownerId);
        return get("", ownerId);
    }

    public ResponseEntity<Object> searchAvailableItems(String text) {
        log.debug("ItemClient: поиск вещей по тексту: '{}'", text);
        Map<String, Object> param = Map.of("text", text);
        return get("/search?text={text}", null, param);
    }

    public ResponseEntity<Object> getAllItems() {
        log.debug("ItemClient: получение всех вещей");
        return get(API_PREFIX, null, null);
    }

    public ResponseEntity<Object> addComment(Long ownerId, Long itemId, CommentDto commentDto) {
        log.info("ItemClient: добавление комментария к вещи {} пользователем {}", itemId, ownerId);
        return post("/" + itemId + "/comment", ownerId, commentDto);
    }

}