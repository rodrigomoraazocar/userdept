package com.roroapi.userdept.services;

import com.roroapi.userdept.entities.User;
import com.roroapi.userdept.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("serial")
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void findAll_ShouldReturnAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        assertThat(userService.findAll()).containsExactly(user);
    }

    @Test
    public void findById_ShouldReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThat(userService.findById(1L)).contains(user);
    }

    @Test
    public void save_ShouldReturnSavedUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThat(userService.save(user)).isEqualTo(user);
    }

    @Test
    public void update_ShouldReturnUpdatedUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Existing User");
        existingUser.setEmail("existing@example.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        assertThat(userService.update(1L, updatedUser)).isEqualTo(updatedUser);
    }

    @Test
    public void handleException_ShouldThrowRuntimeException() {
        when(userRepository.findAll()).thenThrow(new DataAccessException("Database error") {});

        assertThatThrownBy(() -> userService.findAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error fetching users from database");
    }
}
