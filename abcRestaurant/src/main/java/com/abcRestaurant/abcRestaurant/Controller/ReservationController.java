package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Reservation;
import com.abcRestaurant.abcRestaurant.Service.ConfirmResEmailService;
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
    @Autowired
    private ConfirmResEmailService confirmResEmailService;


    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return new ResponseEntity<>(reservationService.allReservations(), HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<Optional<Reservation>> getSingleReservation(@PathVariable String reservationId) {
        return new ResponseEntity<>(reservationService.singleReservation(reservationId), HttpStatus.OK);
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

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<Reservation> updateReservationStatus(@PathVariable String reservationId, @RequestParam String status) {
        try {
            Reservation updatedReservation = reservationService.updateReservationStatus(reservationId, status);
            // Send email notification after updating reservation status
            confirmResEmailService.sendStatusUpdateEmail(
                    updatedReservation.getEmail(),
                    reservationId,
                    status,
                    updatedReservation.getDate(),
                    updatedReservation.getTime(),
                    updatedReservation.getBranch()
            );
            return ResponseEntity.ok(updatedReservation);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
