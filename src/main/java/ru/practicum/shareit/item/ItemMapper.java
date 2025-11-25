package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemDto toItemDto(Item item);


    @Mapping(target = "owner", ignore = true)
    Item toItem(ItemDto itemDto);

    List<ItemDto> mapToItemDto(List<Item> items);
}
