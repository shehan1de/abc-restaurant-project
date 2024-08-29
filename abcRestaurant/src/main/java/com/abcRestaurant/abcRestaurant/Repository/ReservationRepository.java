package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.Reservation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReservationRepository extends MongoRepository<Reservation, ObjectId> {
    Optional<Reservation> findByReservationId(String reservationId);
}
