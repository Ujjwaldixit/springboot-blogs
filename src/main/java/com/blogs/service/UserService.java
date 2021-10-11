package com.blogs.service;

import com.blogs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    boolean register(User user);

    List<User> getAllUsers();
}
