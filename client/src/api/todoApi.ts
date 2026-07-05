import { API_BASE_URL } from "./apiConfig";
import type {
  ErrorResponseViewModel,
  TodoPageViewModel,
  TodoRequestViewModel,
  TodoViewModel,
} from "../viewmodels/todoViewModel";

const TODO_API_URL = `${API_BASE_URL}/todos`;

async function request<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      ...options?.headers,
    },
    ...options,
  });

  if (!response.ok) {
    throw new Error(await getErrorMessage(response));
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

async function getErrorMessage(response: Response) {
  try {
    const error = (await response.json()) as ErrorResponseViewModel;
    const detailMessages = error.details ? Object.values(error.details) : [];

    if (detailMessages.length > 0) {
      return detailMessages.join(" ");
    }

    return error.message || "Something went wrong. Please try again.";
  } catch {
    return "Something went wrong. Please try again.";
  }
}

export function getTodos(search?: string, completed?: boolean | null, page = 0, size = 5) {
  const params = new URLSearchParams();

  if (search?.trim()) {
    params.set("search", search.trim());
  }

  if (completed !== null && completed !== undefined) {
    params.set("completed", String(completed));
  }

  params.set("page", String(page));
  params.set("size", String(size));

  const queryString = params.toString();
  const url = queryString ? `${TODO_API_URL}?${queryString}` : TODO_API_URL;

  return request<TodoPageViewModel>(url);
}

export function createTodo(todo: TodoRequestViewModel) {
  return request<TodoViewModel>(TODO_API_URL, {
    method: "POST",
    body: JSON.stringify(todo),
  });
}

export function updateTodo(id: number, todo: TodoRequestViewModel) {
  return request<TodoViewModel>(`${TODO_API_URL}/${id}`, {
    method: "PUT",
    body: JSON.stringify(todo),
  });
}

export function updateTodoCompleted(id: number, completed: boolean) {
  return request<TodoViewModel>(`${TODO_API_URL}/${id}/completed?completed=${completed}`, {
    method: "PATCH",
  });
}

export function deleteTodo(id: number) {
  return request<void>(`${TODO_API_URL}/${id}`, {
    method: "DELETE",
  });
}
