package dev.rokong.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@SuppressWarnings("serial")
public class RestException extends RuntimeException {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;

    RestException() {
        super();
        this.setTimestamp(LocalDateTime.now());
    }

    RestException(BusinessException e){
        this();
        this.setStatus(HttpStatus.BAD_REQUEST);
        this.setMessage(e.getMessage());
    }

    RestException(RuntimeException e){
        this();
        this.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        this.setMessage(e.getMessage());
    }

    RestException(HttpStatus status) {
        this();
        this.setStatus(status);
        this.setMessage("Unexpected error");
    }

    RestException(HttpStatus status, String message) {
        this();
        this.setStatus(status);
        this.setMessage(message);
    }

}