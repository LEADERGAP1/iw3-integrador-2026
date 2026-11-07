package ar.edu.iua.iw3.business.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String message) { super(message); }
    public NotFoundException(String message, Throwable cause) { super(message, cause); }
}