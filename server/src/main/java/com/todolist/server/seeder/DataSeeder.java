package com.todolist.server.seeder;

import com.todolist.server.model.Todo;
import com.todolist.server.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final TodoRepository todoRepository;

    public DataSeeder(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void run(String... args) {
        if (todoRepository.count() > 0) {
            logger.info("Todo seed skipped because records already exist.");
            return;
        }

        List<Todo> todos = List.of(
                new Todo("Review project requirements", "Read the assignment carefully and list the required features before coding."),
                new Todo("Design todo API endpoints", "Define routes for creating, updating, deleting, filtering, and completing todos."),
                new Todo("Set up PostgreSQL database", "Create the local todolist database and confirm the server can connect successfully."),
                new Todo("Create request validation rules", "Reject empty titles and limit long text before data reaches the service layer."),
                new Todo("Build todo list screen", "Show tasks clearly with completion status, actions, and empty-state handling."),
                new Todo("Add search and status filters", "Allow users to find tasks by text and filter by completed or active status."),
                new Todo("Test invalid API requests", "Check missing titles, unknown IDs, and invalid query values return clear errors."),
                new Todo("Write README run steps", "Document database setup, backend commands, frontend commands, and environment notes."),
                new Todo("Polish responsive layout", "Make sure the todo interface works comfortably on desktop and mobile screens."),
                new Todo("Prepare final GitHub submission", "Review source structure, commit changes, and make sure setup instructions are complete.")
        );

        todoRepository.saveAll(todos);
        logger.info("Seeded {} todo records.", todos.size());
    }
}
