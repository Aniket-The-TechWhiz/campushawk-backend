package com.project.campus.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsCountResponse {

    private Integer totalDepartmentEvents;

    private Integer approvedEvents;

    private Integer pendingEvents;

    private Integer rejectedEvents;

    private Integer cancelledEvents;

    private Integer todayEvents;

}
