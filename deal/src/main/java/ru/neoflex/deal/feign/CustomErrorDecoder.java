package ru.neoflex.deal.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import ru.neoflex.deal.exception.ConflictException;
import ru.neoflex.deal.exception.ExceptionMessage;
import ru.neoflex.deal.exception.BadRequestException;
import ru.neoflex.deal.exception.NotFoundException;
import ru.neoflex.deal.exception.ServerException;

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
            return new BadRequestException(message);
        }
        if (response.status() == 404) {
            return new NotFoundException(message);
        }
        if (response.status() == 409) {
            return new ConflictException(message);
        }
        if (response.status() == 500) {
            return new ServerException(message);
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
