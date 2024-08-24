package com.abcRestaurant.abcRestaurant.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Cart")
public class Cart {
    @Id
    private ObjectId id;
    private String userId;
    private Map<String, Integer> productId;
    private double totalAmount;
}
