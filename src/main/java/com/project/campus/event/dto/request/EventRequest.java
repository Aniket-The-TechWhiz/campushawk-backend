package com.project.campus.event.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    private String eventName;
    private String purpose;
    //private Long userId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String remarks;

    private List<Long> roomIds;
}
