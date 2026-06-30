package com.project.campus.event.dto.response;

import com.project.campus.event.model.RequestedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovedEventResponse {
    private Long id;
    private Long eventRequestId;
    private String eventName;
    private String eventPurpose;
    private Long userId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String remark;
    private List<Long> allocatedRoomIds;
}
