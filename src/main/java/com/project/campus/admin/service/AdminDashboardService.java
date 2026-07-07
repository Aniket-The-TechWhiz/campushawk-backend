package com.project.campus.admin.service;


import com.project.campus.admin.dto.response.AdminDashboardDepartmentResponse;
import com.project.campus.admin.dto.response.AdminDashboardResponse;
import com.project.campus.department.Department;
import com.project.campus.department.DepartmentRepository;
import com.project.campus.event.model.RequestStatus;
import com.project.campus.event.repository.RequestedEventRepository;
import com.project.campus.user.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final DepartmentRepository departmentRepository;
    private final RequestedEventRepository requestedEventRepository;
    private final UserRepository userRepository;

    public AdminDashboardResponse getDashboard() {

        Long departmentCount = departmentRepository.count();

        Long totalEventCount = requestedEventRepository.totalEvents();

        Long pendingEventCount = requestedEventRepository.pendingEvents();

        Long approvedEventCount = requestedEventRepository.approvedEvents();

        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        List<Department> departments = departmentRepository.findAll();

        List<AdminDashboardDepartmentResponse> departmentResponses = new ArrayList<>();

        for (Department department : departments) {

            String hodName = userRepository.getHodName(department.getId());
            if (hodName == null) {
                hodName = "-";
            }

            Long facultyCount = userRepository.countFaculty(department.getId());
            if (facultyCount == null) {
                facultyCount = 0L;
            }

            Long pending = requestedEventRepository.countDepartmentEvents(
                    department.getId(),
                    RequestStatus.PENDING,
                    startOfMonth,
                    endOfMonth
            );

            Long approved = requestedEventRepository.countDepartmentEvents(
                    department.getId(),
                    RequestStatus.APPROVED,
                    startOfMonth,
                    endOfMonth
            );

            Long rejected = requestedEventRepository.countDepartmentEvents(
                    department.getId(),
                    RequestStatus.REJECTED,
                    startOfMonth,
                    endOfMonth
            );

            departmentResponses.add(
                    new AdminDashboardDepartmentResponse(
                            department.getDepartmentName(),
                            hodName,
                            facultyCount,
                            pending == null ? 0L : pending,
                            approved == null ? 0L : approved,
                            rejected == null ? 0L : rejected
                    )
            );
        }

        return new AdminDashboardResponse(
                departmentCount,
                totalEventCount == null ? 0L : totalEventCount,
                pendingEventCount == null ? 0L : pendingEventCount,
                approvedEventCount == null ? 0L : approvedEventCount,
                departmentResponses
        );
    }
}