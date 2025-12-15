package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.ItemRequestDto;
import ru.practicum.dto.request.ItemRequestResponseDto;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private static final Logger log = LoggerFactory.getLogger(ItemRequestServiceImpl.class);

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    //создать новый запрос
    @Override
    @Transactional
    public ItemRequestResponseDto createRequest(Long userId, ItemRequestDto itemRequestDto) {

        log.info("СЕРВИС - Попытка создания запроса: {}", itemRequestDto);

        User requester = findUserOrThrow(userId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
        log.info("Запрос создан и сохранен в БД с ID: {}", savedRequest.getId());

        return ItemRequestMapper.toResponseDto(savedRequest);
    }

    //получить свои запросы
    @Override
    public List<ItemRequestResponseDto> getUserRequests(Long userId) {
        log.info("Получение запросы пользователя {}", userId);

        findUserOrThrow(userId);

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId);

        log.info("Найдено запросов {} пользователя {}", requests.size(), userId);

        return ItemRequestMapper.toResponseDtoList(requests);
    }

    //получить запросы других пользователей
    @Override
    public List<ItemRequestResponseDto> getAllRequests(Long userId, Integer from, Integer size) {

        log.info("Получение чужих запросов для пользователя {}, from={}, size={}", userId, from, size);

        findUserOrThrow(userId);

        Pageable pageable = createPageable(from, size);

        List<ItemRequest> requests = itemRequestRepository
                .findByRequesterIdNot(userId, pageable)
                .getContent();

        log.info("Найдено {} чужих запросов", requests.size());

        return ItemRequestMapper.toResponseDtoList(requests);

    }

    //получить запрос по ID
    @Override
    public ItemRequestResponseDto getRequestById(Long userId, Long requestId) {

        log.info("Получение запроса {} для пользователя {}", requestId, userId);

        findUserOrThrow(userId);

        ItemRequest itemRequest = itemRequestRepository.findByIdWithItems(requestId)
                .orElseThrow(() -> {
                    log.error("Запрос с ID {} не найден", requestId);
                    return new NotFoundException("Запрос с ID " + requestId + " не найден");
                });

        log.info("Запрос {} найден, создатель: {}", requestId, userId);

        return ItemRequestMapper.toResponseDto(itemRequest);
    }

    @Override
    public void checkRequestExists(Long requestId) {
        if (itemRequestRepository.existsById(requestId)) {
            log.error("Запрос с ID {} не существует", requestId);
            throw new NotFoundException("Запрос с ID " + requestId + " не существует");
        }

    }

    //Найти пользователя
    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID {} не найден", userId);
                    return new NotFoundException("Пользователь с ID " + userId + " не найден");
                });
    }

    //Создание объекта пагинации
    private Pageable createPageable(Integer from, Integer size) {
        int pageNumber = from / size;

        return PageRequest.of(
                pageNumber,
                size,
                Sort.by(Sort.Direction.DESC, "created")
        );
    }

    // Найти запрос
    private ItemRequest findRequestOrThrow(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.warn("Запрос с ID {} не найден", requestId);
                    return new NotFoundException("Запрос с ID " + requestId + " не найден");
                });
    }
}
