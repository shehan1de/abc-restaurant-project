package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.DTO.AuthRequestDTO;
import com.abcRestaurant.abcRestaurant.DTO.AuthResponseDTO;
import com.abcRestaurant.abcRestaurant.DTO.UserRequestDTO;
import com.abcRestaurant.abcRestaurant.Model.User;
import com.abcRestaurant.abcRestaurant.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.allUser(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getSingleUser(@PathVariable ObjectId id) {
        return new ResponseEntity<>(userService.singleUser(id), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        User newUser = userService.addUser(userRequestDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }




    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponse = userService.loginUser(authRequestDTO);
        if (authResponse == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") ObjectId id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") ObjectId id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

