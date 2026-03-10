package ru.practicum.server.request;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.request.ItemRequestDto;
import ru.practicum.dto.request.ItemRequestResponseDto;
import ru.practicum.dto.request.ItemRequestShortDto;
import ru.practicum.server.item.Item;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    // Метод для: GET /requests сокращенная версия ItemRequest в ItemRequestShortDto
    public static ItemRequestShortDto toShortDto(ItemRequest itemRequest) {
        if (itemRequest == null) {
            return null;
        }

        return new ItemRequestShortDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }


    // Преобразование ItemRequest в ItemRequestResponseDto
    public static ItemRequestResponseDto toResponseDto(ItemRequest itemRequest) {
        if (itemRequest == null) {
            return null;
        }

        List<ItemRequestResponseDto.ItemDtoForRequest> items = mapItemsToDto(itemRequest.getItems());

        return new ItemRequestResponseDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester().getId(),
                itemRequest.getCreated(),
                items
        );
    }

    // метод для преобразования списка вещей
    private static List<ItemRequestResponseDto.ItemDtoForRequest> mapItemsToDto(List<Item> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        return items.stream()
                .map(ItemRequestMapper::toItemDtoForRequest)
                .collect(Collectors.toList());
    }

    // метод для преобразования одной вещи
    private static ItemRequestResponseDto.ItemDtoForRequest toItemDtoForRequest(Item item) {
        if (item == null) {
            return null;
        }

        return new ItemRequestResponseDto.ItemDtoForRequest(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getOwner() != null ? item.getOwner().getId() : null
        );
    }


    //для: POST /requests - создание нового запроса (ItemRequestDto в ItemRequest)
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        if (itemRequestDto == null) {
            return null;
        }

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(java.time.LocalDateTime.now());

        return itemRequest;
    }

    // Метод для: GET /requests (альтернативная версия)
    // Преобразование списка ItemRequest в список ItemRequestShortDto
    public static List<ItemRequestShortDto> toShortDtoList(List<ItemRequest> itemRequests) {
        if (itemRequests == null) {
            return List.of();
        }

        return itemRequests.stream()
                .map(ItemRequestMapper::toShortDto)
                .collect(Collectors.toList());
    }

    // Преобразование списка ItemRequest в список ItemRequestResponseDto
    public static List<ItemRequestResponseDto> toResponseDtoList(List<ItemRequest> itemRequests) {
        if (itemRequests == null) {
            return List.of();
        }

        return itemRequests.stream()
                .map(ItemRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
