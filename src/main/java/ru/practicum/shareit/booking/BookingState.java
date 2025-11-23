package ru.practicum.shareit.booking;

public enum BookingState {
    ALL,       // все
    CURRENT,   // текущие
    PAST,      // завершённые
    FUTURE,    // будущие
    WAITING,   // ожидающие подтверждения
    REJECTED   // отклонённые
}
