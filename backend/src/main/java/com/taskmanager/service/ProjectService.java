package com.taskmanager.service;

import com.taskmanager.dto.*;
import com.taskmanager.enums.Role;
import com.taskmanager.exception.BadRequestException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.UnauthorizedException;
import com.taskmanager.model.Project;
import com.taskmanager.model.ProjectMember;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectMemberRepository;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Transactional
    public ProjectResponse create(ProjectRequest req) {
        User me = currentUser.get();
        Project project = Project.builder()
                .name(req.getName())
                .description(req.getDescription())
                .createdBy(me)
                .build();
        projectRepository.save(project);

        ProjectMember pm = ProjectMember.builder()
                .project(project).user(me).role(Role.ADMIN).build();
        memberRepository.save(pm);

        return toResponse(project, Role.ADMIN);
    }

    public List<ProjectResponse> myProjects() {
        User me = currentUser.get();
        return memberRepository.findByUser(me).stream()
                .map(pm -> toResponse(pm.getProject(), pm.getRole()))
                .toList();
    }

    public ProjectResponse getOne(Long id) {
        User me = currentUser.get();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        ProjectMember pm = memberRepository.findByProjectAndUser(project, me)
                .orElseThrow(() -> new UnauthorizedException("Not a member of this project"));
        return toResponse(project, pm.getRole());
    }

    @Transactional
    public void delete(Long id) {
        User me = currentUser.get();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        requireAdmin(project, me);
        projectRepository.delete(project);
    }

    @Transactional
    public MemberResponse addMember(Long projectId, AddMemberRequest req) {
        User me = currentUser.get();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        requireAdmin(project, me);

        User newUser = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with this email not found"));

        if (memberRepository.existsByProjectAndUser(project, newUser))
            throw new BadRequestException("User already a member");

        ProjectMember pm = ProjectMember.builder()
                .project(project).user(newUser).role(req.getRole() == null ? Role.MEMBER : req.getRole()).build();
        memberRepository.save(pm);

        return new MemberResponse(newUser.getId(), newUser.getName(), newUser.getEmail(), pm.getRole());
    }

    public List<MemberResponse> listMembers(Long projectId) {
        User me = currentUser.get();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        memberRepository.findByProjectAndUser(project, me)
                .orElseThrow(() -> new UnauthorizedException("Not a member of this project"));
        return memberRepository.findByProject(project).stream()
                .map(pm -> new MemberResponse(pm.getUser().getId(), pm.getUser().getName(),
                        pm.getUser().getEmail(), pm.getRole()))
                .toList();
    }

    @Transactional
    public void removeMember(Long projectId, Long userId) {
        User me = currentUser.get();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        requireAdmin(project, me);
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (target.getId().equals(me.getId()))
            throw new BadRequestException("Admin cannot remove themselves");
        memberRepository.deleteByProjectAndUser(project, target);
    }

    private void requireAdmin(Project project, User user) {
        ProjectMember pm = memberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new UnauthorizedException("Not a project member"));
        if (pm.getRole() != Role.ADMIN)
            throw new UnauthorizedException("Admin access required");
    }

    private ProjectResponse toResponse(Project p, Role myRole) {
        return new ProjectResponse(p.getId(), p.getName(), p.getDescription(),
                p.getCreatedBy().getName(), p.getCreatedBy().getId(),
                myRole.name(), p.getCreatedAt());
    }
}
