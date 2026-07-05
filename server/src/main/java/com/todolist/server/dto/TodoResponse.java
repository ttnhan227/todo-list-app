package com.todolist.server.dto;

import java.time.LocalDateTime;

public record TodoResponse(
        Long id,
        String title,
        String description,
        Boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
