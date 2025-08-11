package service;

import dao.UserDao;
import exceptions.DaoException;
import exceptions.ValidationException;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_validUser_success() throws Exception {
        User user = User.builder()
                .name("Test")
                .email("Test@example.com")
                .age(12)
                .build();

        userService.createUser(user);

        verify(userDao, times(1)).create(user);
    }

    @Test
    void createUser_invalidEmail_throwsValidationException() {
        User user = User.builder()
                .name("Test")
                .email("bad-test-email")
                .age(25)
                .build();

        assertThrows(ValidationException.class, () -> userService.createUser(user));

        verify(userDao, never()).create(any());
    }

    @Test
    void getUserById_found_returnsUser() throws Exception {
        User user = new User(1L, "DopTest", "DopTest@example.com", 15, null);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("DopTest", result.getName());
        verify(userDao).findById(1L);
    }

    @Test
    void getUserById_notFound_throwsDaoException() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DaoException.class, () -> userService.getUserById(99L));
        verify(userDao).findById(99L);
    }

    @Test
    void getAllUsers_returnsList() throws Exception {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "A", "a@example.com", 20, null),
                new User(2L, "B", "b@example.com", 30, null)
        );
        when(userDao.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userDao).findAll();
    }

    @Test
    void updateUser_validData_updatesAndReturnsUser() throws Exception {
        User existing = new User(1L, "Old", "old@example.com", 40, null);
        when(userDao.findById(1L)).thenReturn(Optional.of(existing));

        User updated = userService.updateUser(1L, "New", "new@example.com", 45);

        assertEquals("New", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(45, updated.getAge());
        verify(userDao).update(existing);
    }

    @Test
    void updateUser_userNotFound_throwsDaoException() {
        when(userDao.findById(123L)).thenReturn(Optional.empty());

        assertThrows(DaoException.class,
                () -> userService.updateUser(123L, "Not", "Not@example.com", 20));
        verify(userDao, never()).update(any());
    }

    @Test
    void updateUser_invalidEmail_throwsValidationException() {
        User existing = new User(1L, "Old", "old@example.com", 40, null);
        when(userDao.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(ValidationException.class,
                () -> userService.updateUser(1L, "Old", "badEmail", 40));

        verify(userDao, never()).update(any());
    }

    @Test
    void deleteUser_callsDaoDelete() throws Exception {
        userService.deleteUser(1L);
        verify(userDao).delete(1L);
    }
}
