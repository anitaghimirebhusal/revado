import { Component, inject, input, output, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subtask } from '../../../models/subtask.model';
import { SubtaskService } from '../../../core/services/subtask.service';
import { SubtaskItemComponent } from '../subtask-item/subtask-item.component';

@Component({
  selector: 'app-subtask-list',
  standalone: true,
  imports: [CommonModule, FormsModule, SubtaskItemComponent],
  templateUrl: './subtask-list.component.html',
  styleUrl: './subtask-list.component.css'
})
export class SubtaskListComponent implements OnInit {
  private subtaskService = inject(SubtaskService);

  todoId = input.required<number>();
  subtasks = input<Subtask[]>([]);
  subtasksChanged = output<void>();

  localSubtasks = signal<Subtask[]>([]);
  showAddForm = signal(false);
  newTitle = signal('');

  ngOnInit(): void {
    this.localSubtasks.set(this.subtasks() || []);
  }

  reload(): void {
    this.subtaskService.getAll(this.todoId()).subscribe({
      next: (res) => {
        this.localSubtasks.set(res.data || []);
        this.subtasksChanged.emit();
      }
    });
  }

  addSubtask(): void {
    const title = this.newTitle().trim();
    if (!title) return;

    this.subtaskService.create(this.todoId(), { title }).subscribe({
      next: () => {
        this.newTitle.set('');
        this.showAddForm.set(false);
        this.reload();
      }
    });
  }

  onSubtaskChanged(): void {
    this.reload();
  }

  onNewTitleChange(value: string): void {
    this.newTitle.set(value);
  }
}
