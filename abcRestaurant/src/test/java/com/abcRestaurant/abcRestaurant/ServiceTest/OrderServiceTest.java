package com.abcRestaurant.abcRestaurant.ServiceTest;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Order;
import com.abcRestaurant.abcRestaurant.Repository.OrderRepository;
import com.abcRestaurant.abcRestaurant.Service.OrderService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllOrders() {
        Order order1 = new Order();
        Order order2 = new Order();
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.allOrders();

        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testSingleOrderFound() {
        ObjectId id = new ObjectId();
        Order order = new Order();
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        Order result = orderService.singleOrder(id);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void testSingleOrderNotFound() {
        ObjectId id = new ObjectId();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.singleOrder(id));
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void testFindOrdersByUserId() {
        String userId = "user-123";
        Order order1 = new Order();
        order1.setUserId(userId);
        when(orderRepository.findByUserId(userId)).thenReturn(Collections.singletonList(order1));

        List<Order> result = orderService.findOrdersByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        verify(orderRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testAddOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.addOrder(order);

        assertNotNull(savedOrder.getOrderId());
        assertNotNull(savedOrder.getOrderDate());
        assertEquals("Pending", savedOrder.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateOrder() {
        ObjectId id = new ObjectId();
        Order order = new Order();
        when(orderRepository.existsById(id)).thenReturn(true);
        when(orderRepository.save(order)).thenReturn(order);

        Order updatedOrder = orderService.updateOrder(id, order);

        assertEquals(id, updatedOrder.getId());
        verify(orderRepository, times(1)).existsById(id);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateOrderNotFound() {
        ObjectId id = new ObjectId();
        Order order = new Order();
        when(orderRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(id, order));
        verify(orderRepository, times(1)).existsById(id);
        verify(orderRepository, never()).save(order);
    }

    @Test
    void testDeleteOrder() {
        ObjectId id = new ObjectId();
        when(orderRepository.existsById(id)).thenReturn(true);

        orderService.deleteOrder(id);

        verify(orderRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteOrderNotFound() {
        ObjectId id = new ObjectId();
        when(orderRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(id));
        verify(orderRepository, times(1)).existsById(id);
        verify(orderRepository, never()).deleteById(id);
    }

    @Test
    void testUpdateOrderStatus() {
        String orderId = "order-001";
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus("Pending");

        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus(orderId, "Accepted");

        assertEquals("Accepted", updatedOrder.getOrderStatus());
        verify(orderRepository, times(1)).findByOrderId(orderId);
        verify(orderRepository, times(1)).save(order);
    }



}
