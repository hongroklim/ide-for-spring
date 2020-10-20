package dev.rokong.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class RestException extends RuntimeException {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;

    RestException() {
        super();
        this.setTimestamp(LocalDateTime.now());
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

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    private void setStatus(HttpStatus status) {
        this.status = status;
    }
}