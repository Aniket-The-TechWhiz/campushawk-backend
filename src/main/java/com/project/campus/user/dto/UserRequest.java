package com.project.campus.user.dto;

import com.project.campus.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String password;
    private String name;
    private Role role;
    private Long departmentId;
}
