import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Member } from '../models/project.model';

@Injectable({ providedIn: 'root' })
export class User {
  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<Member[]> {
    return this.http.get<Member[]>(`${environment.apiUrl}/users`);
  }
}