import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../models/api-response.model';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../models/auth.model';
import { User } from '../../models/user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private httpClient = inject(HttpClient);
  private appRouter = inject(Router);
  private authEndpoint = `${environment.apiUrl}/api/v1/auth`;

  private currentUser = signal<User | null>(this.loadUser());
  readonly user = this.currentUser.asReadonly();
  readonly isLoggedIn = computed(() => !!this.currentUser());

  login(request: LoginRequest): Observable<ApiResponse<AuthResponse>> {
    return this.httpClient.post<ApiResponse<AuthResponse>>(`${this.authEndpoint}/login`, request)
      .pipe(tap(res => this.handleAuth(res)));
  }

  register(request: RegisterRequest): Observable<ApiResponse<AuthResponse>> {
    return this.httpClient.post<ApiResponse<AuthResponse>>(`${this.authEndpoint}/register`, request)
      .pipe(tap(res => this.handleAuth(res)));
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUser.set(null);
    this.appRouter.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  private handleAuth(response: ApiResponse<AuthResponse>): void {
    if (response.success && response.data) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
      this.currentUser.set(response.data.user);
    }
  }

  private loadUser(): User | null {
    const userJson = localStorage.getItem('user');
    return userJson ? JSON.parse(userJson) : null;
  }
}
