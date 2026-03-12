package com.movie.movieticket.controller;

import com.movie.movieticket.dto.CreateSeatsRequest;
import com.movie.movieticket.dto.HoldSeatRequest;
import com.movie.movieticket.entity.Seat;
import com.movie.movieticket.entity.SeatStatus;
import com.movie.movieticket.repository.SeatRepository;
import com.movie.movieticket.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seats")
@CrossOrigin
public class SeatController {

    private final SeatRepository seatRepository;
    private final BookingService bookingService;

    @Autowired
    public SeatController(SeatRepository seatRepository, BookingService bookingService) {
        this.seatRepository = seatRepository;
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    /*HoldSeatReqest has List<Long> to hold seat Id's, so we the data from front end, comes to the backend from here*/
    @PostMapping("/hold")
    public ResponseEntity<Map<String, Object>> holdSeats(@RequestBody HoldSeatRequest request){
        Map<String, Object> response = new HashMap<>();

        try{
            List<Long> holdIds = bookingService.holdSeats(request);
            response.put("success", true);
            response.put("holdIds", holdIds);
            response.put("message", "Seats held successfully!");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/bulk")
    public List<Seat> createSeats(@RequestBody CreateSeatsRequest request) {
        List<Seat> seats = request.getSeatNumbers()
                .stream()
                .map(seatNumber -> new Seat(seatNumber, 150.0, SeatStatus.AVAILABLE))
                .toList();

        return seatRepository.saveAll(seats);
    }

}
