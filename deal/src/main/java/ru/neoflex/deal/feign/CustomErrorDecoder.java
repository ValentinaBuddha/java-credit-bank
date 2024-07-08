package ru.neoflex.deal.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.neoflex.deal.exception.BadRequestException;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 400) {
            return new BadRequestException("Bad Request Through Feign in Calculator Service");
        }
        return new Exception("Error in request went through feign client in Calculator Service");
    }
}
