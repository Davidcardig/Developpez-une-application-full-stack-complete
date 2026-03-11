import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

export interface UpdateUserRequest {
  email?: string;
  username?: string;
  password?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) {}

  getProfile(): Observable<User> {
    return this.http.get<User>(`${this.userUrl}/me`);
  }

  updateProfile(request: UpdateUserRequest): Observable<User> {
    return this.http.put<User>(`${this.userUrl}/me`, request);
  }
}

