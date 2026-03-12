package com.movie.movieticket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seat")
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    private double price;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    //No Arg constructor
    public Seat() {}

    //All arg constructor
    public Seat(String seatNumber, double price, SeatStatus status) {
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
    }
}