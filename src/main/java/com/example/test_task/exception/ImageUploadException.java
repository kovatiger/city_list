package com.example.test_task.exception;

public class ImageUploadException extends RuntimeException{
    public ImageUploadException(String message) {
        super(message);
    }
}