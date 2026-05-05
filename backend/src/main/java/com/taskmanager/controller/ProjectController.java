package com.taskmanager.controller;

import com.taskmanager.dto.*;
import com.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor

public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest req) {
        return ResponseEntity.ok(projectService.create(req));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> myProjects() {
        return ResponseEntity.ok(projectService.myProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<MemberResponse> addMember(@PathVariable Long id,
                                                    @Valid @RequestBody AddMemberRequest req) {
        return ResponseEntity.ok(projectService.addMember(id, req));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<MemberResponse>> listMembers(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.listMembers(id));
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id, @PathVariable Long userId) {
        projectService.removeMember(id, userId);
        return ResponseEntity.noContent().build();
    }
}
