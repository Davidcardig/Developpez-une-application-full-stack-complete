import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/auth.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private userUrl = 'http://localhost:8080/api/user';

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private isLoggedSubject = new BehaviorSubject<boolean>(this.isTokenValid());
  public isLogged$ = this.isLoggedSubject.asObservable();

  constructor(private http: HttpClient) {
    // Charger l'utilisateur si un token existe
    if (this.isTokenValid()) {
      this.loadCurrentUser();
    }
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request).pipe(
      tap((response) => this.handleAuthResponse(response))
    );
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap((response) => this.handleAuthResponse(response))
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.isLoggedSubject.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isTokenValid(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }

    try {
      // Décoder le JWT pour vérifier l'expiration
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiration = payload.exp * 1000; // Convertir en millisecondes
      return Date.now() < expiration;
    } catch (error) {
      return false;
    }
  }

  private handleAuthResponse(response: AuthResponse): void {
    localStorage.setItem('token', response.token);
    const user: User = {
      id: response.id,
      email: response.email,
      username: response.username
    };
    this.currentUserSubject.next(user);
    this.isLoggedSubject.next(true);
  }

  private loadCurrentUser(): void {
    this.http.get<User>(`${this.userUrl}/me`).subscribe({
      next: (user) => {
        this.currentUserSubject.next(user);
        this.isLoggedSubject.next(true);
      },
      error: (err) => {
        // Uniquement logout si le token est rejeté par le serveur (401)
        // Pas pour les erreurs réseau temporaires
        if (err.status === 401) {
          this.logout();
        }
      }
    });
  }

  getCurrentUser(): Observable<User | null> {
    return this.currentUser$;
  }

  isAuthenticated(): boolean {
    return this.isLoggedSubject.value;
  }

  refreshCurrentUser(): void {
    this.loadCurrentUser();
  }
}
