package ru.neoflex.deal.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;
import ru.neoflex.deal.util.DateConstant;

import java.util.Date;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private String message;

    @JsonFormat(pattern = DateConstant.DATE_TIME_PATTERN)
    private Date timestamp;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
