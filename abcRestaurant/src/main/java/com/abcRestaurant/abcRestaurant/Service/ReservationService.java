package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Feedback;
import com.abcRestaurant.abcRestaurant.Model.Reservation;
import com.abcRestaurant.abcRestaurant.Repository.ReservationRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    // Get all reservations
    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    // Get a single reservation by reservationId
    public Optional<Reservation> singleReservation(String reservationId) {
        return reservationRepository.findByReservationId(reservationId);
    }

    // Add a new reservation
    public Reservation addReservation(Reservation reservation) {
        reservation.setReservationId(generateReservationId());
        return reservationRepository.save(reservation);
    }

    // Generate a new reservation ID
    private String generateReservationId() {
        List<Reservation> reservations = reservationRepository.findAll();
        int maxId = 0;
        for (Reservation reservation : reservations) {
            String reservationId = reservation.getReservationId();
            try {
                int numericPart = Integer.parseInt(reservationId.split("-")[1]);
                if (numericPart > maxId) {
                    maxId = numericPart;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error parsing reservationId: " + reservationId + ". Skipping this entry.");
            }
        }
        int nextId = maxId + 1;
        return String.format("reservation-%03d", nextId);
    }

    public Reservation updateReservationStatus(String reservationId, String status) {
        Reservation reservation = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + reservationId));

        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }


}
