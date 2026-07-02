package com.project.campus.notification.model;

import com.project.campus.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String title;

    @Column(length = 1000)
    private String message;

    private Boolean isRead = false;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type;
}
