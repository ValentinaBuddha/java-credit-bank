package ru.neoflex.deal.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import ru.neoflex.deal.exception.ExceptionMessage;
import ru.neoflex.deal.exception.ScoringException;

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

        if (response.status() == 400) {
            return new ScoringException(message);
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
