package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //транзакция для чтения
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);


    //создать вещь
    @Override
    @Transactional
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        User owner = userService.findUserById(ownerId);

        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        log.info("Вещь: {} добавлена пользователем: {}", item.getName(), ownerId);
        return itemMapper.toItemDto(savedItem);
    }

    //вывод всех вещей
    @Override
    public Collection<ItemDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return itemMapper.mapToItemDto(items);
    }

    // информация о вещи по id
    @Override
    public ItemDto getItemById(Long itemId) {
        log.info("Поиск вещи с id = {}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Вещи с id = {} не найден", itemId);
                    return new NotFoundException("Вещь с id = " + itemId + " не найден");
                });

        ItemDto itemDto = itemMapper.toItemDto(item);

        List<CommentDto> comments = commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

        itemDto.setComments(comments);
        log.info("Найдена вещь {} с {} комментариями", itemId, comments.size());

        return itemDto;
    }

    // Только для владельца
    @Override
    public Collection<ItemDto> getItemsByOwner(Long ownerId) {
        userService.findUserById(ownerId);

        List<Item> items = itemRepository.findByOwnerId(ownerId);

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<Comment> allComments = commentRepository.findByItemIdIn(itemIds);

        Map<Long, List<CommentDto>> commentsMap = new HashMap<>();

        for (Comment comment : allComments) {
            Long itemId = comment.getItem().getId();
            CommentDto commentDto = commentMapper.toCommentDto(comment);

            commentsMap.computeIfAbsent(itemId, k -> new ArrayList<>())
                    .add(commentDto);
        }

        List<ItemDto> result = new ArrayList<>();

        for (Item item : items) {
            ItemDto itemDto = itemMapper.toItemDto(item);

            List<CommentDto> comments = commentsMap.get(item.getId());
            itemDto.setComments(comments != null ? comments : List.of());

            result.add(itemDto);
        }

        return result;
    }

    // Поиск по тексту
    @Override
    public Collection<ItemDto> searchAvailableItems(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<Item> items = itemRepository.searchAvailableItems(text.toLowerCase());
        return items.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    //Обновление информации о вещи
    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemDto updates, Long userId) {

        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Нельзя редактировать чужую вещь");
        }

        boolean wasUpdated = false;

        if (updates.getName() != null && !updates.getName().isBlank() &&
                !updates.getName().equals(existingItem.getName())) {
            existingItem.setName(updates.getName());
            log.info("Название вещи изменено на: {}", updates.getName());
            wasUpdated = true;
        }

        if (updates.getDescription() != null && !updates.getDescription().isBlank() &&
                !updates.getDescription().equals(existingItem.getDescription())) {
            existingItem.setDescription(updates.getDescription());
            log.info("Описание вещи изменено на: {}", updates.getDescription());
            wasUpdated = true;
        }

        if (updates.getAvailable() != null) {
            existingItem.setAvailable(updates.getAvailable());
            log.info("Статус доступности изменен на: {}", updates.getAvailable());
            wasUpdated = true;
        }

        if (!wasUpdated) {
            log.warn("Данные вещи не изменились");
            throw new ValidationException("Данные вещи не изменились");
        }
        return itemMapper.toItemDto(existingItem);
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        log.info("Добавление комментария пользователем: {} к вещи {}", userId, itemId);

        User author = userService.findUserById(userId);

        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        boolean hasBooked = bookingRepository.existsByBookerIdAndItemIdAndEndBefore(
                userId, itemId, LocalDateTime.now());

        log.info("Проверка бронирования: userId={}, itemId={}, hasBooked={}",
                userId, itemId, hasBooked);

        if (!hasBooked) {
            log.warn("Пользователь {} не брал вещь {} в аренду", userId, itemId);
            throw new ValidationException("Пользователь не брал эту вещь в аренду или аренда еще не завершена");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(existingItem);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        log.info("Комментарий успешно добавлен: {}", savedComment.getId());

        return commentMapper.toCommentDto(savedComment);
    }
}
