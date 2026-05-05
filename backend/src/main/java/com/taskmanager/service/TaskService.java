package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.dto.UpdateStatusRequest;
import com.taskmanager.enums.Role;
import com.taskmanager.enums.TaskStatus;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.UnauthorizedException;
import com.taskmanager.model.Project;
import com.taskmanager.model.ProjectMember;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectMemberRepository;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Transactional
    public TaskResponse create(TaskRequest req) {
        User me = currentUser.get();
        Project project = projectRepository.findById(req.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        requireAdmin(project, me);

        User assignee = null;
        if (req.getAssignedToUserId() != null) {
            assignee = userRepository.findById(req.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            if (!memberRepository.existsByProjectAndUser(project, assignee))
                throw new UnauthorizedException("Assignee is not a project member");
        }

        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .dueDate(req.getDueDate())
                .priority(req.getPriority())
                .status(TaskStatus.TODO)
                .project(project)
                .assignedTo(assignee)
                .build();
        taskRepository.save(task);
        return toResponse(task);
    }

    public List<TaskResponse> getProjectTasks(Long projectId) {
        User me = currentUser.get();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        memberRepository.findByProjectAndUser(project, me)
                .orElseThrow(() -> new UnauthorizedException("Not a project member"));
        return taskRepository.findByProject(project).stream().map(this::toResponse).toList();
    }

    public List<TaskResponse> myTasks() {
        User me = currentUser.get();
        return taskRepository.findByAssignedTo(me).stream().map(this::toResponse).toList();
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest req) {
        User me = currentUser.get();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        requireAdmin(task.getProject(), me);

        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setDueDate(req.getDueDate());
        task.setPriority(req.getPriority());

        if (req.getAssignedToUserId() != null) {
            User assignee = userRepository.findById(req.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            if (!memberRepository.existsByProjectAndUser(task.getProject(), assignee))
                throw new UnauthorizedException("Assignee not a project member");
            task.setAssignedTo(assignee);
        } else {
            task.setAssignedTo(null);
        }
        taskRepository.save(task);
        return toResponse(task);
    }

    @Transactional
    public TaskResponse updateStatus(Long id, UpdateStatusRequest req) {
        User me = currentUser.get();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        ProjectMember pm = memberRepository.findByProjectAndUser(task.getProject(), me)
                .orElseThrow(() -> new UnauthorizedException("Not a project member"));

        boolean isAssignee = task.getAssignedTo() != null && task.getAssignedTo().getId().equals(me.getId());
        if (pm.getRole() != Role.ADMIN && !isAssignee)
            throw new UnauthorizedException("Only admin or assignee can update status");

        task.setStatus(req.getStatus());
        taskRepository.save(task);
        return toResponse(task);
    }

    @Transactional
    public void delete(Long id) {
        User me = currentUser.get();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        requireAdmin(task.getProject(), me);
        taskRepository.delete(task);
    }

    private void requireAdmin(Project project, User user) {
        ProjectMember pm = memberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new UnauthorizedException("Not a project member"));
        if (pm.getRole() != Role.ADMIN)
            throw new UnauthorizedException("Admin access required");
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(
                t.getId(), t.getTitle(), t.getDescription(),
                t.getStatus(), t.getPriority(), t.getDueDate(),
                t.getProject().getId(), t.getProject().getName(),
                t.getAssignedTo() != null ? t.getAssignedTo().getId() : null,
                t.getAssignedTo() != null ? t.getAssignedTo().getName() : null,
                t.getCreatedAt()
        );
    }
}
