package com.taskmanager.security;

import com.taskmanager.exception.UnauthorizedException;
import com.taskmanager.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component

public class CurrentUser {
    public User get() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) return user;
        throw new UnauthorizedException("Not authenticated");
    }
}
