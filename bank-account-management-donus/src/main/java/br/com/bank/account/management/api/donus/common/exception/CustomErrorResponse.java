package br.com.bank.account.management.api.donus.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomErrorResponse {

    String error;
    String message;
    int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime timestamp;

    public CustomErrorResponse(String error, String message) {
        super();
        this.error = error;
        this.message = message;
    }
}
