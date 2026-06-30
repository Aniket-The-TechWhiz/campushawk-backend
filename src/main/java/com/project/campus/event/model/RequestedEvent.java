package com.project.campus.event.model;

import com.project.campus.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_requests")
public class RequestedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(nullable = false,length = 500)
    private String purpose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User user;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String remarks;

    @OneToMany(
            mappedBy = "eventRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<RequestedRoom> requestedRooms = new ArrayList<>();
}