package com.abcRestaurant.abcRestaurant.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Order")
public class Order {
    @Id
    private ObjectId id;
    private String orderId;
    private String userId;
    private String userEmail;
    private List<OrderItem> items;
    private LocalDateTime orderDate;
    private String branch;
    private String paymentMethod;
    private String deliveryAddress;
    private String offerId;
    private float taxAmount;
    private float deliveryCharges;
    private float discountAmount;
    private float finalAmount;
    private String orderStatus;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class OrderItem {
        private String productId;
        private String productName;
        private int quantity;
        private float price;
    }


}
