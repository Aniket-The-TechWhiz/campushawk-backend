package com.project.campus.user;

import com.project.campus.room.model.Department;
import com.project.campus.room.repository.DepartmentRepository;
import com.project.campus.user.dto.UserRequest;
import com.project.campus.user.dto.UserResponse;
import com.project.campus.user.model.User;
import com.project.campus.user.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserResponse addUser(UserRequest request) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setRole(request.getRole());
        user.setDepartment(department);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole(),
                savedUser.getDepartment().getId()
        );
    }

    public List<UserResponse> getAllUser() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getRole(),
                        user.getDepartment().getId()
                ))
                .toList();
    }
}