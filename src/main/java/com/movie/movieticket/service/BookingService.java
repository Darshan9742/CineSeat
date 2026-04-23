package com.movie.movieticket.service;

import com.movie.movieticket.dto.HoldSeatRequest;
import com.movie.movieticket.entity.Hold;
import com.movie.movieticket.entity.HoldStatus;
import com.movie.movieticket.entity.Seat;
import com.movie.movieticket.entity.SeatStatus;
import com.movie.movieticket.repository.HoldRepository;
import com.movie.movieticket.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final SeatRepository seatRepository;
    private final HoldRepository holdRepository;

    public BookingService(SeatRepository seatRepository, HoldRepository holdRepository) {
        this.seatRepository = seatRepository;
        this.holdRepository = holdRepository;
    }

    @Transactional
    public List<Long> holdSeats(HoldSeatRequest holdSeatRequest) {
        List<Long> holdIds = new ArrayList<>();

        //Sorting Ids to prevent deadlocks
        List<Long> requestIds = holdSeatRequest.getSeatIds()
                .stream()
                .sorted()
                .toList();

        //Bulk locking all seat at ones
        List<Seat> lockedSeats = seatRepository.lockMultipleSeats(requestIds);

        //Ensuring all requested seats are found
        if (lockedSeats.size() != requestIds.size()) {
            throw new RuntimeException("One or more seats could not found.");
        }

        for (Seat seat : lockedSeats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE){
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is already Taken.");
            }

            seat.setStatus(SeatStatus.HELD);
            seatRepository.save(seat);

            Hold hold = new Hold(
                    seat,
                    holdSeatRequest.getUserDetails().getName(),
                    holdSeatRequest.getUserDetails().getEmail(),
                    holdSeatRequest.getUserDetails().getPhone()
            );

            hold = holdRepository.save(hold);
            holdIds.add(hold.getId());
        }

        return holdIds;
    }
//    @Transactional
//    public List<Long> holdSeats(HoldSeatRequest holdSeatRequest) {
//        List<Long> holdIds = new ArrayList<>();
//
//        for (Long seatId : holdSeatRequest.getSeatIds()){
//            Seat seat = seatRepository.lockSeat(seatId)
//                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));
//
//            if (seat.getStatus() != SeatStatus.AVAILABLE){
//                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is not available: ");
//            }
//
//            seat.setStatus(SeatStatus.HELD);
//            seatRepository.save(seat);
//
//            Hold hold = new Hold(
//                    seat,
//                    holdSeatRequest.getUserDetails().getName(),
//                    holdSeatRequest.getUserDetails().getEmail(),
//                    holdSeatRequest.getUserDetails().getPhone()
//            );
//            hold = holdRepository.save(hold);
//            holdIds.add(hold.getId());
//        }
//        return holdIds;
//    }

    @Transactional
    public void confirmBooking(List<Long> holdIds){
        List<Hold> holds = holdRepository.findByIdIn(holdIds);
        String seats = "";
        for (Hold hold : holds){
            seats += hold.getSeat().getSeatNumber()+ ", ";
            hold.setStatus(HoldStatus.COMPLETED);
            Seat seat = hold.getSeat();
            seat.setStatus(SeatStatus.BOOKED);
            seatRepository.save(seat);
            holdRepository.save(hold);
        }
    }

    @Transactional
    public void releaseExpiredHolds(){
        LocalDateTime now = LocalDateTime.now();
        List<Hold> expiredHolds = holdRepository.findExpiredHolds(now);

        for (Hold hold : expiredHolds){
            hold.setStatus(HoldStatus.EXPIRED);
            Seat seat = hold.getSeat();
            seat.setStatus(SeatStatus.AVAILABLE);
            holdRepository.save(hold);
            seatRepository.save(seat);
        }
    }
}
