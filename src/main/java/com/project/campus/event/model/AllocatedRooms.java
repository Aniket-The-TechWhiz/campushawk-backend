package com.project.campus.event.model;

import com.project.campus.room.model.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "confirmed_event_rooms")
public class AllocatedRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "confirmed_event_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_confirmed_event_room")
    )
    private ApprovedEvent approvedEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "room_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_allocated_room")
    )
    private Room room;
}