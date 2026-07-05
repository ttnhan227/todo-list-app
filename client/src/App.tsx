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
import { TodoPagination } from "./components/TodoPagination";
import type { TodoRequestViewModel, TodoViewModel } from "./viewmodels/todoViewModel";
import "./App.css";

const PAGE_SIZE = 5;

function App() {
  const [todos, setTodos] = useState<TodoViewModel[]>([]);
  const [search, setSearch] = useState("");
  const [completedFilter, setCompletedFilter] = useState<boolean | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [isFirstPage, setIsFirstPage] = useState(true);
  const [isLastPage, setIsLastPage] = useState(true);
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
      const data = await getTodos(search, completedFilter, currentPage, PAGE_SIZE);

      if (data.content.length === 0 && data.totalElements > 0 && currentPage > 0) {
        setCurrentPage(Math.max(data.totalPages - 1, 0));
        return;
      }

      setTodos(data.content);
      setCurrentPage(data.page);
      setTotalElements(data.totalElements);
      setTotalPages(data.totalPages);
      setIsFirstPage(data.first);
      setIsLastPage(data.last);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to load tasks.");
    } finally {
      setIsLoading(false);
    }
  }, [search, completedFilter, currentPage]);

  useEffect(() => {
    void loadTodos();
  }, [loadTodos]);

  function handleSearchChange(value: string) {
    setSearch(value);
    setCurrentPage(0);
  }

  function handleCompletedFilterChange(value: boolean | null) {
    setCompletedFilter(value);
    setCurrentPage(0);
  }

  async function handleSubmit(todo: TodoRequestViewModel) {
    try {
      setIsSubmitting(true);
      setError("");

      if (editingTodo) {
        await updateTodo(editingTodo.id, todo);
        setEditingTodo(null);
      } else {
        await createTodo(todo);

        if (currentPage > 0) {
          setCurrentPage(0);
          return;
        }
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
            <p>Showing</p>
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
            onSearchChange={handleSearchChange}
            onCompletedFilterChange={handleCompletedFilterChange}
          />
          <TodoList
            todos={todos}
            isLoading={isLoading}
            onToggleCompleted={handleToggleCompleted}
            onEdit={setEditingTodo}
            onDelete={handleDelete}
          />
          <TodoPagination
            page={currentPage}
            totalPages={totalPages}
            totalElements={totalElements}
            isFirst={isFirstPage}
            isLast={isLastPage}
            onPageChange={setCurrentPage}
          />
        </div>
      </section>
    </main>
  );
}

export default App;
