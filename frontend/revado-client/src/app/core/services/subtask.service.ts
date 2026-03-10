import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../models/api-response.model';
import { Subtask, SubtaskRequest } from '../../models/subtask.model';

@Injectable({ providedIn: 'root' })
export class SubtaskService {
  private httpClient = inject(HttpClient);
  private apiRoot = environment.apiUrl;

  private url(todoId: number): string {
    return `${this.apiRoot}/api/v1/todos/${todoId}/subtasks`;
  }

  getAll(todoId: number): Observable<ApiResponse<Subtask[]>> {
    return this.httpClient.get<ApiResponse<Subtask[]>>(this.url(todoId));
  }

  create(todoId: number, request: SubtaskRequest): Observable<ApiResponse<Subtask>> {
    return this.httpClient.post<ApiResponse<Subtask>>(this.url(todoId), request);
  }

  update(todoId: number, subtaskId: number, request: SubtaskRequest): Observable<ApiResponse<Subtask>> {
    return this.httpClient.put<ApiResponse<Subtask>>(`${this.url(todoId)}/${subtaskId}`, request);
  }

  toggleComplete(todoId: number, subtaskId: number): Observable<ApiResponse<Subtask>> {
    return this.httpClient.patch<ApiResponse<Subtask>>(`${this.url(todoId)}/${subtaskId}/toggle`, {});
  }

  delete(todoId: number, subtaskId: number): Observable<ApiResponse<void>> {
    return this.httpClient.delete<ApiResponse<void>>(`${this.url(todoId)}/${subtaskId}`);
  }
}
