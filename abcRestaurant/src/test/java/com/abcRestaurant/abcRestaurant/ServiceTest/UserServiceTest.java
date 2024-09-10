package com.abcRestaurant.abcRestaurant.ServiceTest;

import com.abcRestaurant.abcRestaurant.DTO.AuthRequestDTO;
import com.abcRestaurant.abcRestaurant.DTO.AuthResponseDTO;
import com.abcRestaurant.abcRestaurant.DTO.UserRequestDTO;
import com.abcRestaurant.abcRestaurant.DTO.UserResponseDTO;
import com.abcRestaurant.abcRestaurant.Exception.EmailAlreadyExistsException;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.User;
import com.abcRestaurant.abcRestaurant.Repository.UserRepository;
import com.abcRestaurant.abcRestaurant.Security.JwtUtil;
import com.abcRestaurant.abcRestaurant.Service.UserService;
import com.abcRestaurant.abcRestaurant.Service.VerificationEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private VerificationEmailService emailService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser_Success() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserEmail("test@example.com");
        userRequestDTO.setUsername("testuser");
        userRequestDTO.setPhoneNumber(1234567890L);
        userRequestDTO.setPassword("password");
        userRequestDTO.setUserType("Admin");

        when(userRepository.existsByUserEmail(userRequestDTO.getUserEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        User result = userService.addUser(userRequestDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAddUser_EmailAlreadyExists() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserEmail("test@example.com");

        when(userRepository.existsByUserEmail(userRequestDTO.getUserEmail())).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.addUser(userRequestDTO);
        });

        assertEquals("Email already exists: test@example.com", exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        User existingUser = new User();
        existingUser.setUserId("user-001");

        User updatedUser = new User();
        updatedUser.setUserId("user-001");
        updatedUser.setUsername("updateduser");

        when(userRepository.existsByUserId("user-001")).thenReturn(true);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser("user-001", updatedUser);

        assertNotNull(result);
        assertEquals("updateduser", result.getUsername());
        verify(userRepository, times(1)).save(updatedUser);
    }


    @Test
    void testUpdateUser_NotFound() {
        User updatedUser = new User();
        updatedUser.setUserId("user-001");

        when(userRepository.findByUserId("user-001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser("user-001", updatedUser);
        });

        assertEquals("User not found with id user-001", exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setUserEmail("test@example.com");
        authRequestDTO.setPassword("password");

        User user = new User();
        user.setUserId("user-001");
        user.setUsername("testuser");
        user.setUserEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setPhoneNumber(1234567890L);
        user.setUserType("Customer");
        user.setProfilePicture("profile.jpg");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(user.getUserId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setUserEmail(user.getUserEmail());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setUserType(user.getUserType());
        userResponseDTO.setProfilePicture(user.getProfilePicture());

        when(userRepository.findByUserEmail(authRequestDTO.getUserEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authRequestDTO.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("dummyToken");

        AuthResponseDTO result = userService.loginUser(authRequestDTO);

        assertNotNull(result);
        assertEquals("dummyToken", result.getToken());
        assertNotNull(result.getUser());
        assertEquals(userResponseDTO.getUserId(), result.getUser().getUserId());
        assertEquals(userResponseDTO.getUsername(), result.getUser().getUsername());
        assertEquals(userResponseDTO.getUserEmail(), result.getUser().getUserEmail());
        assertEquals(userResponseDTO.getPhoneNumber(), result.getUser().getPhoneNumber());
        assertEquals(userResponseDTO.getUserType(), result.getUser().getUserType());
        assertEquals(userResponseDTO.getProfilePicture(), result.getUser().getProfilePicture());
    }



    @Test
    void testLoginUser_InvalidCredentials() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setUserEmail("test@example.com");
        authRequestDTO.setPassword("password");

        User user = new User();
        user.setUserEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("wrongpassword"));

        when(userRepository.findByUserEmail(authRequestDTO.getUserEmail())).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.loginUser(authRequestDTO);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }


    @Test
    void testVerifyCurrentPassword_UserNotFound() {
        when(userRepository.findByUserId("user-001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.verifyCurrentPassword("user-001", "password");
        });

        assertEquals("User not found with id: user-001", exception.getMessage());
    }

    @Test
    void testUpdatePassword_Success() {
        User user = new User();
        user.setUserId("user-001");
        user.setPassword(passwordEncoder.encode("oldpassword"));

        when(userRepository.findByUserId("user-001")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updatePassword("user-001", "newpassword");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        when(userRepository.findByUserId("user-001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updatePassword("user-001", "newpassword");
        });

        assertEquals("User not found with id: user-001", exception.getMessage());
    }
}

