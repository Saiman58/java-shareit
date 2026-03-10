package ru.practicum.gateway.exception;

public class GatewayValidationException extends RuntimeException {
    public GatewayValidationException(String message) {
        super(message);
    }
}
