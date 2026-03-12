package com.movie.movieticket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "hold")
@Getter
@Setter
public class Hold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seatId", nullable = false)
    private Seat seat;

    private String userName;
    private String userEmail;
    private String userPhone;

    private LocalDateTime createdAt;
    private LocalDateTime expireAt;

    @Enumerated(EnumType.STRING)
    private HoldStatus status;

    //No Arg Constructor
    public Hold() {}

    //All Arg Constructor
    public Hold(Seat seat, String userName, String userEmail, String userPhone) {
        this.seat = seat;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.createdAt = LocalDateTime.now();
        this.expireAt = createdAt.plusMinutes(6);
        this.status = HoldStatus.ACTIVE;
    }
}
