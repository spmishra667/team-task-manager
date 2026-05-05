import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Project as ProjectService } from '../../services/project';
import { Task as TaskService } from '../../services/task';
import { Project as ProjectModel, Member } from '../../models/project.model';
import { Task as TaskModel, TaskStatus, Priority } from '../../models/task.model';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './project-detail.html',
  styleUrl: './project-detail.css'
})
export class ProjectDetail implements OnInit {
  projectId!: number;
  project: ProjectModel | null = null;
  members: Member[] = [];
  tasks: TaskModel[] = [];
  loading = true;

  // Add Member
  showMemberModal = false;
  newMemberEmail = '';

  // Task Form
  showTaskModal = false;
  editingTaskId: number | null = null;
  taskForm = {
    title: '', description: '', dueDate: '',
    priority: 'MEDIUM' as Priority, assignedToUserId: null as number | null
  };

  constructor(private route: ActivatedRoute,
              private projectService: ProjectService,
              private taskService: TaskService) {}

  ngOnInit() {
    this.projectId = +this.route.snapshot.paramMap.get('id')!;
    this.loadAll();
  }

  loadAll() {
    this.loading = true;
    this.projectService.getOne(this.projectId).subscribe(p => this.project = p);
    this.projectService.getMembers(this.projectId).subscribe(m => this.members = m);
    this.taskService.getProjectTasks(this.projectId).subscribe({
      next: t => { this.tasks = t; this.loading = false; },
      error: () => this.loading = false
    });
  }

  isAdmin(): boolean { return this.project?.myRole === 'ADMIN'; }

  // ----- Members -----
  openAddMember() { this.showMemberModal = true; this.newMemberEmail = ''; }
  closeAddMember() { this.showMemberModal = false; }

  addMember() {
    if (!this.newMemberEmail) return;
    this.projectService.addMember(this.projectId, { email: this.newMemberEmail, role: 'MEMBER' }).subscribe({
      next: () => { this.closeAddMember(); this.loadAll(); },
      error: (err) => alert(err.error?.message || 'Failed')
    });
  }

  removeMember(userId: number) {
    if (!confirm('Remove this member?')) return;
    this.projectService.removeMember(this.projectId, userId).subscribe({
      next: () => this.loadAll(),
      error: () => alert('Failed to remove')
    });
  }

  // ----- Tasks -----
  openTaskModal(task?: TaskModel) {
    this.showTaskModal = true;
    if (task) {
      this.editingTaskId = task.id;
      this.taskForm = {
        title: task.title, description: task.description || '',
        dueDate: task.dueDate || '', priority: task.priority,
        assignedToUserId: task.assignedToUserId
      };
    } else {
      this.editingTaskId = null;
      this.taskForm = { title: '', description: '', dueDate: '', priority: 'MEDIUM', assignedToUserId: null };
    }
  }

  closeTaskModal() { this.showTaskModal = false; }

  saveTask() {
    if (!this.taskForm.title.trim()) return;
    const payload = { ...this.taskForm, projectId: this.projectId };
    const obs = this.editingTaskId
      ? this.taskService.update(this.editingTaskId, payload)
      : this.taskService.create(payload);
    obs.subscribe({
      next: () => { this.closeTaskModal(); this.loadAll(); },
      error: (err) => alert(err.error?.message || 'Failed')
    });
  }

  deleteTask(id: number) {
    if (!confirm('Delete this task?')) return;
    this.taskService.delete(id).subscribe({
      next: () => this.loadAll(),
      error: () => alert('Failed')
    });
  }

  updateStatus(task: TaskModel, status: TaskStatus) {
    this.taskService.updateStatus(task.id, status).subscribe({
      next: () => this.loadAll(),
      error: () => alert('Failed to update')
    });
  }

  isOverdue(task: TaskModel): boolean {
    if (!task.dueDate || task.status === 'DONE') return false;
    return new Date(task.dueDate) < new Date(new Date().toDateString());
  }

  getStatusBadge(s: TaskStatus): string {
    return s === 'TODO' ? 'bg-secondary' : s === 'IN_PROGRESS' ? 'bg-warning' : 'bg-success';
  }

  getPriorityBadge(p: Priority): string {
    return p === 'HIGH' ? 'bg-danger' : p === 'MEDIUM' ? 'bg-warning' : 'bg-info';
  }
}