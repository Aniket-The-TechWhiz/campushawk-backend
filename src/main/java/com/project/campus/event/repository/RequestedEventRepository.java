package com.project.campus.event.repository;

import com.project.campus.department.Department;
import com.project.campus.event.model.RequestStatus;
import com.project.campus.event.model.RequestedEvent;
import com.project.campus.user.model.Role;
import com.project.campus.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestedEventRepository extends JpaRepository<RequestedEvent, Long> {

    List<RequestedEvent> findByDepartment(Department department);

    List<RequestedEvent> findByUser(User user);

    List<RequestedEvent> findByUserRole(Role role);

    List<RequestedEvent> findByDepartmentAndUserRole(Department department, Role role);

    // Total events (excluding CANCELLED and REJECTED)
    @Query("""
            SELECT COUNT(e)
            FROM RequestedEvent e
            WHERE e.status NOT IN (
                com.project.campus.event.model.RequestStatus.CANCELLED,
                com.project.campus.event.model.RequestStatus.REJECTED
            )
            """)
    Long totalEvents();

    // Pending events
    @Query("""
            SELECT COUNT(e)
            FROM RequestedEvent e
            WHERE e.status = com.project.campus.event.model.RequestStatus.PENDING
            """)
    Long pendingEvents();

    // Approved events
    @Query("""
            SELECT COUNT(e)
            FROM RequestedEvent e
            WHERE e.status = com.project.campus.event.model.RequestStatus.APPROVED
            """)
    Long approvedEvents();

    // Department-wise monthly event count
    @Query("""
            SELECT COUNT(e)
            FROM RequestedEvent e
            WHERE e.department.id = :departmentId
              AND e.status = :status
              AND e.startTime BETWEEN :start AND :end
            """)
    Long countDepartmentEvents(
            @Param("departmentId") Long departmentId,
            @Param("status") RequestStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}