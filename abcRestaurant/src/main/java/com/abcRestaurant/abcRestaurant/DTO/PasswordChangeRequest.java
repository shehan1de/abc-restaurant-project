package com.abcRestaurant.abcRestaurant.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class PasswordChangeRequest {
    private String userId;
    private String currentPassword;
    private String newPassword;


}
