package ru.neoflex.gateway.exception;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}