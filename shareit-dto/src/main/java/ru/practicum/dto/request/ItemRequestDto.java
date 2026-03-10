package ru.practicum.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//создать запрос
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    @NotBlank(message = "Описание запроса не может быть пустым")
    private String description;
}
