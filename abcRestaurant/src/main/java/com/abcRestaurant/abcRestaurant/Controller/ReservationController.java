package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Reservation;
import com.abcRestaurant.abcRestaurant.Service.EmailService;
import com.abcRestaurant.abcRestaurant.Service.ReservationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return new ResponseEntity<>(reservationService.allReservations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Reservation>> getSingleReservation(@PathVariable ObjectId id) {
        return new ResponseEntity<>(reservationService.singleReservation(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation reservation) {
        Reservation newReservation = reservationService.addReservation(reservation);
        try {
            emailService.sendReservationConfirmation(
                    reservation.getEmail(),
                    newReservation.getReservationId(),
                    String.valueOf(reservation.getPersons()),
                    reservation.getDate(),
                    reservation.getTime(),
                    reservation.getBranch()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") ObjectId id, @RequestBody Reservation reservation) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") ObjectId id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
