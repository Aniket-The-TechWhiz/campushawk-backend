package com.project.campus.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocatedRoomResponse {

    private Long roomId;
    private String roomName;
}