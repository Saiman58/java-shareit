package ru.practicum.gateway.request;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import ru.practicum.gateway.client.ClientRestFactory;
import ru.practicum.dto.request.ItemRequestDto;
import ru.practicum.gateway.client.BaseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Slf4j
@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl,
                             RestTemplateBuilder builder) {
        super(ClientRestFactory.build(serverUrl + API_PREFIX, builder));
    }

    // =============== Методы для работы с запросами ===============

    public ResponseEntity<Object> createRequest(Long userId, ItemRequestDto itemRequestDto) {
        log.debug("ItemRequestClient: создание запроса пользователем {}", userId);
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getMyRequests(Long userId) {
        log.debug("ItemRequestClient: получение запросов пользователя {}", userId);
        return get("", userId, null);
    }

    public ResponseEntity<Object> getAllOtherRequests(Long userId, Integer from, Integer size) {
        log.debug("ItemRequestClient: получение чужих запросов для пользователя {}, from={}, size={}",
                userId, from, size);

        String path = "/all?from={from}&size={size}";
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );

        return get(path, userId, parameters);
    }

    public ResponseEntity<Object> getRequestById(Long requestId, Long userId) {
        log.debug("ItemRequestClient: получение запроса {}, userId={}", requestId, userId);
        return get("/{requestId}", userId, Map.of("requestId", requestId));
    }
}