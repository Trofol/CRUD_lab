package service;

import myapp.dto.UserDto;
import myapp.exceptions.UserNotFoundException;
import myapp.exceptions.ValidationException;
import myapp.model.User;
import myapp.repository.UserRepository;
import myapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService; // Твой сервис с UserRepository и DTO

    @Test
    void createUser_validUser_success() {
        UserDto dto = UserDto.builder()
                .name("Test")
                .email("test@example.com")
                .age(25)
                .build();

        User savedUser = User.builder()
                .id(1L)
                .name(dto.getName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(dto.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_invalidEmail_throwsValidationException() {
        UserDto dto = UserDto.builder()
                .name("Test")
                .email("bad-email")
                .age(25)
                .build();

        assertThrows(ValidationException.class, () -> userService.createUser(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_found_returnsDto() {
        User user = User.builder()
                .id(1L)
                .name("User")
                .email("user@example.com")
                .age(30)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertEquals(user.getName(), result.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_notFound_throwsDaoException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
        verify(userRepository).findById(99L);
    }

    @Test
    void getAllUsers_returnsListOfDto() {
        List<User> users = List.of(
                User.builder().id(1L).name("A").email("a@example.com").age(20).build(),
                User.builder().id(2L).name("B").email("b@example.com").age(25).build()
        );

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> dtos = userService.getAllUsers();

        assertEquals(2, dtos.size());
        verify(userRepository).findAll();
    }

    @Test
    void updateUser_validData_updatesAndReturnsDto() {
        Long id = 1L;
        UserDto dto = UserDto.builder()
                .name("NewName")
                .email("newemail@example.com")
                .age(40)
                .build();

        User existingUser = User.builder()
                .id(id)
                .name("OldName")
                .email("oldemail@example.com")
                .age(30)
                .build();

        User updatedUser = User.builder()
                .id(id)
                .name(dto.getName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDto result = userService.updateUser(id, dto);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getAge(), result.getAge());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_invalidEmail_throwsValidationException() {
        Long id = 1L;

        UserDto dto = UserDto.builder()
                .email("bad-email")
                .build();

        User existingUser = User.builder()
                .id(id)
                .name("Name")
                .email("email@example.com")
                .age(25)
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        assertThrows(ValidationException.class, () -> userService.updateUser(id, dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_userNotFound_throwsDaoException() {
        Long id = 123L;
        UserDto dto = UserDto.builder().build();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(id, dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_callsRepositoryDelete() {
        Long id = 1L;

        doNothing().when(userRepository).deleteById(id);

        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }
}
