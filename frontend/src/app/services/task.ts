import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Task as TaskModel, TaskRequest, TaskStatus } from '../models/task.model';

@Injectable({ providedIn: 'root' })
export class Task {
  private apiUrl = `${environment.apiUrl}/tasks`;

  constructor(private http: HttpClient) {}

  create(req: TaskRequest): Observable<TaskModel> {
    return this.http.post<TaskModel>(this.apiUrl, req);
  }

  getProjectTasks(projectId: number): Observable<TaskModel[]> {
    return this.http.get<TaskModel[]>(`${this.apiUrl}/project/${projectId}`);
  }

  getMyTasks(): Observable<TaskModel[]> {
    return this.http.get<TaskModel[]>(`${this.apiUrl}/my-tasks`);
  }

  getOne(id: number): Observable<TaskModel> {
    return this.http.get<TaskModel>(`${this.apiUrl}/${id}`);
  }

  update(id: number, req: TaskRequest): Observable<TaskModel> {
    return this.http.put<TaskModel>(`${this.apiUrl}/${id}`, req);
  }

  updateStatus(id: number, status: TaskStatus): Observable<TaskModel> {
    return this.http.put<TaskModel>(`${this.apiUrl}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}