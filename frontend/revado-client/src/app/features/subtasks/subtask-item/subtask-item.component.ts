import { Component, inject, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subtask } from '../../../models/subtask.model';
import { SubtaskService } from '../../../core/services/subtask.service';

@Component({
  selector: 'app-subtask-item',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './subtask-item.component.html',
  styleUrl: './subtask-item.component.css'
})
export class SubtaskItemComponent {
  private subtaskService = inject(SubtaskService);

  subtask = input.required<Subtask>();
  todoId = input.required<number>();
  changed = output<void>();

  editing = signal(false);
  editTitle = signal('');

  onToggle(): void {
    this.subtaskService.toggleComplete(this.todoId(), this.subtask().id).subscribe({
      next: () => this.changed.emit()
    });
  }

  startEdit(): void {
    this.editTitle.set(this.subtask().title);
    this.editing.set(true);
  }

  saveEdit(): void {
    const title = this.editTitle().trim();
    if (!title) return;
    this.subtaskService.update(this.todoId(), this.subtask().id, { title }).subscribe({
      next: () => {
        this.editing.set(false);
        this.changed.emit();
      }
    });
  }

  cancelEdit(): void {
    this.editing.set(false);
  }

  onDelete(): void {
    if (confirm('Delete this subtask?')) {
      this.subtaskService.delete(this.todoId(), this.subtask().id).subscribe({
        next: () => this.changed.emit()
      });
    }
  }

  onEditTitleChange(value: string): void {
    this.editTitle.set(value);
  }
}
