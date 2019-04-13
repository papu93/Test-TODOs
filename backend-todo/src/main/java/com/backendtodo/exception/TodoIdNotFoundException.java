package com.backendtodo.exception;

public class TodoIdNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TodoIdNotFoundException(String message) {
        super(message);
    }
}
