package com.project.campus.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverviewResponse {

    private AnalyticsCountResponse analytics;

    private List<MonthlyActivityResponse> monthlyActivity;

    private StatusSplitResponse statusSplit;

    private List<UpcomingEventResponse> upcomingEvents;
}