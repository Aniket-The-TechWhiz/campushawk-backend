package com.project.campus.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    public Department addDepartment(Department request) {
        return departmentRepository.save(request);
    }

    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }
}
