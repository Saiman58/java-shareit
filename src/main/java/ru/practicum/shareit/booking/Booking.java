package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "Bookings")
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
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
    private LocalDateTime createdDate;


    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        if (status == null) {
            status = BookingStatus.WAITING; // статус по умолчанию
        }
    }

}
