package ru.practicum.server.request;

import ru.practicum.dto.request.ItemRequestDto;
import ru.practicum.dto.request.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {

    //создать запрос
    ItemRequestResponseDto createRequest(Long userId, ItemRequestDto itemRequestDto);

    //получить все запросы текущего пользователя
    List<ItemRequestResponseDto> getUserRequests(Long userId);

    //получить запросы других пользователей
    List<ItemRequestResponseDto> getAllRequests(Long userId, Integer from, Integer size);

    //получить запрос по ID
    ItemRequestResponseDto getRequestById(Long userId, Long requestId);

    //проверить существование запроса
    void checkRequestExists(Long requestId);

}
