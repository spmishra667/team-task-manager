package com.taskmanager.repository;


import com.taskmanager.enums.TaskStatus;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    List<Task> findByAssignedTo(User user);
    long countByAssignedToAndStatus(User user, TaskStatus status);
    long countByAssignedTo(User user);
    List<Task> findByAssignedToAndDueDateBeforeAndStatusNot(User user, LocalDate date, TaskStatus status);
}
