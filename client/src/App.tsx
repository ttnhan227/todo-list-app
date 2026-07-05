import { useCallback, useEffect, useMemo, useState } from "react";
import {
  createTodo,
  deleteTodo,
  getTodos,
  updateTodo,
  updateTodoCompleted,
} from "./api/todoApi";
import { TodoFilters } from "./components/TodoFilters";
import { TodoForm } from "./components/TodoForm";
import { TodoList } from "./components/TodoList";
import type { TodoRequestViewModel, TodoViewModel } from "./viewmodels/todoViewModel";
import "./App.css";

function App() {
  const [todos, setTodos] = useState<TodoViewModel[]>([]);
  const [search, setSearch] = useState("");
  const [completedFilter, setCompletedFilter] = useState<boolean | null>(null);
  const [editingTodo, setEditingTodo] = useState<TodoViewModel | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  const stats = useMemo(() => {
    const completed = todos.filter((todo) => todo.completed).length;

    return {
      total: todos.length,
      completed,
      active: todos.length - completed,
    };
  }, [todos]);

  const loadTodos = useCallback(async () => {
    try {
      setIsLoading(true);
      setError("");
      const data = await getTodos(search, completedFilter);
      setTodos(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to load tasks.");
    } finally {
      setIsLoading(false);
    }
  }, [search, completedFilter]);

  useEffect(() => {
    void loadTodos();
  }, [loadTodos]);

  async function handleSubmit(todo: TodoRequestViewModel) {
    try {
      setIsSubmitting(true);
      setError("");

      if (editingTodo) {
        await updateTodo(editingTodo.id, todo);
        setEditingTodo(null);
      } else {
        await createTodo(todo);
      }

      await loadTodos();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to save task.");
    } finally {
      setIsSubmitting(false);
    }
  }

  async function handleToggleCompleted(todo: TodoViewModel) {
    try {
      setError("");
      await updateTodoCompleted(todo.id, !todo.completed);
      await loadTodos();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to update task status.");
    }
  }

  async function handleDelete(id: number) {
    try {
      setError("");
      await deleteTodo(id);
      if (editingTodo?.id === id) {
        setEditingTodo(null);
      }
      await loadTodos();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to delete task.");
    }
  }

  return (
    <main className="app-shell">
      <section className="page-header">
        <div>
          <p className="eyebrow">Todo Manager</p>
          <h1>Plan, track, and finish your tasks.</h1>
        </div>

        <div className="stats" aria-label="Task summary">
          <div>
            <span>{stats.total}</span>
            <p>Total</p>
          </div>
          <div>
            <span>{stats.active}</span>
            <p>Active</p>
          </div>
          <div>
            <span>{stats.completed}</span>
            <p>Done</p>
          </div>
        </div>
      </section>

      {error && <p className="app-error">{error}</p>}

      <section className="workspace">
        <TodoForm
          editingTodo={editingTodo}
          isSubmitting={isSubmitting}
          onSubmit={handleSubmit}
          onCancelEdit={() => setEditingTodo(null)}
        />

        <div className="task-panel">
          <TodoFilters
            search={search}
            completedFilter={completedFilter}
            onSearchChange={setSearch}
            onCompletedFilterChange={setCompletedFilter}
          />
          <TodoList
            todos={todos}
            isLoading={isLoading}
            onToggleCompleted={handleToggleCompleted}
            onEdit={setEditingTodo}
            onDelete={handleDelete}
          />
        </div>
      </section>
    </main>
  );
}

export default App;
