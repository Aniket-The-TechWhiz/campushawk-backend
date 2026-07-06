package com.project.campus.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRequestedEventResponse {
    private String eventName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private List<String> room;
    private String remark;
}
