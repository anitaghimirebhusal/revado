import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../models/api-response.model';
import { Todo, TodoRequest } from '../../models/todo.model';

@Injectable({ providedIn: 'root' })
export class TodoService {
  private httpClient = inject(HttpClient);
  private todoEndpoint = `${environment.apiUrl}/api/v1/todos`;

  getAll(completed?: boolean): Observable<ApiResponse<Todo[]>> {
    let params = new HttpParams();
    if (completed !== undefined) {
      params = params.set('completed', completed.toString());
    }
    return this.httpClient.get<ApiResponse<Todo[]>>(this.todoEndpoint, { params });
  }

  getById(id: number): Observable<ApiResponse<Todo>> {
    return this.httpClient.get<ApiResponse<Todo>>(`${this.todoEndpoint}/${id}`);
  }

  create(request: TodoRequest): Observable<ApiResponse<Todo>> {
    return this.httpClient.post<ApiResponse<Todo>>(this.todoEndpoint, request);
  }

  update(id: number, request: TodoRequest): Observable<ApiResponse<Todo>> {
    return this.httpClient.put<ApiResponse<Todo>>(`${this.todoEndpoint}/${id}`, request);
  }

  toggleComplete(id: number): Observable<ApiResponse<Todo>> {
    return this.httpClient.patch<ApiResponse<Todo>>(`${this.todoEndpoint}/${id}/toggle`, {});
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.httpClient.delete<ApiResponse<void>>(`${this.todoEndpoint}/${id}`);
  }
}
