package com.movie.movieticket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class CreateSeatsRequest {
    private List<String> seatNumbers;

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }
}
