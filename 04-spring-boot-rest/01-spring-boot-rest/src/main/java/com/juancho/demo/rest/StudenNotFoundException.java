package com.juancho.demo.rest;

public class StudenNotFoundException extends RuntimeException {

    public StudenNotFoundException() {
    }

    public StudenNotFoundException(String message) {
        super(message);
    }

    public StudenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
