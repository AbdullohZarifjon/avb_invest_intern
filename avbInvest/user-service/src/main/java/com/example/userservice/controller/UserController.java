package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserGetAllDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(userService.createUser(userCreateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetAllDto> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(userService.updateUser(id, userCreateDto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/company/{id}")
    @Operation(summary = "Delete users by company id")
    public ResponseEntity<Void> deleteUsersByCompanyId(@PathVariable Integer id) {
        userService.deleteUsersByCompanyId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get list of all users")
    public ResponseEntity<List<UserGetAllDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/company/{id}")
    @Operation(summary = "Get Users By Company Id")
    public List<UserResponseDto> getUsersByCompanyId(@PathVariable Integer id) {
        return userService.getUserByCompanyId(id);
    }
}
