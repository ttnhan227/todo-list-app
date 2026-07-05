package com.todolist.server.repository;

import com.todolist.server.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Todo> findByCompletedOrderByCreatedAtDesc(Boolean completed, Pageable pageable);

    Page<Todo> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
            String title,
            String description,
            Pageable pageable
    );

    Page<Todo> findByCompletedAndTitleContainingIgnoreCaseOrCompletedAndDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
            Boolean completedForTitle,
            String title,
            Boolean completedForDescription,
            String description,
            Pageable pageable
    );
}
