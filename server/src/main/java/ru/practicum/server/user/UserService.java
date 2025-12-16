package ru.practicum.server.user;

import ru.practicum.dto.user.UserDto;

import java.util.Collection;

public interface UserService {

    //   получения всех пользователей
    Collection<UserDto> getAllUsers();

    //  создания нового пользователя
    UserDto createUser(UserDto userDto);

    // Обновить информацию о пользователе по id
    UserDto updateUser(Long userId, UserDto updates);

    //поиск пользователя по id для внутренних нужд проекта
    User findUserById(Long userId);

    //поиск пользователя по id для пользователя
    UserDto findUserByIdToDto(Long userId);

    //удаление пользователя по id
    void deleteUser(Long id);
}
