package com.project.campus.event.service;

import com.project.campus.event.dto.request.ApproveEventRequest;
import com.project.campus.event.dto.request.EventRequest;
import com.project.campus.event.dto.response.ApprovedEventResponse;
import com.project.campus.event.dto.response.EventResponse;
import com.project.campus.event.model.*;
import com.project.campus.event.repository.AllocatedRoomRepository;
import com.project.campus.event.repository.ApprovedEventRepository;
import com.project.campus.event.repository.RequestedEventRepository;
import com.project.campus.event.repository.RequestedRoomRepository;
import com.project.campus.room.model.*;
import com.project.campus.room.repository.*;
import com.project.campus.user.model.User;
import com.project.campus.user.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final RequestedEventRepository requestedEventRepository;
    private final UserRepository userRepository;
    private final ApprovedEventRepository approvedEventRepository;
    private final RoomRepository roomRepository;
    private final RequestedRoomRepository requestedRoomRepository;
    private final AllocatedRoomRepository allocatedRoomRepository;

    @Transactional
    public EventResponse createEvent(EventRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RequestedEvent event = new RequestedEvent();

        event.setEventName(request.getEventName());
        event.setPurpose(request.getPurpose());
        event.setUser(user);          // Logged-in user
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setStatus(RequestStatus.PENDING);
        event.setRemarks(request.getRemarks());

        RequestedEvent savedEvent = requestedEventRepository.save(event);

        // Save requested rooms
        for (Long roomId : request.getRoomIds()) {

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

            RequestedRoom requestedRoom = new RequestedRoom();
            requestedRoom.setEventRequest(savedEvent);
            requestedRoom.setRoom(room);

            requestedRoomRepository.save(requestedRoom);
        }

        // Response
        EventResponse response = new EventResponse();
        response.setId(savedEvent.getId());
        response.setEventName(savedEvent.getEventName());
        response.setPurpose(savedEvent.getPurpose());
        response.setUserId(savedEvent.getUser().getId());
        response.setStartTime(savedEvent.getStartTime());
        response.setEndTime(savedEvent.getEndTime());
        response.setStatus(savedEvent.getStatus());
        response.setRemarks(savedEvent.getRemarks());

        return response;
    }

    @Transactional
    public void approve(Long eventId, ApproveEventRequest request){

        RequestedEvent event = requestedEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User approver = userRepository.findById(request.getApprovedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        event.setStatus(RequestStatus.APPROVED);

        ApprovedEvent approved = new ApprovedEvent();

        approved.setEventRequest(event);
        approved.setApprovedBy(approver);
        approved.setApprovedAt(LocalDateTime.now());
        approved.setStartDateTime(event.getStartTime());
        approved.setEndDateTime(event.getEndTime());
        approved.setStatus(RequestStatus.ACTIVE);

        approved = approvedEventRepository.save(approved);

        for(Long roomId : request.getAllocatedRoomIds()){

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found"));

            boolean occupied =
                    allocatedRoomRepository
                            .existsByRoomRoomNumberAndApprovedEventStartDateTimeLessThanAndApprovedEventEndDateTimeGreaterThan(
                                    roomId,
                                    event.getEndTime(),
                                    event.getStartTime());

            if(occupied){
                throw new RuntimeException("Room " + roomId + " already allocated.");
            }

            AllocatedRooms allocated = new AllocatedRooms();

            allocated.setApprovedEvent(approved);
            allocated.setRoom(room);

            allocatedRoomRepository.save(allocated);
        }
    }

    @Transactional
    public void deleteRequestedEvent(Long id) {

        RequestedEvent event = requestedEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Prevent deleting an approved event
        if (event.getStatus() == RequestStatus.APPROVED) {
            throw new RuntimeException("Approved events cannot be deleted.");
        }

        // Delete all requested rooms
        requestedRoomRepository.deleteByEventRequest(event);

        // Delete the event
        requestedEventRepository.delete(event);
    }

    public void rejectRequestedEvent(Long id, String reason) {
        RequestedEvent event = requestedEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        if(event.getStatus()==RequestStatus.PENDING ){
            event.setRemarks(reason);
            event.setStatus(RequestStatus.REJECTED);
            requestedEventRepository.save(event);
        }
        else{
            throw new RuntimeException("Only pending request can be rejected!");
        }
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request) {

        // Find event
        RequestedEvent event = requestedEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Only Pending or Rejected events can be updated
        if (event.getStatus() != RequestStatus.PENDING &&
                event.getStatus() != RequestStatus.REJECTED) {

            throw new RuntimeException("Only PENDING or REJECTED events can be updated.");
        }

        // Get logged-in user from JWT
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Security Check:
        // Only the creator of the event can update it
        if (!event.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to update this event.");
        }

        // Update event details
        event.setEventName(request.getEventName());
        event.setPurpose(request.getPurpose());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setRemarks(request.getRemarks());

        // If rejected, send it again for approval
        event.setStatus(RequestStatus.PENDING);

        RequestedEvent updatedEvent = requestedEventRepository.save(event);

        // Remove old requested rooms
        requestedRoomRepository.deleteByEventRequest(updatedEvent);

        // Save newly selected rooms
        for (Long roomId : request.getRoomIds()) {

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() ->
                            new RuntimeException("Room not found: " + roomId));

            RequestedRoom requestedRoom = new RequestedRoom();
            requestedRoom.setEventRequest(updatedEvent);
            requestedRoom.setRoom(room);

            requestedRoomRepository.save(requestedRoom);
        }

        // Prepare response
        EventResponse response = new EventResponse();
        response.setId(updatedEvent.getId());
        response.setEventName(updatedEvent.getEventName());
        response.setPurpose(updatedEvent.getPurpose());
        response.setUserId(updatedEvent.getUser().getId());
        response.setStartTime(updatedEvent.getStartTime());
        response.setEndTime(updatedEvent.getEndTime());
        response.setStatus(updatedEvent.getStatus());
        response.setRemarks(updatedEvent.getRemarks());

        return response;
    }

    @Transactional
    public void cancelApprovedEvent(Long approvedEventId) {

        ApprovedEvent approvedEvent = approvedEventRepository.findById(approvedEventId)
                .orElseThrow(() -> new RuntimeException("Approved event not found"));

        // Update original request status (optional)
        RequestedEvent requestedEvent = approvedEvent.getEventRequest();
        requestedEvent.setStatus(RequestStatus.CANCELLED);

        // Delete all allocated rooms
        allocatedRoomRepository.deleteByApprovedEvent(approvedEvent);

        // Delete approved event
        approvedEventRepository.delete(approvedEvent);
    }

    @Transactional(readOnly = true)
    public List<ApprovedEventResponse> getAllApprovedEvents() {

        List<ApprovedEvent> approvedEvents = approvedEventRepository.findAll();

        return approvedEvents.stream()
                .map(approvedEvent -> {

                    RequestedEvent requestedEvent = approvedEvent.getEventRequest();

                    List<Long> allocatedRoomIds = approvedEvent.getAllocatedRooms()
                            .stream()
                            .map(allocatedRoom -> allocatedRoom.getRoom().getRoomNumber())
                            .toList();

                    ApprovedEventResponse response = new ApprovedEventResponse();

                    response.setId(approvedEvent.getId());
                    response.setEventRequestId(requestedEvent.getId());
                    response.setEventName(requestedEvent.getEventName());
                    response.setEventPurpose(requestedEvent.getPurpose());
                    response.setUserId(requestedEvent.getUser().getId());
                    response.setStartDateTime(approvedEvent.getStartDateTime());
                    response.setEndDateTime(approvedEvent.getEndDateTime());
                    response.setRemark(requestedEvent.getRemarks());
                    response.setAllocatedRoomIds(allocatedRoomIds);

                    return response;
                })
                .toList();
    }
}
