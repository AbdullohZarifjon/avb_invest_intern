package com.example.userservice.exps;

public class RecordAlreadyException extends RuntimeException {

    public RecordAlreadyException(String message) {
        super(message);
    }
}
