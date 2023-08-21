package com.example.test_task.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ViolationErrorResponse {
    private final List<Violation> violations;
}
