package com.project.campus.event.repository;

import com.project.campus.event.model.AllocatedRooms;
import com.project.campus.event.model.ApprovedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AllocatedRoomRepository extends JpaRepository<AllocatedRooms,Long> {
    boolean existsByRoomRoomNumberAndApprovedEventStartDateTimeLessThanAndApprovedEventEndDateTimeGreaterThan(
            Long roomNumber,
            LocalDateTime end,
            LocalDateTime start
    );
    void deleteByApprovedEvent(ApprovedEvent approvedEvent);
    List<AllocatedRooms> findByApprovedEvent(ApprovedEvent approvedEvent);
}
