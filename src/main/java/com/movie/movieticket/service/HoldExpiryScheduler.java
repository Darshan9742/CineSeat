package com.movie.movieticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HoldExpiryScheduler {
    private final BookingService bookingService;

    @Autowired
    public HoldExpiryScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 6000)
    public void releaseExpiredSeats() {
        bookingService.releaseExpiredHolds();
    }
}
