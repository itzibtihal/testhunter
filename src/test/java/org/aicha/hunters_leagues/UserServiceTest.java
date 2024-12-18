package org.aicha.hunters_leagues;

import org.aicha.hunters_leagues.domain.User;
import org.aicha.hunters_leagues.exception.exps.EmailAlreadyExisteException;
import org.aicha.hunters_leagues.exception.exps.InvalidPasswordException;
import org.aicha.hunters_leagues.exception.exps.ResourceNotFoundException;
import org.aicha.hunters_leagues.repository.UserRepository;
import org.aicha.hunters_leagues.service.UserService;
import org.aicha.hunters_leagues.utils.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser_EmailAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExisteException.class, () -> userService.addSUser(user));
    }


    @Test
    public void testLogin_InvalidPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(PasswordUtil.hashPassword("password"));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(InvalidPasswordException.class, () -> userService.login("test@example.com", "wrongpassword"));
    }


    @Test
    public void testUpdateUser_UserNotFound() {
        UUID userId = UUID.randomUUID();
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, user));
    }

}