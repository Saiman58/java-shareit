package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//ответ!!
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private List<ItemDtoForRequest> items;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDtoForRequest {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
        private Long ownerId;
    }
}
