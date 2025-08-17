package myapp.controllers;

import myapp.controller.UserController;
import myapp.dto.UserDto;
import myapp.exceptions.DaoException;
import myapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final Long USER_ID = 1L;
    private static final String USER_NAME_1 = "Test";
    private static final String USER_NAME_2 = "DopTest";
    private static final String USER_NAME_NEW = "NewTest";
    private static final String USER_EMAIL_1 = "Test@example.com";
    private static final String USER_EMAIL_2 = "DopTest@example.com";
    private static final String USER_EMAIL_NEW = "NewTest@example.com";
    private static final int USER_AGE_1 = 25;
    private static final int USER_AGE_2 = 30;
    private static final int USER_AGE_NEW = 28;
    private static final String CONTENT_TYPE_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void getAllUsers_returnsUsersList() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(USER_ID, USER_NAME_1, USER_EMAIL_1, USER_AGE_1),
                new UserDto(2L, USER_NAME_2, USER_EMAIL_2, USER_AGE_2)
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(users.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(USER_NAME_1));
    }

    @Test
    void getUserById_found_returnsUser() throws Exception {
        UserDto user = new UserDto(USER_ID, USER_NAME_1, USER_EMAIL_1, USER_AGE_1);

        when(userService.getUserById(USER_ID)).thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", USER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(USER_EMAIL_1));
    }

    @Test
    void getUserById_notFound_returns404() throws Exception {
        when(userService.getUserById(USER_ID)).thenThrow(new DaoException("Пользователь не найден"));

        mockMvc.perform(get("/api/users/{id}", USER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createUser_validInput_returnsCreatedUser() throws Exception {
        UserDto input = new UserDto(null, USER_NAME_NEW, USER_EMAIL_NEW, USER_AGE_NEW);
        UserDto created = new UserDto(USER_ID, USER_NAME_NEW, USER_EMAIL_NEW, USER_AGE_NEW);

        when(userService.createUser(any(UserDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/users")
                        .contentType(CONTENT_TYPE_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(USER_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(USER_NAME_NEW));
    }

    @Test
    void updateUser_validInput_returnsUpdatedUser() throws Exception {
        UserDto input = new UserDto(null, USER_NAME_2, USER_EMAIL_2, USER_AGE_2);
        UserDto updated = new UserDto(USER_ID, USER_NAME_2, USER_EMAIL_2, USER_AGE_2);

        when(userService.updateUser(eq(USER_ID), any(UserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/{id}", USER_ID)
                        .contentType(CONTENT_TYPE_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(USER_NAME_2));
    }

    @Test
    void updateUser_notFound_returns404() throws Exception {
        UserDto input = new UserDto(null, USER_NAME_NEW, USER_EMAIL_NEW, USER_AGE_NEW);

        when(userService.updateUser(eq(USER_ID), any(UserDto.class))).thenThrow(new DaoException("Пользователь не найден"));

        mockMvc.perform(put("/api/users/{id}", USER_ID)
                        .contentType(CONTENT_TYPE_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteUser_callsService_returnsNoContent() throws Exception {
        doNothing().when(userService).deleteUser(USER_ID);

        mockMvc.perform(delete("/api/users/{id}", USER_ID))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(userService).deleteUser(USER_ID);
    }
}
