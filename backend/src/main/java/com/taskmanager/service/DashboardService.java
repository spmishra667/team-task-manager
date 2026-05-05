package com.taskmanager.service;

import com.taskmanager.dto.DashboardResponse;
import com.taskmanager.enums.TaskStatus;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class DashboardService {

    private final TaskRepository taskRepository;
    private final CurrentUser currentUser;

    public DashboardResponse getStats() {
        User me = currentUser.get();
        List<Task> myTasks = taskRepository.findByAssignedTo(me);

        long total = myTasks.size();
        long todo = myTasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = myTasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = myTasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();

        LocalDate today = LocalDate.now();
        long overdue = myTasks.stream()
                .filter(t -> t.getDueDate() != null
                        && t.getDueDate().isBefore(today)
                        && t.getStatus() != TaskStatus.DONE)
                .count();

        Map<String, Long> tasksPerUser = myTasks.stream()
                .filter(t -> t.getAssignedTo() != null)
                .collect(Collectors.groupingBy(t -> t.getAssignedTo().getName(), Collectors.counting()));

        return new DashboardResponse(total, todo, inProgress, done, overdue, tasksPerUser);
    }
}
