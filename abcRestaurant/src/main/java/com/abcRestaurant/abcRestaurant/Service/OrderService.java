package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Order;
import com.abcRestaurant.abcRestaurant.Repository.OrderRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Get all Orders
    public List<Order> allOrders() {
        return orderRepository.findAll();
    }

    // Get a single order by id
    public Order singleOrder(ObjectId id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }
    public List<Order> findOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
    // Add a new order
    public Order addOrder(Order order) {
        // Generate and set orderId
        String orderId = generateOrderId();
        order.setOrderId(orderId);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("Pending");
        return orderRepository.save(order);
    }

    // Update an existing order by id
    public Order updateOrder(ObjectId id, Order order) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
        order.setId(id);
        return orderRepository.save(order);
    }

    // Delete an order by id
    public void deleteOrder(ObjectId id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }

    // Generate a unique order ID
    private String generateOrderId() {
        long count = orderRepository.count();
        return String.format("order-%03d", count + 1);
    }
}
