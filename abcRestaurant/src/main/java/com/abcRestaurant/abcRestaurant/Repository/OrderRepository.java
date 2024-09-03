package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.Reservation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.abcRestaurant.abcRestaurant.Model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

    List<Order> findByUserId(String userId);
    Optional<Order> findByOrderId(String orderId);
    Optional<Object> findById(String orderId);

    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
