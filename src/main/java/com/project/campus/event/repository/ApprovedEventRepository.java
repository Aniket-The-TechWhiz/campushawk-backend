package com.project.campus.event.repository;

import com.project.campus.event.model.ApprovedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovedEventRepository extends JpaRepository<ApprovedEvent,Long> {
    Optional<ApprovedEvent> findByEventRequestId(Long eventRequestId);
}
