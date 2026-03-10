import { Component, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Todo, TodoRequest } from '../../../models/todo.model';
import { TodoFormComponent } from '../todo-form/todo-form.component';
import { SubtaskListComponent } from '../../subtasks/subtask-list/subtask-list.component';

@Component({
  selector: 'app-todo-item',
  standalone: true,
  imports: [CommonModule, TodoFormComponent, SubtaskListComponent],
  templateUrl: './todo-item.component.html',
  styleUrl: './todo-item.component.css'
})
export class TodoItemComponent {
  todo = input.required<Todo>();

  toggleComplete = output<number>();
  deleteTodo = output<number>();
  updateTodo = output<{ id: number; request: TodoRequest }>();
  todosChanged = output<void>();

  editing = signal(false);
  expanded = signal(false);

  onToggle(): void {
    this.toggleComplete.emit(this.todo().id);
  }

  onDelete(): void {
    if (confirm('Are you sure you want to delete this todo?')) {
      this.deleteTodo.emit(this.todo().id);
    }
  }

  onEdit(): void {
    this.editing.set(true);
  }

  onSaveEdit(request: TodoRequest): void {
    this.updateTodo.emit({ id: this.todo().id, request });
    this.editing.set(false);
  }

  onCancelEdit(): void {
    this.editing.set(false);
  }

  toggleExpand(): void {
    this.expanded.update(v => !v);
  }

  onSubtasksChanged(): void {
    this.todosChanged.emit();
  }
}
