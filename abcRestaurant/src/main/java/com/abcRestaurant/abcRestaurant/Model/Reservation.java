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
@Document(collection = "Reservation")

public class Reservation {
    @Id
    private ObjectId id;
    private String reservationId;
    private String name;
    private String email;
    private String branch;
    private long phoneNumber;
    private String date;
    private String time;
    private int persons;
    private String request;
    private String status;
}
