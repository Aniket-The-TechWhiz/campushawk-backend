package com.project.campus.event.repository;

import com.project.campus.event.model.RequestedEvent;
import com.project.campus.room.model.Department;
import com.project.campus.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestedEventRepository extends JpaRepository<RequestedEvent,Long> {
    List<RequestedEvent> findByDepartment(Department department);
    List<RequestedEvent> findByUser(User user);
}
