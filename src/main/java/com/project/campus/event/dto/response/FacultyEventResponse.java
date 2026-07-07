package com.project.campus.event.dto.response;

import com.project.campus.room.dto.response.RequestedRoomResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyEventResponse {

    private Long eventId;

    private String facultyName;
    private String eventName;
    private String eventPurpose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<RequestedRoomResponse> rooms;

    private String eventStatus;
}
