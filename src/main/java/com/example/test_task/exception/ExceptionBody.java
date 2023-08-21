package com.example.test_task.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class ExceptionBody {
    @Schema(description = "Error message")
    private String message;

    public ExceptionBody(String message) {
        this.message = message;
    }
}