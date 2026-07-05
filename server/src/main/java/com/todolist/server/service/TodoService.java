package com.todolist.server.service;

import com.todolist.server.dto.TodoRequest;
import com.todolist.server.dto.TodoPageResponse;
import com.todolist.server.dto.TodoResponse;

public interface TodoService {

    TodoPageResponse getTodos(String search, Boolean completed, int page, int size);

    TodoResponse getTodoById(Long id);

    TodoResponse createTodo(TodoRequest request);

    TodoResponse updateTodo(Long id, TodoRequest request);

    TodoResponse updateCompleted(Long id, Boolean completed);

    void deleteTodo(Long id);
}
