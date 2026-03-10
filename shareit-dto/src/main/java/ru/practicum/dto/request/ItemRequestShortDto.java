package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//на всякий случай вдруг пригодиться (Сокращенная версия инфы о вещи)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestShortDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}
