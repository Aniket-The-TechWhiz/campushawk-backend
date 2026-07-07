package com.project.campus.room.service;

import com.project.campus.room.dto.request.RoomRequest;
import com.project.campus.room.dto.response.RoomResponse;
import com.project.campus.department.Department;
import com.project.campus.room.model.Room;
import com.project.campus.department.DepartmentRepository;
import com.project.campus.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final DepartmentRepository departmentRepository;

    public RoomResponse addRoom(RoomRequest request) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomName(request.getRoomName());
        room.setSpeciality(request.getSpeciality());
        room.setFloor(request.getFloor());
        room.setCapacity(request.getCapacity());
        room.setDepartment(department);

        Room savedRoom = roomRepository.save(room);

        return new RoomResponse(
                savedRoom.getRoomNumber(),
                savedRoom.getRoomName(),
                savedRoom.getSpeciality(),
                savedRoom.getFloor(),
                savedRoom.getCapacity(),
                savedRoom.getDepartment().getId()
        );
    }

    public List<RoomResponse> getAllRooms() {

        return roomRepository.findAll()
                .stream()
                .map(room -> new RoomResponse(
                        room.getRoomNumber(),
                        room.getRoomName(),
                        room.getSpeciality(),
                        room.getFloor(),
                        room.getCapacity(),
                        room.getDepartment().getId()
                ))
                .toList();
    }

    public RoomResponse getRoomById(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return new RoomResponse(
                room.getRoomNumber(),
                room.getRoomName(),
                room.getSpeciality(),
                room.getFloor(),
                room.getCapacity(),
                room.getDepartment().getId()
        );
    }

    public String deleteRoom(Long id) {

        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }

        roomRepository.deleteById(id);

        return "Deleted Successfully";
    }
}