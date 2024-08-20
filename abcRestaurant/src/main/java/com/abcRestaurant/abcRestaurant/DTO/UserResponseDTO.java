package com.abcRestaurant.abcRestaurant.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String userId;
    private String username;
    private String userEmail;
    private long phoneNumber;
    private String userType;
    private String profilePicture;
}
