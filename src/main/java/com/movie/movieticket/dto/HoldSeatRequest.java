package com.movie.movieticket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HoldSeatRequest {
    private List<Long> seatIds;
    private UserDetailsDTO userDetails;

}
