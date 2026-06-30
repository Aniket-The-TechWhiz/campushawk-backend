package com.project.campus.event.repository;

import com.project.campus.event.model.RequestedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestedEventRepository extends JpaRepository<RequestedEvent,Long> {
}
