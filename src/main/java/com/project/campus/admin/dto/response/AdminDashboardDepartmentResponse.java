package com.project.campus.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDepartmentResponse {

    private String departmentName;
    private String hodName;
    private Long facultyCount;

    private Long pendingEventCount;
    private Long approvedEventCount;
    private Long rejectedEventCount;
}