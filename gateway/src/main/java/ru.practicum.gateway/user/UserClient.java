package ru.practicum.gateway.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.gateway.client.ClientRestFactory;
import ru.practicum.dto.user.UserDto;
import ru.practicum.gateway.client.BaseClient;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    public UserClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder builder) {
        super(ClientRestFactory.build(serverUrl + API_PREFIX, builder));
    }

    public ResponseEntity<Object> createUser(UserDto dto) {
        return post("", dto);
    }

    public ResponseEntity<Object> getUser(Long userId) {
        return get("/" + userId, userId);
    }

    public ResponseEntity<Object> getAllUsers() {
        return get(""); // без userId
    }

    public ResponseEntity<Object> updateUser(Long userId, UserDto dto) {
        return patch("/" + userId, dto);
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        return delete("/" + userId);
    }
}