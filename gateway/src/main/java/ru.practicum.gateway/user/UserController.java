package ru.practicum.gateway.user;

import ru.practicum.dto.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Gateway: createUser request {}", userDto);
        return userClient.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        ResponseEntity<Object> error = validateId(userId);
        if (error != null) return error;

        log.info("Gateway: getUser request id={}", userId);
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Gateway: getAllUsers request");
        return userClient.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @RequestBody UserDto userDto) {
        ResponseEntity<Object> error = validateId(userId);
        if (error != null) return error;

        log.info("Gateway: updateUser request id={}, dto={}", userId, userDto);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        ResponseEntity<Object> error = validateId(userId);
        if (error != null) return error;

        log.info("Gateway: deleteUser request id={}", userId);
        return userClient.deleteUser(userId);
    }

    private ResponseEntity<Object> validateId(Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Id must be positive");
        }
        return null; // id валиден
    }
}