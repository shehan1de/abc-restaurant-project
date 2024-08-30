package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.DTO.AuthRequestDTO;
import com.abcRestaurant.abcRestaurant.DTO.AuthResponseDTO;
import com.abcRestaurant.abcRestaurant.DTO.PasswordChangeRequest;
import com.abcRestaurant.abcRestaurant.DTO.UserRequestDTO;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.User;
import com.abcRestaurant.abcRestaurant.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.allUser(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<User>> getSingleUser(@PathVariable String userId) {
        return new ResponseEntity<>(userService.singleUser(userId), HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        User newUser = userService.addUser(userRequestDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/userAdd")
    public ResponseEntity<User> userAdd(@RequestBody User user) {
        User newUser = userService.userAdd(user);
        return new ResponseEntity<>(newUser,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponse = userService.loginUser(authRequestDTO);
        if (authResponse == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") String userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserProfile(
            @PathVariable("userId") String userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long phoneNumber,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String branch,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture)
    {

        Map<String, Object> response = new HashMap<>();
        try {
            User updatedUser = userService.updateUserProfile(userId, username, phoneNumber, profilePicture,userEmail,userType,branch);

            response.put("status", "success");
            response.put("user", updatedUser);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "File upload failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (ResourceNotFoundException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PostMapping("/verify-and-change-password")
    public ResponseEntity<Map<String, Object>> verifyAndChangePassword(
            @RequestBody PasswordChangeRequest passwordChangeRequest,
            @RequestHeader("Authorization") String token) {

        Map<String, Object> response = new HashMap<>();
        try {
            String userId = passwordChangeRequest.getUserId(); // Get userId directly from request

            boolean isPasswordValid = userService.verifyCurrentPassword(userId, passwordChangeRequest.getCurrentPassword());

            if (!isPasswordValid) {
                response.put("status", "error");
                response.put("message", "Current password is incorrect.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            userService.updatePassword(userId, passwordChangeRequest.getNewPassword());

            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "An error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




}
