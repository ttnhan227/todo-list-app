package com.todolist.server.controller;

import com.todolist.server.dto.TodoRequest;
import com.todolist.server.dto.TodoResponse;
import com.todolist.server.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean completed
    ) {
        return ResponseEntity.ok(todoService.getTodos(search, completed));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequest request
    ) {
        return ResponseEntity.ok(todoService.updateTodo(id, request));
    }

    @PatchMapping("/{id}/completed")
    public ResponseEntity<TodoResponse> updateCompleted(
            @PathVariable Long id,
            @RequestParam Boolean completed
    ) {
        return ResponseEntity.ok(todoService.updateCompleted(id, completed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
