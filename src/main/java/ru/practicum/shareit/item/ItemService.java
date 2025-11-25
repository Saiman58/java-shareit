package ru.practicum.shareit.item;

import java.util.Collection;

public interface ItemService {

    ItemDto createItem(Long ownerId, ItemDto itemDto);

    Collection<ItemDto> getAllItems();

    Collection<ItemDto> getItemsByOwner(Long ownerId);

    ItemDto getItemById(Long itemId);

    Collection<ItemDto> searchAvailableItems(String text);

    ItemDto updateItem(Long id, ItemDto itemDto, Long userId);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

}
