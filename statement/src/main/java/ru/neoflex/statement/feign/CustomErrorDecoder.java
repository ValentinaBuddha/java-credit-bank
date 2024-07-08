package ru.neoflex.statement.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.neoflex.statement.exception.BadRequestException;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 400) {
            return new BadRequestException("Bad Request Through Feign in Deal Service");
        }
        return new Exception("Error in request went through feign client in Deal Service");
    }
}
