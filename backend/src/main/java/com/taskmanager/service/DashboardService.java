package com.taskmanager.service;

import com.taskmanager.dto.DashboardResponse;
import com.taskmanager.enums.Role;
import com.taskmanager.enums.TaskStatus;
import com.taskmanager.model.Project;
import com.taskmanager.model.ProjectMember;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectMemberRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;
    private final ProjectMemberRepository memberRepository;
    private final CurrentUser currentUser;

    public DashboardResponse getStats() {
        User me = currentUser.get();

        // Get all projects user is part of
        List<ProjectMember> myMemberships = memberRepository.findByUser(me);

        // Collect tasks from ALL projects user belongs to
        Set<Task> allTasks = new HashSet<>();
        for (ProjectMember pm : myMemberships) {
            Project project = pm.getProject();
            // Admin sees all project tasks; Member sees only own tasks in that project
            List<Task> projectTasks = taskRepository.findByProject(project);
            if (pm.getRole() == Role.ADMIN) {
                allTasks.addAll(projectTasks);
            } else {
                projectTasks.stream()
                        .filter(t -> t.getAssignedTo() != null && t.getAssignedTo().getId().equals(me.getId()))
                        .forEach(allTasks::add);
            }
        }

        long total = allTasks.size();
        long todo = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();

        LocalDate today = LocalDate.now();
        long overdue = allTasks.stream()
                .filter(t -> t.getDueDate() != null
                        && t.getDueDate().isBefore(today)
                        && t.getStatus() != TaskStatus.DONE)
                .count();

        // Tasks per user (across all visible tasks)
        Map<String, Long> tasksPerUser = allTasks.stream()
                .filter(t -> t.getAssignedTo() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getAssignedTo().getName(),
                        Collectors.counting()
                ));

        return new DashboardResponse(total, todo, inProgress, done, overdue, tasksPerUser);
    }
}