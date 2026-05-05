import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Task as TaskService } from '../../services/task';
import { Task as TaskModel, TaskStatus } from '../../models/task.model';

@Component({
  selector: 'app-my-tasks',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './my-tasks.html',
  styleUrl: './my-tasks.css'
})
export class MyTasks implements OnInit {
  tasks: TaskModel[] = [];
  loading = true;
  filterStatus: string = 'ALL';

  constructor(private taskService: TaskService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.taskService.getMyTasks().subscribe({
      next: t => { this.tasks = t; this.loading = false; },
      error: () => this.loading = false
    });
  }

  get filteredTasks(): TaskModel[] {
    if (this.filterStatus === 'ALL') return this.tasks;
    if (this.filterStatus === 'OVERDUE') return this.tasks.filter(t => this.isOverdue(t));
    return this.tasks.filter(t => t.status === this.filterStatus);
  }

  updateStatus(task: TaskModel, status: TaskStatus) {
    this.taskService.updateStatus(task.id, status).subscribe({
      next: () => this.load(),
      error: () => alert('Failed')
    });
  }

  isOverdue(task: TaskModel): boolean {
    if (!task.dueDate || task.status === 'DONE') return false;
    return new Date(task.dueDate) < new Date(new Date().toDateString());
  }

  getStatusBadge(s: TaskStatus): string {
    return s === 'TODO' ? 'bg-secondary' : s === 'IN_PROGRESS' ? 'bg-warning' : 'bg-success';
  }

  getPriorityBadge(p: string): string {
    return p === 'HIGH' ? 'bg-danger' : p === 'MEDIUM' ? 'bg-warning' : 'bg-info';
  }
}