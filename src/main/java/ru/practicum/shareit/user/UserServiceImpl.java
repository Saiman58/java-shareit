package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    //вывод всех пользователей
    @Override
    public Collection<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.mapToUserDto(users);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        log.info("СЕРВИС - Попытка создания пользователя: {}", userDto);

        // Проверка на существование пользователя с таким email
        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.error("Пользователь с email {} уже существует", userDto.getEmail());
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        User user = UserMapper.toUser(userDto);
        User savedUser = userRepository.save(user);

        log.info("Пользователь создан: id={}, email={}", savedUser.getId(), savedUser.getEmail());

        return UserMapper.toUserDto(savedUser);
    }

    //обновление данных у пользователя
    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto updates) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        boolean wasUpdated = false;

        if (updates.getName() != null && !updates.getName().equals(existingUser.getName())) {
            existingUser.setName(updates.getName());
            log.info("Имя пользователя изменено на: {}", updates.getName());
            wasUpdated = true;
        }

        if (updates.getEmail() != null && !existingUser.getEmail().equals(updates.getEmail())) {

            if (userRepository.existsByEmailAndIdNot(updates.getEmail(), userId)) {
                log.error("Email: {} уже используется другим пользователем", updates.getEmail());
                throw new ConflictException("Email " + updates.getEmail() + " уже используется");
            }

            existingUser.setEmail(updates.getEmail());
            log.info("Email пользователя изменен на: {}", updates.getEmail());
            wasUpdated = true;
        }

        if (!wasUpdated) {
            log.warn("Данные пользователя: {} не изменились", userId);
            throw new ValidationException("Данные пользователя не изменились");
        }
        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toUserDto(updatedUser);
    }

    //поиск пользователя по id для внутренних нужд проекта
    @Override
    public User findUserById(Long userId) {
        log.info("Поиск пользователя с id = {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id = {} не найден", userId);
                    return new NotFoundException("Пользователь с id = " + userId + " не найден");

                });
    }

    //поиск пользователя по id для пользователя
    @Override
    public UserDto findUserByIdToDto(Long userId) {
        User user = findUserById(userId);
        return UserMapper.toUserDto(user);
    }

    //удаление пользователя по id
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        findUserById(userId);
        userRepository.deleteById(userId);
        log.info("Пользователь с ID {} удален", userId);
    }
}
