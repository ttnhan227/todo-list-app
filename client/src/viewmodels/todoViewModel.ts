export type TodoViewModel = {
  id: number;
  title: string;
  description: string | null;
  completed: boolean;
  createdAt: string;
  updatedAt: string;
};

export type TodoRequestViewModel = {
  title: string;
  description: string;
};

export type ErrorResponseViewModel = {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  details: Record<string, string> | null;
};
