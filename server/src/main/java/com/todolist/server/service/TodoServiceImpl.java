package com.todolist.server.service;

import com.todolist.server.dto.TodoPageResponse;
import com.todolist.server.dto.TodoRequest;
import com.todolist.server.dto.TodoResponse;
import com.todolist.server.exception.ResourceNotFoundException;
import com.todolist.server.model.Todo;
import com.todolist.server.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public TodoPageResponse getTodos(String search, Boolean completed, int page, int size) {
        String normalizedSearch = normalizeSearch(search);
        PageRequest pageRequest = PageRequest.of(validatePage(page), validateSize(size));
        Page<Todo> todoPage = findTodos(normalizedSearch, completed, pageRequest);

        return new TodoPageResponse(
                todoPage.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),
                todoPage.getNumber(),
                todoPage.getSize(),
                todoPage.getTotalElements(),
                todoPage.getTotalPages(),
                todoPage.isFirst(),
                todoPage.isLast()
        );
    }

    @Override
    public TodoResponse getTodoById(Long id) {
        return toResponse(findTodoById(id));
    }

    @Override
    public TodoResponse createTodo(TodoRequest request) {
        Todo todo = new Todo(request.title().trim(), normalizeDescription(request.description()));
        return toResponse(todoRepository.save(todo));
    }

    @Override
    public TodoResponse updateTodo(Long id, TodoRequest request) {
        Todo todo = findTodoById(id);
        todo.setTitle(request.title().trim());
        todo.setDescription(normalizeDescription(request.description()));

        return toResponse(todoRepository.save(todo));
    }

    @Override
    public TodoResponse updateCompleted(Long id, Boolean completed) {
        if (completed == null) {
            throw new IllegalArgumentException("Completed status is required");
        }

        Todo todo = findTodoById(id);
        todo.setCompleted(completed);

        return toResponse(todoRepository.save(todo));
    }

    @Override
    public void deleteTodo(Long id) {
        Todo todo = findTodoById(id);
        todoRepository.delete(todo);
    }

    private Todo findTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
    }

    private Page<Todo> findTodos(String search, Boolean completed, PageRequest pageRequest) {
        if (search != null && completed != null) {
            return todoRepository
                    .findByCompletedAndTitleContainingIgnoreCaseOrCompletedAndDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
                            completed,
                            search,
                            completed,
                            search,
                            pageRequest
                    );
        }

        if (search != null) {
            return todoRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
                    search,
                    search,
                    pageRequest
            );
        }

        if (completed != null) {
            return todoRepository.findByCompletedOrderByCreatedAtDesc(completed, pageRequest);
        }

        return todoRepository.findAllByOrderByCreatedAtDesc(pageRequest);
    }

    private int validatePage(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be zero or greater");
        }

        return page;
    }

    private int validateSize(int size) {
        if (size < 1 || size > 50) {
            throw new IllegalArgumentException("Page size must be between 1 and 50");
        }

        return size;
    }

    private TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getCompleted(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }

    private String normalizeSearch(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }

        return search.trim();
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }

        return description.trim();
    }
}
