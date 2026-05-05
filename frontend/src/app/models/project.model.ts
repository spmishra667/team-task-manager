export interface Project {
  id: number;
  name: string;
  description: string;
  createdByName: string;
  createdById: number;
  myRole: 'ADMIN' | 'MEMBER';
  createdAt: string;
}

export interface ProjectRequest {
  name: string;
  description: string;
}

export interface Member {
  userId: number;
  name: string;
  email: string;
  role: 'ADMIN' | 'MEMBER';
}

export interface AddMemberRequest {
  email: string;
  role: 'ADMIN' | 'MEMBER';
}