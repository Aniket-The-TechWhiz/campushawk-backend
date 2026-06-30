package com.project.campus.event.repository;

import com.project.campus.event.model.RequestedEvent;
import com.project.campus.event.model.RequestedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestedRoomRepository extends JpaRepository<RequestedRoom,Long> {
    void deleteByEventRequest(RequestedEvent event);
}
