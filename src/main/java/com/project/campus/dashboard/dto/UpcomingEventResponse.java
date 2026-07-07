package com.project.campus.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingEventResponse {

    private String eventName;

    private LocalDateTime startTime;

}
