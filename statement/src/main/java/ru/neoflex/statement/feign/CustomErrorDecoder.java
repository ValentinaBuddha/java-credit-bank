package ru.neoflex.statement.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.neoflex.statement.exception.EmailExistsException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 409) {
            return new EmailExistsException("Client with this email already exists. Use other email.");
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
