package com.project.campus.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestedRoomResponse {

    private Long roomId;

    private String roomName;
}
