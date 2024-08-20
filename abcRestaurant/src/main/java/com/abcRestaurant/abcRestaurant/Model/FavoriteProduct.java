package com.abcRestaurant.abcRestaurant.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "FavoriteProduct")
public class FavoriteProduct {
    @Id
    private ObjectId id;
    private String userId;
    private List<String> productIds;
}
