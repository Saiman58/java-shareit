package ru.practicum.dto.exception;

public class GatewayValidationException extends RuntimeException {
    public GatewayValidationException(String message) {
        super(message);
    }

    public GatewayValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}