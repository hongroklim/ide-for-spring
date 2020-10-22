package dev.rokong.exception;

@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {
    ApplicationException(){
        super();
    }

    ApplicationException(String message){
        super(message);
    }

    public ApplicationException(String message, Throwable cause){
        super(message, cause);
    }
}