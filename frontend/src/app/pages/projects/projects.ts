import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Project as ProjectService } from '../../services/project';
import { Project as ProjectModel } from '../../models/project.model';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './projects.html',
  styleUrl: './projects.css'
})
export class Projects implements OnInit {
  projects: ProjectModel[] = [];
  loading = true;
  error = '';

  showModal = false;
  newName = '';
  newDescription = '';
  creating = false;

  constructor(private projectService: ProjectService) {}

  ngOnInit() { this.loadProjects(); }

  loadProjects() {
    this.loading = true;
    this.projectService.getMyProjects().subscribe({
      next: (data) => { this.projects = data; this.loading = false; },
      error: () => { this.error = 'Failed to load projects'; this.loading = false; }
    });
  }

  openCreateModal() {
    this.showModal = true;
    this.newName = ''; this.newDescription = '';
  }

  closeModal() { this.showModal = false; }

  createProject() {
    if (!this.newName.trim()) return;
    this.creating = true;
    this.projectService.create({ name: this.newName, description: this.newDescription }).subscribe({
      next: () => { this.creating = false; this.closeModal(); this.loadProjects(); },
      error: () => { this.creating = false; alert('Failed to create project'); }
    });
  }

  deleteProject(id: number, event: Event) {
    event.stopPropagation();
    event.preventDefault();
    if (!confirm('Delete this project?')) return;
    this.projectService.delete(id).subscribe({
      next: () => this.loadProjects(),
      error: () => alert('Only admin can delete')
    });
  }
}