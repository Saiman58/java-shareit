package ru.practicum.server.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.item.ItemDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "request.id", target = "requestId")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemDto toItemDto(Item item);


    @Mapping(target = "owner", ignore = true)
    Item toItem(ItemDto itemDto);

    List<ItemDto> mapToItemDto(List<Item> items);
}
