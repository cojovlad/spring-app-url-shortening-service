package com.example.spring_app_url_shortening_service.exception;

public class UrlExpiredException extends RuntimeException {
    public UrlExpiredException(String message) {
        super(message);
    }
}
