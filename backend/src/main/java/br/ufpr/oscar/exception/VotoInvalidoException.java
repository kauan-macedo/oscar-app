package br.ufpr.oscar.exception;

public class VotoInvalidoException extends RuntimeException {

    public VotoInvalidoException(String message) {
        super(message);
    }

    public VotoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
