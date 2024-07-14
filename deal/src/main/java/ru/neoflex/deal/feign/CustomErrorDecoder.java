package ru.neoflex.deal.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.neoflex.deal.exception.ScoringException;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 400) {
            return new ScoringException("Scoring result : statement denied.");
        }
        return new Exception("Error in request went through feign client in Calculator Service");
    }
}
