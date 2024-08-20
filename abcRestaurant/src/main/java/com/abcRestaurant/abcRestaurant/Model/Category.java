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
@Document(collection = "Category")

public class Category {
    @Id
    private ObjectId id;
    private String categoryId;
    private String categoryName;
    private String categoryDescription;
    private String categoryImage;
}