package com.example.spring_app_url_shortening_service.exception;

public class UrlInactiveException extends RuntimeException {
    public UrlInactiveException(String message) {
        super(message);
    }
}
