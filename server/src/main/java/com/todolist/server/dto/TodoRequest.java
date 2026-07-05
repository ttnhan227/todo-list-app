package com.todolist.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 120, message = "Title must be at most 120 characters")
        String title,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description
) {
}
