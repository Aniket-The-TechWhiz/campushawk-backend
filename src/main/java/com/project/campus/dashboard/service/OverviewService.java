package com.project.campus.dashboard.service;

import com.project.campus.dashboard.dto.*;
import com.project.campus.event.model.RequestStatus;
import com.project.campus.event.model.RequestedEvent;
import com.project.campus.event.repository.RequestedEventRepository;
import com.project.campus.department.Department;
import com.project.campus.department.DepartmentRepository;
import com.project.campus.user.model.User;
import com.project.campus.user.model.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OverviewService {

    private final RequestedEventRepository requestedEventRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public OverviewResponse getOverview() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Department department = departmentRepository.findById(user.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        List<RequestedEvent> departmentEvents =
                requestedEventRepository.findByDepartment(department);

        List<RequestedEvent> myEvents =
                requestedEventRepository.findByUser(user);

        OverviewResponse response = new OverviewResponse();

        response.setAnalytics(getAnalytics(departmentEvents));

        response.setMonthlyActivity(getMonthlyActivity(departmentEvents));

        response.setStatusSplit(getStatusSplit(myEvents));

        response.setUpcomingEvents(getUpcomingEvents(departmentEvents));

        return response;
    }

    private AnalyticsCountResponse getAnalytics(List<RequestedEvent> events) {

        LocalDate today = LocalDate.now();

        return new AnalyticsCountResponse(

                events.size(),

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.APPROVED)
                        .count(),

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.PENDING)
                        .count(),

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.REJECTED)
                        .count(),

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.CANCELLED)
                        .count(),

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.APPROVED)
                        .filter(e ->
                                !e.getStartTime().toLocalDate().isAfter(today) &&
                                        !e.getEndTime().toLocalDate().isBefore(today)
                        )
                        .count()
        );
    }
    private List<MonthlyActivityResponse> getMonthlyActivity(List<RequestedEvent> events) {

        Map<Month, Long> monthCount = events.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getStartTime().getMonth(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        return monthCount.entrySet()
                .stream()
                .map(entry -> new MonthlyActivityResponse(
                        entry.getKey().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        entry.getValue().intValue()
                ))
                .toList();
    }
    private StatusSplitResponse getStatusSplit(List<RequestedEvent> events) {

        return new StatusSplitResponse(

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.APPROVED)
                        .count(),

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.PENDING)
                        .count(),

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.REJECTED)
                        .count(),

                (int) events.stream()
                        .filter(e -> e.getStatus() == RequestStatus.CANCELLED)
                        .count()
        );
    }
    private List<UpcomingEventResponse> getUpcomingEvents(List<RequestedEvent> events) {

        LocalDateTime now = LocalDateTime.now();

        return events.stream()

                .filter(e -> e.getStatus() == RequestStatus.APPROVED)

                .filter(e -> e.getStartTime().isAfter(now))

                .sorted(Comparator.comparing(RequestedEvent::getStartTime))

                .map(e -> new UpcomingEventResponse(
                        e.getEventName(),
                        e.getStartTime()
                ))

                .toList();
    }
}
