package com.abcRestaurant.abcRestaurant.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "User")

public class User {
    @Id
    private ObjectId id;
    private String userId;
    private String username;
    private String password;
    private String userEmail;
    private Long phoneNumber;
    private String profilePicture;
    private String userType;
    private String verificationCode;
    private LocalDateTime verificationCodeExpiry;
    private String branch;


    public void updateProfile(String username, Long phoneNumber, String profilePicture) {
        if (username != null && !username.isEmpty()) {
            this.username = username;
        }
        if (phoneNumber != null && phoneNumber > 0) {
            this.phoneNumber = phoneNumber;
        }
        if (profilePicture != null && !profilePicture.isEmpty()) {
            this.profilePicture = profilePicture;
        }
    }

}

