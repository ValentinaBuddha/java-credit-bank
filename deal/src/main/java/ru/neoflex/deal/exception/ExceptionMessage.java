package ru.neoflex.deal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExceptionMessage {

    private String timestamp;
    private String status;
    private String error;
    private String message;
    private String path;
}