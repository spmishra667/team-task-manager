import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Dashboard as DashboardService } from '../../services/dashboard';
import { DashboardStats } from '../../models/dashboard.model';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  stats: DashboardStats | null = null;
  loading = true;
  error = '';
  user: any;
  userKeys: string[] = [];

  constructor(private dashboardService: DashboardService, private auth: Auth) {
    this.user = auth.getUser();
  }

  ngOnInit() {
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.userKeys = Object.keys(data.tasksPerUser || {});
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load dashboard';
        this.loading = false;
      }
    });
  }

  getUserCount(name: string): number {
    return this.stats?.tasksPerUser[name] || 0;
  }
}