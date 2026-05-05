import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AddMemberRequest, Member, Project as ProjectModel, ProjectRequest } from '../models/project.model';

@Injectable({ providedIn: 'root' })
export class Project {
  private apiUrl = `${environment.apiUrl}/projects`;

  constructor(private http: HttpClient) {}

  create(req: ProjectRequest): Observable<ProjectModel> {
    return this.http.post<ProjectModel>(this.apiUrl, req);
  }

  getMyProjects(): Observable<ProjectModel[]> {
    return this.http.get<ProjectModel[]>(this.apiUrl);
  }

  getOne(id: number): Observable<ProjectModel> {
    return this.http.get<ProjectModel>(`${this.apiUrl}/${id}`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  addMember(projectId: number, req: AddMemberRequest): Observable<Member> {
    return this.http.post<Member>(`${this.apiUrl}/${projectId}/members`, req);
  }

  getMembers(projectId: number): Observable<Member[]> {
    return this.http.get<Member[]>(`${this.apiUrl}/${projectId}/members`);
  }

  removeMember(projectId: number, userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${projectId}/members/${userId}`);
  }
}