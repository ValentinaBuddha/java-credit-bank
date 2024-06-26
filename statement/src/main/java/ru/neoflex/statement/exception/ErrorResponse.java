package ru.neoflex.statement.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static ru.neoflex.statement.util.DateConstant.DATE_TIME_PATTERN;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private String message;

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private Date timestamp;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
