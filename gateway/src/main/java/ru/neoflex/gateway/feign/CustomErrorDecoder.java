package ru.neoflex.gateway.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import ru.neoflex.gateway.exception.EntityNotFoundException;
import ru.neoflex.gateway.exception.ExceptionMessage;

import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {

        String message;

        try (InputStream responseBodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            ExceptionMessage exceptionMessage = mapper.readValue(responseBodyIs, ExceptionMessage.class);

            message = exceptionMessage.getMessage();
        }

        if (response.status() == 404) {
                return new EntityNotFoundException(message);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
