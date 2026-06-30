package com.project.campus.event.controller;

import com.project.campus.event.dto.request.ApproveEventRequest;
import com.project.campus.event.dto.request.EventRequest;
import com.project.campus.event.dto.response.ApprovedEventResponse;
import com.project.campus.event.dto.response.EventResponse;
import com.project.campus.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest request){
        EventResponse response=eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequest request) {

        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approve(
            @PathVariable Long id,
            @RequestBody ApproveEventRequest request){

        eventService.approve(id,request);

        return ResponseEntity.ok("Approved");
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteRequestedEvent(@PathVariable Long id){
        eventService.deleteRequestedEvent(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectRequestedEvent(@PathVariable Long id,@RequestParam String reason){
        eventService.rejectRequestedEvent(id,reason);
        return ResponseEntity.ok("Rejected event request");
    }

    @DeleteMapping("/{id}/cancelApprovedRequest")
    public ResponseEntity<String> cancelApprovedEvent(@PathVariable Long id){
        eventService.cancelApprovedEvent(id);
        return ResponseEntity.ok("Cancelled Successfully");
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ApprovedEventResponse>> getAllApprovedEvents() {
        return ResponseEntity.ok(eventService.getAllApprovedEvents());
    }
}
