package com.abcRestaurant.abcRestaurant.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Product")

public class Product {
    @Id
    private ObjectId id;
    private String productId;
    private String productName;
    private String categoryName;
    private float productPrice;
    private String productImage;
    private String productDescription;

}