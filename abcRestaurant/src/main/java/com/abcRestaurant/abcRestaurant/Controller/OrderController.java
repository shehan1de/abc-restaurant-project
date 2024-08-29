package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Order;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Service.ConfirmOrdEmailService;
import com.abcRestaurant.abcRestaurant.Service.OrderService;
import com.abcRestaurant.abcRestaurant.Service.OrderEmailService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderEmailService orderEmailService;
    private final ConfirmOrdEmailService confirmOrdEmailService;

    @Autowired
    public OrderController(OrderService orderService, OrderEmailService orderEmailService, ConfirmOrdEmailService confirmOrdEmailService) {
        this.orderService = orderService;
        this.orderEmailService = orderEmailService;
        this.confirmOrdEmailService = confirmOrdEmailService;
    }


    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.allOrders(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String userId) {
        List<Order> orders = orderService.findOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        try {
            // Add the order to the database
            Order newOrder = orderService.addOrder(order);

            // Send an order confirmation email
            orderEmailService.sendOrderConfirmation(
                    newOrder.getUserEmail(),
                    newOrder.getOrderId(),
                    newOrder.getItems(),
                    newOrder.getTaxAmount(),
                    newOrder.getDeliveryCharges(),
                    newOrder.getDiscountAmount(),
                    newOrder.getFinalAmount()
            );

            // Return the new order with HTTP status 201 Created
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception and return a 500 Internal Server Error response
            e.printStackTrace(); // Optionally log the error message
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable ObjectId id, @RequestBody Order order) {
        Order updatedOrder = orderService.updateOrder(id, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable ObjectId id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam String orderStatus) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, orderStatus);

            if (updatedOrder == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Send email notification after updating order status
            confirmOrdEmailService.sendOrderConfirmation(
                    updatedOrder.getUserEmail(),
                    orderId,
                    updatedOrder.getFinalAmount(),
                    orderStatus
            );

            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
