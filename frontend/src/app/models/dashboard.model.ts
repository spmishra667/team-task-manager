export interface DashboardStats {
  totalTasks: number;
  todoCount: number;
  inProgressCount: number;
  doneCount: number;
  overdueCount: number;
  tasksPerUser: { [key: string]: number };
}