package myapp.controller;

import myapp.dto.UserDto;
import myapp.dto.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import myapp.service.UserService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Management", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить всех пользователей")
    public ResponseEntity<CollectionModel<EntityModel<UserResource>>> getAllUsers() {
        List<UserResource> users = userService.getAllUsers();
        
        List<EntityModel<UserResource>> userResources = users.stream()
                .map(user -> EntityModel.of(user)
                        .add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()))
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<UserResource>> collectionModel = CollectionModel.of(userResources);
        collectionModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    public ResponseEntity<EntityModel<UserResource>> getUser(@PathVariable Long id) {
        UserResource user = userService.getUserById(id);
        
        EntityModel<UserResource> userResource = EntityModel.of(user)
                .add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
        
        return ResponseEntity.ok(userResource);
    }

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public ResponseEntity<EntityModel<UserResource>> createUser(@Valid @RequestBody UserDto userDto) {
        UserResource created = userService.createUser(userDto);
        
        EntityModel<UserResource> userResource = EntityModel.of(created)
                .add(linkTo(methodOn(UserController.class).getUser(created.getId())).withSelfRel());
        
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    public ResponseEntity<EntityModel<UserResource>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        UserResource updated = userService.updateUser(id, userDto);
        
        EntityModel<UserResource> userResource = EntityModel.of(updated)
                .add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
        
        return ResponseEntity.ok(userResource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
