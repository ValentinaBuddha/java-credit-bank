package ru.neoflex.deal.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.neoflex.deal.exception.ScoringException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new ScoringException("Scoring result : statement denied.");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
