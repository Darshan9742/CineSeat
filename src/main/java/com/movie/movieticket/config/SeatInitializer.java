package com.movie.movieticket.config;

import com.movie.movieticket.entity.Seat;
import com.movie.movieticket.entity.SeatStatus;
import com.movie.movieticket.repository.SeatRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*Basically this class is IDEMPOTENT*/
@Slf4j
@Component
public class SeatInitializer {

    private final SeatRepository seatRepository;
    private static final int INITIAL_SEAT_COUNT = 20;
    private static final double DEFAULT_SEAT_PRICE = 150.0;

    @Autowired
    public SeatInitializer(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @PostConstruct
    public void initializeSeats() {
        long totalSeats = seatRepository.count();

        if (totalSeats < INITIAL_SEAT_COUNT){
            int seatsToAdd =  INITIAL_SEAT_COUNT - (int) totalSeats;
            createSeats(seatsToAdd);
        }
    }

    private void createSeats(int count) {
        long existingSeats = seatRepository.count();
        int startNum = (int) existingSeats + 1;

        for (int i=0;i<count;i++){
            int seatNum = startNum + i;
            String seatNumber = generateSeatNumber(seatNum);
            Seat seat = new Seat(seatNumber, DEFAULT_SEAT_PRICE, SeatStatus.AVAILABLE);
            seatRepository.save(seat);
        }
    }

    private String generateSeatNumber(int seatNum) {
        int row = (seatNum - 1) / 10;
        int seatInRow = ((seatNum - 1) % 10) + 1;
        char rowLetter = (char) ('A' + row);
        return rowLetter + String.valueOf(seatInRow);
    }
}
