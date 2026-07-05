import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import type { TodoRequestViewModel, TodoViewModel } from "../viewmodels/todoViewModel";

type TodoFormProps = {
  editingTodo: TodoViewModel | null;
  isSubmitting: boolean;
  onSubmit: (todo: TodoRequestViewModel) => Promise<void>;
  onCancelEdit: () => void;
};

export function TodoForm({ editingTodo, isSubmitting, onSubmit, onCancelEdit }: TodoFormProps) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    setTitle(editingTodo?.title ?? "");
    setDescription(editingTodo?.description ?? "");
    setError("");
  }, [editingTodo]);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const trimmedTitle = title.trim();
    const trimmedDescription = description.trim();

    if (!trimmedTitle) {
      setError("Title is required.");
      return;
    }

    if (trimmedTitle.length > 120) {
      setError("Title must be at most 120 characters.");
      return;
    }

    if (trimmedDescription.length > 500) {
      setError("Description must be at most 500 characters.");
      return;
    }

    setError("");
    await onSubmit({
      title: trimmedTitle,
      description: trimmedDescription,
    });

    if (!editingTodo) {
      setTitle("");
      setDescription("");
    }
  }

  return (
    <form className="todo-form" onSubmit={handleSubmit}>
      <div className="form-header">
        <div>
          <h2>{editingTodo ? "Edit Task" : "Add Task"}</h2>
          <p>{editingTodo ? "Update the selected task." : "Create a new task for your list."}</p>
        </div>
        {editingTodo && (
          <button type="button" className="button button-secondary" onClick={onCancelEdit}>
            Cancel
          </button>
        )}
      </div>

      <label className="field">
        <span>Title</span>
        <input
          value={title}
          maxLength={120}
          onChange={(event) => setTitle(event.target.value)}
          placeholder="Review API validation"
        />
      </label>

      <label className="field">
        <span>Description</span>
        <textarea
          value={description}
          maxLength={500}
          onChange={(event) => setDescription(event.target.value)}
          placeholder="Add useful context for this task"
          rows={4}
        />
      </label>

      {error && <p className="form-error">{error}</p>}

      <button type="submit" className="button button-primary" disabled={isSubmitting}>
        {isSubmitting ? "Saving..." : editingTodo ? "Save Changes" : "Add Task"}
      </button>
    </form>
  );
}
