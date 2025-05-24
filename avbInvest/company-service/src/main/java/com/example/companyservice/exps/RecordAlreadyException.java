package com.example.companyservice.exps;

public class RecordAlreadyException extends RuntimeException {

    public RecordAlreadyException(String message) {
        super(message);
    }
}
