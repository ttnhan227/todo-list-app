package com.todolist.server.repository;

import com.todolist.server.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByOrderByCreatedAtDesc();

    List<Todo> findByCompletedOrderByCreatedAtDesc(Boolean completed);

    List<Todo> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
            String title,
            String description
    );

    List<Todo> findByCompletedAndTitleContainingIgnoreCaseOrCompletedAndDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
            Boolean completedForTitle,
            String title,
            Boolean completedForDescription,
            String description
    );
}
