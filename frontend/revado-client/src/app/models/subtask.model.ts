export interface Subtask {
  id: number;
  title: string;
  completed: boolean;
  todoId: number;
  createdAt: string;
  updatedAt: string;
}

export interface SubtaskRequest {
  title: string;
}
