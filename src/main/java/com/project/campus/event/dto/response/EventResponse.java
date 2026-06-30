package com.project.campus.event.dto.response;

import com.project.campus.event.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String eventName;
    private String purpose;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RequestStatus status;
    private String remarks;
}
