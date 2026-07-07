package com.project.campus.department;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<Department> addDepartment(@RequestBody Department request) {
        Department response = departmentService.addDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/getAllDepartment")
    public ResponseEntity<List<Department>> getAllDepartment(){
        return ResponseEntity.ok(departmentService.getAllDepartment());
    }
}
