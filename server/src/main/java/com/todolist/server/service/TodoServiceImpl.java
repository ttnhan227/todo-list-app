package com.todolist.server.service;

import com.todolist.server.dto.TodoRequest;
import com.todolist.server.dto.TodoResponse;
import com.todolist.server.exception.ResourceNotFoundException;
import com.todolist.server.model.Todo;
import com.todolist.server.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<TodoResponse> getTodos(String search, Boolean completed) {
        String normalizedSearch = normalizeSearch(search);

        return findTodos(normalizedSearch, completed)
                .stream()
                .map(this::toResponse)
                .toList();
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

    private List<Todo> findTodos(String search, Boolean completed) {
        if (search != null && completed != null) {
            return todoRepository
                    .findByCompletedAndTitleContainingIgnoreCaseOrCompletedAndDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
                            completed,
                            search,
                            completed,
                            search
                    );
        }

        if (search != null) {
            return todoRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
                    search,
                    search
            );
        }

        if (completed != null) {
            return todoRepository.findByCompletedOrderByCreatedAtDesc(completed);
        }

        return todoRepository.findAllByOrderByCreatedAtDesc();
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
