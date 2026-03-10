import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TodoService } from '../../../core/services/todo.service';
import { Todo, TodoRequest } from '../../../models/todo.model';
import { TodoItemComponent } from '../todo-item/todo-item.component';
import { TodoFormComponent } from '../todo-form/todo-form.component';

@Component({
  selector: 'app-todo-list',
  standalone: true,
  imports: [CommonModule, TodoItemComponent, TodoFormComponent],
  templateUrl: './todo-list.component.html',
  styleUrl: './todo-list.component.css'
})
export class TodoListComponent implements OnInit {
  private todoService = inject(TodoService);

  todos = signal<Todo[]>([]);
  loading = signal(true);
  showForm = signal(false);
  activeFilter = signal<'all' | 'active' | 'completed'>('all');

  ngOnInit(): void {
    this.loadTodos();
  }

  loadTodos(): void {
    this.loading.set(true);
    const filter = this.activeFilter();
    const completed = filter === 'completed' ? true : filter === 'active' ? false : undefined;

    this.todoService.getAll(completed).subscribe({
      next: (res) => {
        this.todos.set(res.data || []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  setFilter(filter: 'all' | 'active' | 'completed'): void {
    this.activeFilter.set(filter);
    this.loadTodos();
  }

  onCreateTodo(request: TodoRequest): void {
    this.todoService.create(request).subscribe({
      next: () => {
        this.showForm.set(false);
        this.loadTodos();
      }
    });
  }

  onUpdateTodo(event: { id: number; request: TodoRequest }): void {
    this.todoService.update(event.id, event.request).subscribe({
      next: () => this.loadTodos()
    });
  }

  onToggleComplete(id: number): void {
    this.todoService.toggleComplete(id).subscribe({
      next: () => this.loadTodos()
    });
  }

  onDeleteTodo(id: number): void {
    this.todoService.delete(id).subscribe({
      next: () => this.loadTodos()
    });
  }
}
