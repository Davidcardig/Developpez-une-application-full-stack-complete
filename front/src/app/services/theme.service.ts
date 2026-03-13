import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theme } from '../models/theme.model';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private apiUrl = 'http://localhost:8080/api/themes';

  constructor(private http: HttpClient) {}

  getAllThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(this.apiUrl);
  }

  getSubscribedThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/subscribed`);
  }


  subscribe(themeId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${themeId}/subscribe`, {});
  }


  unsubscribe(themeId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${themeId}/subscribe`);
  }
}

