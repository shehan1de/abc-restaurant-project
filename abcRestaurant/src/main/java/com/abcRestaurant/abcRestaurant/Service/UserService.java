package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.DTO.AuthRequestDTO;
import com.abcRestaurant.abcRestaurant.DTO.AuthResponseDTO;
import com.abcRestaurant.abcRestaurant.DTO.UserRequestDTO;
import com.abcRestaurant.abcRestaurant.DTO.UserResponseDTO;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.User;
import com.abcRestaurant.abcRestaurant.Repository.UserRepository;
import com.abcRestaurant.abcRestaurant.Security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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

    public Optional<User> singleUser(String userId) {
        return userRepository.findByUserId(userId);
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

    public User updateUser(String userId, User user) {
        if (!userRepository.existsByUserId(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        user.setUserId(userId);
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsByUserId(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        userRepository.deleteByUserId(userId);
    }

    public AuthResponseDTO loginUser(AuthRequestDTO authRequestDTO) {
        User user = userRepository.findByUserEmail(authRequestDTO.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + authRequestDTO.getUserEmail()));
        if (passwordEncoder.matches(authRequestDTO.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user);
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUserId(), user.getUsername(), user.getUserEmail(), user.getPhoneNumber(), user.getUserType(), user.getProfilePicture());
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





    public User updateUserProfile(String userId, String username, Long phoneNumber, MultipartFile profilePicture) throws IOException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        // Handle profile picture upload
        String profilePictureFilename = handleProfilePictureUpload(profilePicture);

        // Update user details
        if (username != null && !username.isEmpty()) {
            user.setUsername(username);
        }
        if (phoneNumber != null && phoneNumber > 0) {
            user.setPhoneNumber(phoneNumber);
        }
        if (profilePictureFilename != null) {
            user.setProfilePicture(profilePictureFilename);
        }

        // Save and return the updated user
        return userRepository.save(user);
    }


    // Define the upload path
    @Value("${file.upload-dir}")
    private String uploadDir;
    public String handleProfilePictureUpload(MultipartFile profilePicture) throws IOException {
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String originalFilename = profilePicture.getOriginalFilename();

            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            String profilePictureFilename = "propic-" + timestamp + fileExtension;



            File file = new File(uploadDir, profilePictureFilename);


            // Ensure the directory exists
            file.getParentFile().mkdirs();

            // Save the file
            profilePicture.transferTo(file);

            return profilePictureFilename;
        }
        return null;
    }


    public boolean verifyCurrentPassword(String userId, String currentPassword) throws ResourceNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if the provided current password matches the hashed password in the database
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    public void updatePassword(String userId, String newPassword) throws ResourceNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Encode the new password and update the user
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
