package com.project.campus.room.model;

import com.project.campus.department.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    @Column(name = "room_id",unique = true,nullable = false)
    private Long roomNumber;

    @Column(nullable = false)
    private String roomName;

    private String speciality;

    @Enumerated(EnumType.STRING)
    private Floor floor;

    private Integer capacity;

    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "department_id",
            foreignKey = @ForeignKey(name = "fk_room_department")
    )
    private Department department;
}
