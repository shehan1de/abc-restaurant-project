package com.abcRestaurant.abcRestaurant.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String username;
    private String password;
    private String userEmail;
    private long phoneNumber;
    private String UserType;
    private String profilePicture;
}