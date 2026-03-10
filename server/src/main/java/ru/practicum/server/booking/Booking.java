package ru.practicum.server.booking;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.booking.BookingStatus;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "bookings")
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    //вещь
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    private Item item;

    //пользователь
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    //статус брони
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime created;


    @PrePersist
    protected void onCreate() {
        if (created == null) {
            created = LocalDateTime.now();
        }
        if (status == null) {
            status = BookingStatus.WAITING; // статус по умолчанию
        }
    }
}
