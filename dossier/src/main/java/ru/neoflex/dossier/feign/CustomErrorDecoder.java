package ru.neoflex.dossier.feign;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        return new Exception("Error in request went through feign client in Deal Service");
    }
}
