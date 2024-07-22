package ru.neoflex.statement.exception;

public class EmailExistsException  extends RuntimeException {
    public EmailExistsException(String message) {
        super(message);
    }
}