package com.project.campus.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponse {

    private Long departmentCount;
    private Long totalEventCount;
    private Long pendingEventCount;
    private Long approvedEventCount;

    private List<AdminDashboardDepartmentResponse> departments;
}
