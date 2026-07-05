package com.todolist.server.service;

import com.todolist.server.dto.TodoPageResponse;
import com.todolist.server.dto.TodoRequest;
import com.todolist.server.dto.TodoResponse;
import com.todolist.server.exception.ResourceNotFoundException;
import com.todolist.server.model.Todo;
import com.todolist.server.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void getTodosReturnsPaginatedTodos() {
        Todo todo = new Todo("Buy groceries", "Pick up vegetables and milk");
        PageRequest pageRequest = PageRequest.of(0, 5);

        when(todoRepository.findAllByOrderByCreatedAtDesc(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(todo), pageRequest, 12));

        TodoPageResponse response = todoService.getTodos(null, null, 0, 5);

        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().title()).isEqualTo("Buy groceries");
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(5);
        assertThat(response.totalElements()).isEqualTo(12);
        assertThat(response.totalPages()).isEqualTo(3);
        assertThat(response.first()).isTrue();
        assertThat(response.last()).isFalse();
    }

    @Test
    void getTodosRejectsInvalidPage() {
        assertThatThrownBy(() -> todoService.getTodos(null, null, -1, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page must be zero or greater");
    }

    @Test
    void getTodosRejectsInvalidSize() {
        assertThatThrownBy(() -> todoService.getTodos(null, null, 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be between 1 and 50");
    }

    @Test
    void createTodoTrimsTitleAndDescription() {
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoResponse response = todoService.createTodo(
                new TodoRequest("  Clean kitchen  ", "  Wash dishes and wipe the counter  ")
        );

        ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);
        verify(todoRepository).save(todoCaptor.capture());

        assertThat(todoCaptor.getValue().getTitle()).isEqualTo("Clean kitchen");
        assertThat(todoCaptor.getValue().getDescription()).isEqualTo("Wash dishes and wipe the counter");
        assertThat(response.title()).isEqualTo("Clean kitchen");
        assertThat(response.description()).isEqualTo("Wash dishes and wipe the counter");
    }

    @Test
    void updateCompletedChangesStatus() {
        Todo todo = new Todo("Water plants", null);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoResponse response = todoService.updateCompleted(1L, true);

        assertThat(response.completed()).isTrue();
        verify(todoRepository).save(todo);
    }

    @Test
    void getTodoByIdThrowsWhenTodoDoesNotExist() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.getTodoById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Todo not found with id: 99");
    }
}
