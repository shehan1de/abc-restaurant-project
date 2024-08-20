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
@Document(collection = "Feedback")

public class Feedback {
    @Id
    private ObjectId id;
    private String feedbackId;
    private String name;
    private String email;
    private long phoneNumber;
    private String subject;
    private String message;
}