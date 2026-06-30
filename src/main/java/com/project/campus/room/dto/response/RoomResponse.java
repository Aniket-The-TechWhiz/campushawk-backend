package com.project.campus.room.dto.response;

import com.project.campus.room.model.Department;
import com.project.campus.room.model.Floor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long roomNumber;
    private String roomName;
    private String speciality;
    private Floor floor;
    private Integer capacity;
    private Long departmentId;
}
