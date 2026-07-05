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
                new Todo("Buy groceries", "Pick up vegetables, eggs, milk, and rice for the week."),
                new Todo("Do laundry", "Wash clothes, dry them, and fold everything before evening."),
                new Todo("Clean the kitchen", "Wipe the counters, wash dishes, and take out the trash."),
                new Todo("Pay electricity bill", "Check the bill amount and complete the payment before the due date."),
                new Todo("Water the plants", "Water indoor plants and move them near sunlight for a few hours."),
                new Todo("Plan tomorrow's meals", "Choose simple breakfast, lunch, and dinner options."),
                new Todo("Call family", "Check in with family and ask how their week is going."),
                new Todo("Exercise for 30 minutes", "Go for a walk, stretch, or do a short home workout."),
                new Todo("Read a book", "Read one chapter before going to sleep."),
                new Todo("Prepare work bag", "Pack keys, wallet, charger, notebook, and water bottle.")
        );

        todoRepository.saveAll(todos);
        logger.info("Seeded {} todo records.", todos.size());
    }
}
