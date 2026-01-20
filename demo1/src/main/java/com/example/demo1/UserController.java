package com.example.demo1;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long id) {
        UserResponse userResponse = userService.getUser(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = userService.getAllUsers();
        return ResponseEntity.ok(userResponses);
    }

    @PostMapping
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.saveUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable long id, @Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
