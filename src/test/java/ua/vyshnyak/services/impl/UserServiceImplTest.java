package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.exceptions.EntityAlreadyExistsException;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void getUserByEmail() {
        User user = TestUtils.createUser();
        user.setEmail("test email");
        Mockito.when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User registeredUser = userService.getUserByEmail(user.getEmail());

        assertThat(user, is(registeredUser));
        verify(userRepository).getUserByEmail(user.getEmail());
    }

    @Test
    void getUserByEmailUserNotFound() {
        Mockito.when(userRepository.getUserByEmail("test email")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail("test email"));
    }


    @Test
    void save() {
        User user = TestUtils.createUser();
        userService.save(user);
        verify(userRepository).persist(user);
    }

    @Test
    void saveUserAlreadyExists() {
        User user = TestUtils.createUser();
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EntityAlreadyExistsException.class,() -> userService.save(user));
        verify(userRepository, never()).persist(user);
    }

    @Test
    void remove() {
        User user = TestUtils.createUser();
        userService.remove(user);
        verify(userRepository).delete(user);
    }

    @Test
    void getById() {
        User user = TestUtils.createUser();
        user.setId(1L);
        Mockito.when(userRepository.find(user.getId())).thenReturn(Optional.of(user));

        User registeredUser = userService.getById(user.getId());

        assertThat(user, is(registeredUser));
        verify(userRepository).find(user.getId());
    }

    @Test
    void getByIdUserNotFound() {
        Mockito.when(userRepository.find(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));
    }

    @Test
    void getAll() {
        List<User> users = Arrays.asList(
                TestUtils.createUser(),
                TestUtils.createUser()
        );
        Mockito.when(userRepository.findAll()).thenReturn(users);

        Collection<User> allRegisteredUsers = userService.getAll();

        assertThat(allRegisteredUsers, is(users));
        verify(userRepository).findAll();
    }
}