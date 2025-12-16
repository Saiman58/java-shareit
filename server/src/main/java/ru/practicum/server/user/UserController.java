package ru.practicum.server.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // вывод всех пользователей
    @GetMapping
    public Collection<UserDto> findUserAll() {
        log.info("GET/users - Запрос на вывод всех пользователей");
        return userService.getAllUsers();
    }

    // Добавить пользователя
    @PostMapping
    public UserDto createUserDto(@Valid @RequestBody UserDto userDto) {
        log.info("POST/users - Запрос на создание пользователя: {}", userDto);
        return userService.createUser(userDto);
    }

    // Обновить информацию о пользователе по id
    @PatchMapping("/{userId}")
    public UserDto updateUser(@Positive
                              @PathVariable Long userId,
                              @RequestBody UserDto userDtoUpdates) {
        log.info("PATCH/users - Запрос на обновление информации о пользователе: {}", userId);
        return userService.updateUser(userId, userDtoUpdates);
    }

    //Найти пользователя по id
    @GetMapping("/{userId}")
    public UserDto findUserById(@Positive @PathVariable Long userId) {
        log.info("GET/users/{} - Запрос на просмотр информации о пользователе с id: {}", userId, userId);
        return userService.findUserByIdToDto(userId);
    }

    //удаление пользователя по id
    @DeleteMapping("/{userId}")
    public void deleteUser(@Positive @PathVariable Long userId) {
        log.info("DELETE/users/{} - Запрос на удаление пользователе с id: {}", userId, userId);
        userService.deleteUser(userId);
    }
}