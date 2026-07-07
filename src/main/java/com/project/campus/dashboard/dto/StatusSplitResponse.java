package com.project.campus.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusSplitResponse {

    private Integer approved;

    private Integer pending;

    private Integer rejected;

    private Integer cancelled;

}
