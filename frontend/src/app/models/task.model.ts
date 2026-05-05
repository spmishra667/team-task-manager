export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface Task {
  id: number;
  title: string;
  description: string;
  status: TaskStatus;
  priority: Priority;
  dueDate: string;
  projectId: number;
  projectName: string;
  assignedToUserId: number | null;
  assignedToName: string | null;
  createdAt: string;
}

export interface TaskRequest {
  title: string;
  description: string;
  dueDate: string;
  priority: Priority;
  projectId: number;
  assignedToUserId: number | null;
}