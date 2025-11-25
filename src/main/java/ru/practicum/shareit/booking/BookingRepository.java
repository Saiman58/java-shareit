package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Поиск всех бронирований пользователя с пагинацией
    List<Booking> findByBookerId(Long bookerId, Pageable pageable);

    // Поиск будущих бронирований пользователя (начало после указанной даты)
    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    // Поиск завершенных бронирований пользователя (окончание до указанной даты)
    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    // Поиск текущих бронирований пользователя (началось до указанной даты и закончится после)
    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime start,
                                                          LocalDateTime end, Pageable pageable);

    // Поиск бронирований пользователя по статусу
    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    // Поиск всех бронирований вещей владельца с пагинацией
    List<Booking> findByItemOwnerId(Long ownerId, Pageable pageable);

    // Поиск будущих бронирований вещей владельца
    List<Booking> findByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime start, Pageable pageable);

    // Поиск завершенных бронирований вещей владельца
    List<Booking> findByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime end, Pageable pageable);

    // Поиск текущих бронирований вещей владельца
    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start,
                                                             LocalDateTime end, Pageable pageable);

    // Поиск бронирований вещей владельца по статусу
    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Pageable pageable);

    // Проверка, брал ли пользователь вещь в аренду в прошлом (проверка комментов)
    boolean existsByBookerIdAndItemIdAndEndBefore(Long userId, Long itemId, LocalDateTime time);

}