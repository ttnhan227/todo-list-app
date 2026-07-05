package com.todolist.server.dto;

import java.util.List;

public record TodoPageResponse(
        List<TodoResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
