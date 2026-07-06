package com.project.campus.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyEventResponse {
    private String facultyName;
    private String eventName;
    private String eventPurpose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> room;
    private String eventStatus;
}
