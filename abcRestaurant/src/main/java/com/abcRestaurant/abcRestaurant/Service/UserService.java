package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.DTO.AuthRequestDTO;
import com.abcRestaurant.abcRestaurant.DTO.AuthResponseDTO;
import com.abcRestaurant.abcRestaurant.DTO.UserRequestDTO;
import com.abcRestaurant.abcRestaurant.DTO.UserResponseDTO;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.User;
import com.abcRestaurant.abcRestaurant.Repository.UserRepository;
import com.abcRestaurant.abcRestaurant.Security.JwtUtil;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final VerificationEmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, VerificationEmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
        return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getPassword(), new ArrayList<>());
    }

    public List<User> allUser() {
        return userRepository.findAll();
    }

    public Optional<User> singleUser(ObjectId id) {
        return userRepository.findById(id);
    }

    public User addUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUserId(generateUserId());
        user.setUsername(userRequestDTO.getUsername());
        user.setUserEmail(userRequestDTO.getUserEmail());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setUserType(userRequestDTO.getUserType());

        String profilePicture = userRequestDTO.getProfilePicture();
        user.setProfilePicture(profilePicture != null && !profilePicture.isEmpty() ? profilePicture : "default.jpg");

        return userRepository.save(user);
    }


    private String generateUserId() {
        long count = userRepository.count();
        return String.format("user-%03d", count + 1);
    }

    public User updateUser(ObjectId id, User user) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        user.setId(id);
        return userRepository.save(user);
    }

    public void deleteUser(ObjectId id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    public AuthResponseDTO loginUser(AuthRequestDTO authRequestDTO) {
        User user = userRepository.findByUserEmail(authRequestDTO.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + authRequestDTO.getUserEmail()));
        if (passwordEncoder.matches(authRequestDTO.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user);
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUserId(), user.getUsername(), user.getUserEmail(), user.getPhoneNumber(), user.getUserType(),user.getProfilePicture());
            return new AuthResponseDTO(token, userResponseDTO);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void sendVerificationCode(String email) {
        Optional<User> userOptional = userRepository.findByUserEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String code = generateVerificationCode();
            user.setVerificationCode(code);
            user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(3));
            userRepository.save(user);
            emailService.sendVerificationCode(email, code);
        } else {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
    }

    public boolean verifyCode(String email, String code) {
        Optional<User> userOptional = userRepository.findByUserEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getVerificationCode() != null &&
                    user.getVerificationCode().equals(code) &&
                    user.getVerificationCodeExpiry().isAfter(LocalDateTime.now());
        }
        return false;
    }

    public void resetPassword(String email, String code, String newPassword) {
        Optional<User> userOptional = userRepository.findByUserEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getVerificationCode() != null &&
                    user.getVerificationCode().equals(code) &&
                    user.getVerificationCodeExpiry().isAfter(LocalDateTime.now())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setVerificationCode(null);
                user.setVerificationCodeExpiry(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid or expired verification code.");
            }
        } else {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Scheduled(fixedRate = 60000) // Run every minute (60000 milliseconds)
    public void removeExpiredVerificationCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = userRepository.findByVerificationCodeExpiryBefore(now);
        for (User user : users) {
            user.setVerificationCode(null);
            user.setVerificationCodeExpiry(null);
            userRepository.save(user);
        }
    }
}
