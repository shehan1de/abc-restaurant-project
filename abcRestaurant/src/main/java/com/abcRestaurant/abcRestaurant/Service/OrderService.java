package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.FinancialReportData;
import com.abcRestaurant.abcRestaurant.Model.Order;
import com.abcRestaurant.abcRestaurant.Model.SalesReportData;
import com.abcRestaurant.abcRestaurant.Repository.OrderRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Method to get all Orders
    public List<Order> allOrders() {
        return orderRepository.findAll();
    }

    // Method to get a single order by id
    public Order singleOrder(ObjectId id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    // Method to find orders by userId
    public List<Order> findOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    // Method to add a new order
    public Order addOrder(Order order) {
        String orderId = generateOrderId();
        order.setOrderId(orderId);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("Pending");
        return orderRepository.save(order);
    }

    // Method to update an existing order by id
    public Order updateOrder(ObjectId id, Order order) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
        order.setId(id);
        return orderRepository.save(order);
    }

    // Method to delete an order by id
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

    // Method to update order status
    public Order updateOrderStatus(String orderId, String orderStatus) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    // Method to find a single order by id
    public Order findOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
    }

    // Method to get sales report data
    public SalesReportData getSalesReportData(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getOrderDate().isAfter(startDate) && order.getOrderDate().isBefore(endDate))
                .collect(Collectors.toList());

        double totalRevenue = orders.stream().mapToDouble(Order::getFinalAmount).sum();
        long totalOrders = orders.size();
        long totalProductsSold = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .mapToInt(Order.OrderItem::getQuantity)
                .sum();

        Map<String, Long> topSellingProducts = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(Order.OrderItem::getProductName, Collectors.summingLong(Order.OrderItem::getQuantity)));

        // Sort top-selling products by quantity
        topSellingProducts = topSellingProducts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new SalesReportData(totalRevenue, totalOrders, totalProductsSold, topSellingProducts);
    }

    public FinancialReportData getFinancialReportData(LocalDateTime startDate, LocalDateTime endDate) {
        // Fetch orders within the date range
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

        double totalRevenue = 0.0;
        double totalExpenses = 0.0;
        double totalTaxes = 0.0;

        for (Order order : orders) {
            totalRevenue += order.getFinalAmount();
            totalExpenses += order.getDeliveryCharges() + order.getDiscountAmount();
            totalTaxes += order.getTaxAmount();
        }

        double netProfit = totalRevenue - totalExpenses;

        return new FinancialReportData(totalRevenue, totalExpenses, netProfit, totalTaxes);
    }
}



