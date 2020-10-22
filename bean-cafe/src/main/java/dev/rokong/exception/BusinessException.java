package dev.rokong.exception;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
    BusinessException(){
        super();
    }

    BusinessException(String message){
        super(message);
    }

    public BusinessException(String message, Throwable cause){
        super(message, cause);
    }
}