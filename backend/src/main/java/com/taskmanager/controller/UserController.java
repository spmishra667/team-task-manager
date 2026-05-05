package com.taskmanager.controller;

import com.taskmanager.dto.MemberResponse;
import com.taskmanager.enums.Role;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {


    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllUsers() {
        List<MemberResponse> users = userRepository.findAll().stream()
                .map(u -> new MemberResponse(u.getId(), u.getName(), u.getEmail(), Role.MEMBER))
                .toList();
        return ResponseEntity.ok(users);
    }
}
