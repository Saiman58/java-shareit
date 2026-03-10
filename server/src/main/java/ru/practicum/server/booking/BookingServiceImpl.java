package ru.practicum.server.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.booking.BookingDto;
import ru.practicum.dto.booking.BookingResponseDto;
import ru.practicum.dto.booking.BookingState;
import ru.practicum.dto.booking.BookingStatus;
import ru.practicum.server.exception.AccessDeniedException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    //создание брони
    @Override
    @Transactional
    public BookingResponseDto createBooking(Long bookerId, BookingDto bookingDto) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (item.getOwner().getId().equals(bookerId)) {
            throw new AccessDeniedException("Владелец не может бронировать свою вещь");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала должна быть раньше даты окончания");
        }

        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);


        Booking saveBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingResponseDto(saveBooking);
    }

    //подтверждение статуса
    @Override
    @Transactional
    public BookingResponseDto bookingStatusUpdate(Long bookingId, Boolean approved, Long bookerId) {

        Booking booking = findById(bookingId);

        if (!booking.getItem().getOwner().getId().equals(bookerId)) {
            throw new AccessDeniedException("Только владелец может подтверждать бронь");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Статус брони уже изменен");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        Booking updatedBooking = bookingRepository.save(booking);

        return bookingMapper.toBookingResponseDto(updatedBooking);
    }

    //получение данных о конкретном бронировании
    @Override
    public BookingResponseDto getBookingById(Long bookingId, Long bookerId) {

        Booking booking = findById(bookingId);

        boolean isBooker = booking.getBooker().getId().equals(bookerId);
        boolean isOwner = booking.getItem().getOwner().getId().equals(bookerId);

        if (!isBooker && !isOwner) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        return bookingMapper.toBookingResponseDto(booking);
    }

    //Получение списка всех бронирований текущего пользователя
    @Override
    public List<BookingResponseDto> getUserBookings(Long bookerId, BookingState state, int from, int size) {

        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(bookerId, now, now, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndBefore(bookerId, now, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartAfter(bookerId, now, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatus(bookerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, pageable);
                break;
            default: // "ALL"
                bookings = bookingRepository.findByBookerId(bookerId, pageable);

        }
        return bookings.stream()
                .map(bookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    //Получение списка бронирований для всех вещей текущего пользователя
    @Override
    public List<BookingResponseDto> getOwnerBookings(Long ownerId, BookingState state, int from, int size) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        // Пагинация
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        // Фильтрация по состоянию
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, now, now, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndBefore(ownerId, now, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartAfter(ownerId, now, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED, pageable);
                break;
            default: // "ALL"
                bookings = bookingRepository.findByItemOwnerId(ownerId, pageable);
        }

        return bookings.stream()
                .map(bookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }


    //вспомогательный класс для поиска брони
    public Booking findById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));
        return booking;
    }
}

