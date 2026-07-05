package com.todolist.server.service;

import com.todolist.server.dto.TodoRequest;
import com.todolist.server.dto.TodoResponse;

import java.util.List;

public interface TodoService {

    List<TodoResponse> getTodos(String search, Boolean completed);

    TodoResponse getTodoById(Long id);

    TodoResponse createTodo(TodoRequest request);

    TodoResponse updateTodo(Long id, TodoRequest request);

    TodoResponse updateCompleted(Long id, Boolean completed);

    void deleteTodo(Long id);
}
