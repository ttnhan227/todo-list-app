import type { TodoViewModel } from "../viewmodels/todoViewModel";
import { TodoItem } from "./TodoItem";

type TodoListProps = {
  todos: TodoViewModel[];
  isLoading: boolean;
  onToggleCompleted: (todo: TodoViewModel) => Promise<void>;
  onEdit: (todo: TodoViewModel) => void;
  onDelete: (id: number) => Promise<void>;
};

export function TodoList({ todos, isLoading, onToggleCompleted, onEdit, onDelete }: TodoListProps) {
  if (isLoading) {
    return <p className="state-message">Loading tasks...</p>;
  }

  if (todos.length === 0) {
    return <p className="state-message">No tasks match the current view.</p>;
  }

  return (
    <section className="todo-list" aria-label="Todo list">
      {todos.map((todo) => (
        <TodoItem
          key={todo.id}
          todo={todo}
          onToggleCompleted={onToggleCompleted}
          onEdit={onEdit}
          onDelete={onDelete}
        />
      ))}
    </section>
  );
}
