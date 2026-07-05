import type { TodoViewModel } from "../viewmodels/todoViewModel";

type TodoItemProps = {
  todo: TodoViewModel;
  onToggleCompleted: (todo: TodoViewModel) => Promise<void>;
  onEdit: (todo: TodoViewModel) => void;
  onDelete: (id: number) => Promise<void>;
};

const dateFormatter = new Intl.DateTimeFormat("en", {
  dateStyle: "medium",
  timeStyle: "short",
});

export function TodoItem({ todo, onToggleCompleted, onEdit, onDelete }: TodoItemProps) {
  return (
    <article className={todo.completed ? "todo-item completed" : "todo-item"}>
      <label className="todo-check">
        <input
          type="checkbox"
          checked={todo.completed}
          onChange={() => void onToggleCompleted(todo)}
        />
        <span>{todo.completed ? "Completed" : "Active"}</span>
      </label>

      <div className="todo-content">
        <h3>{todo.title}</h3>
        {todo.description && <p>{todo.description}</p>}
        <time dateTime={todo.updatedAt}>Updated {dateFormatter.format(new Date(todo.updatedAt))}</time>
      </div>

      <div className="todo-actions">
        <button type="button" className="button button-secondary" onClick={() => onEdit(todo)}>
          Edit
        </button>
        <button type="button" className="button button-danger" onClick={() => void onDelete(todo.id)}>
          Delete
        </button>
      </div>
    </article>
  );
}
