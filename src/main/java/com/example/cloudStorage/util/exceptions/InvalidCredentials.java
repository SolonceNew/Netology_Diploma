package com.example.cloudStorage.util.exceptions;


public class InvalidCredentials extends RuntimeException {
    public InvalidCredentials(String message) {
        super(message);
    }
}
