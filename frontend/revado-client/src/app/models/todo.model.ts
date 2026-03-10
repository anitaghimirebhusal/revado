import { Subtask } from './subtask.model';

export interface Todo {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  subtasks: Subtask[];
  createdAt: string;
  updatedAt: string;
}

export interface TodoRequest {
  title: string;
  description?: string;
}
