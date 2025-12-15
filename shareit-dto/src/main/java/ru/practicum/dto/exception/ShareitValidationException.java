package ru.practicum.dto.exception;

public class ShareitValidationException extends RuntimeException {
    public ShareitValidationException(String message) {
        super(message);
    }
}
