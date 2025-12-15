package ru.practicum.server.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.server.request.ItemRequest;
import ru.practicum.server.user.User;

@Getter
@Setter
@Entity
@ToString
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    //описание
    @Column(name = "description", length = 1000)
    private String description;

    //Доступен или нет
    @Column(name = "available", nullable = false)
    private Boolean available;

    //владелец вещи
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @ToString.Exclude // чтобы избежать циклических ссылок в toString()
    private User owner;

    //запрос
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    @ToString.Exclude
    private ItemRequest request;
}




